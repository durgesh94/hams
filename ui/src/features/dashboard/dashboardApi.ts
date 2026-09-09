import { baseApi } from "../api/baseApi";
import type { ApiResponse } from "../api/types";
import type { DashboardData } from "./types";

export const dashboardApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getDashboardData: builder.query<DashboardData, string>({
      query: (month) => `/api/v1/dashboard/month?month=${month}`,
      transformResponse: (response: ApiResponse<DashboardData>) =>
        response.data,
      providesTags: ["Dashboard"],
    }),
  }),
});

export const { useGetDashboardDataQuery } = dashboardApi;
