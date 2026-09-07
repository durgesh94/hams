import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

import { authStorage } from "./authStorage";
import type { User } from "./types";

interface AuthState {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;
}

const storedToken = authStorage.getToken();
const storedUser = authStorage.getUser();
const hasValidStoredToken = storedToken
  ? !authStorage.isTokenExpired(storedToken)
  : false;

if (storedToken && !hasValidStoredToken) {
  authStorage.clearAuth();
}

const initialState: AuthState = {
  token: hasValidStoredToken ? storedToken : null,
  user: hasValidStoredToken ? storedUser : null,
  isAuthenticated: hasValidStoredToken,
};

const authSlice = createSlice({
  name: "auth",

  initialState,

  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{
        token: string;
        user: User;
      }>,
    ) => {
      state.token = action.payload.token;
      state.user = action.payload.user;
      state.isAuthenticated = true;

      authStorage.setAuth(action.payload.token, action.payload.user);
    },

    logout: (state) => {
      state.token = null;
      state.user = null;
      state.isAuthenticated = false;

      authStorage.clearAuth();
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;

export default authSlice.reducer;
