import { beforeEach, describe, expect, it, vi } from "vitest";

import { authStorage } from "./authStorage";

const user = {
  username: "john.doe",
  role: "ADMIN",
};

const tokenWithPayload = (payload: Record<string, unknown>) => {
  const encodedPayload = btoa(JSON.stringify(payload))
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");

  return `header.${encodedPayload}.signature`;
};

describe("authStorage", () => {
  beforeEach(() => {
    localStorage.clear();
    vi.restoreAllMocks();
  });

  it("should store and retrieve the token and user", () => {
    authStorage.setAuth("test-token", user);

    expect(authStorage.getToken()).toBe("test-token");
    expect(authStorage.getUser()).toEqual(user);
  });

  it("should return null when no auth data is stored", () => {
    expect(authStorage.getToken()).toBeNull();
    expect(authStorage.getUser()).toBeNull();
  });

  it("should return null when stored user data is invalid JSON", () => {
    localStorage.setItem("hospital_user", "invalid-json");

    expect(authStorage.getUser()).toBeNull();
  });

  it("should read the expiration time from a JWT payload", () => {
    const token = tokenWithPayload({ exp: 1_800_000_000 });

    expect(authStorage.getTokenExpirationTime(token)).toBe(1_800_000_000_000);
  });

  it("should return null for tokens without a valid expiration", () => {
    expect(authStorage.getTokenExpirationTime("not-a-jwt")).toBeNull();
    expect(authStorage.getTokenExpirationTime(tokenWithPayload({}))).toBeNull();
    expect(
      authStorage.getTokenExpirationTime(tokenWithPayload({ exp: 0 })),
    ).toBeNull();
  });

  it("should identify expired and non-expired tokens", () => {
    vi.spyOn(Date, "now").mockReturnValue(1_000_000);
    const expiredToken = tokenWithPayload({ exp: 999 });
    const validToken = tokenWithPayload({ exp: 1_001 });

    expect(authStorage.isTokenExpired(expiredToken)).toBe(true);
    expect(authStorage.isTokenExpired(validToken)).toBe(false);
  });

  it("should keep tokens without expiration as non-expired", () => {
    expect(authStorage.isTokenExpired("not-a-jwt")).toBe(false);
    expect(authStorage.isTokenExpired(tokenWithPayload({}))).toBe(false);
  });

  it("should clear the stored token and user", () => {
    authStorage.setAuth("test-token", user);

    authStorage.clearAuth();

    expect(authStorage.getToken()).toBeNull();
    expect(authStorage.getUser()).toBeNull();
  });
});
