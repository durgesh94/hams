import { baseApi } from "../api/baseApi";

import type { LoginRequest, LoginResponse, User } from "./types";

export const authApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (credentials) => ({
        url: "/api/v1/auth/login",
        method: "POST",
        body: credentials,
      }),
    }),

    getCurrentUser: builder.query<User, void>({
      query: () => "/api/v1/auth/me",
      providesTags: ["Auth"],
    }),
  }),
});

export const { useLoginMutation, useGetCurrentUserQuery } = authApi;
