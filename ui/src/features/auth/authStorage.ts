import type { User } from "./types";

const TOKEN_KEY = "hospital_access_token";
const USER_KEY = "hospital_user";

interface JwtPayload {
  exp?: number;
}

const parseJwtPayload = (token: string): JwtPayload | null => {
  try {
    const [, payload] = token.split(".");

    if (!payload) {
      return null;
    }

    const base64 = payload.replace(/-/g, "+").replace(/_/g, "/");
    const paddedBase64 = base64.padEnd(
      Math.ceil(base64.length / 4) * 4,
      "=",
    );

    return JSON.parse(window.atob(paddedBase64)) as JwtPayload;
  } catch {
    return null;
  }
};

const getTokenExpirationTime = (token: string): number | null => {
  const payload = parseJwtPayload(token);

  if (!payload?.exp) {
    return null;
  }

  return payload.exp * 1000;
};

export const authStorage = {
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  },

  getTokenExpirationTime,

  isTokenExpired(token: string): boolean {
    const expirationTime = getTokenExpirationTime(token);

    if (!expirationTime) {
      return false;
    }

    return expirationTime <= Date.now();
  },

  getUser(): User | null {
    const user = localStorage.getItem(USER_KEY);

    if (!user) {
      return null;
    }

    try {
      return JSON.parse(user) as User;
    } catch {
      return null;
    }
  },

  setAuth(token: string, user: User): void {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
  },

  clearAuth(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  },
};
