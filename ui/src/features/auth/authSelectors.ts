import type { RootState } from "../../app/store";

export const selectAuth = (state: RootState) => state.auth;

export const selectToken = (state: RootState) => state.auth.token;

export const selectUser = (state: RootState) => state.auth.user;

export const selectIsAuthenticated = (state: RootState) =>
  state.auth.isAuthenticated;

export const selectIsAdmin = (state: RootState) => state.auth.role === "ADMIN";

export const selectIsOperator = (state: RootState) =>
  state.auth.role === "OPERATOR";

export const selectRole = (state: RootState) => state.auth.role;
