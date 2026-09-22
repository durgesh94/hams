import { configureStore } from "@reduxjs/toolkit";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { baseApi } from "../api/baseApi";
import { dashboardApi } from "./dashboardApi";

const dashboardData = {
  month: "2026-09",
  activeDoctorCount: 12,
  newPatientCount: 34,
  appointmentCount: 56,
};

const createStore = () =>
  configureStore({
    reducer: {
      [baseApi.reducerPath]: baseApi.reducer,
      auth: () => ({
        token: "test-token",
        user: null,
        role: null,
        isAuthenticated: true,
      }),
    },
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware().concat(baseApi.middleware),
  });

const jsonResponse = (body: unknown) =>
  new Response(JSON.stringify(body), {
    status: 200,
    headers: { "Content-Type": "application/json" },
  });

describe("dashboardApi", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("should fetch and transform dashboard data for a month", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: dashboardData }));
    const store = createStore();

    const result = await store
      .dispatch(dashboardApi.endpoints.getDashboardData.initiate("2026-09"))
      .unwrap();

    expect(result).toEqual(dashboardData);

    const request = fetchMock.mock.calls[0]?.[0] as Request;
    expect(request.url).toContain("/api/v1/dashboard/month?month=2026-09");
    expect(request.method).toBe("GET");
    expect(request.headers.get("Authorization")).toBe("Bearer test-token");
  });

  it("should preserve zero dashboard counts", async () => {
    vi.spyOn(globalThis, "fetch").mockResolvedValue(
      jsonResponse({
        data: {
          month: "2026-02",
          activeDoctorCount: 0,
          newPatientCount: 0,
          appointmentCount: 0,
        },
      }),
    );
    const store = createStore();

    const result = await store
      .dispatch(dashboardApi.endpoints.getDashboardData.initiate("2026-02"))
      .unwrap();

    expect(result.activeDoctorCount).toBe(0);
    expect(result.newPatientCount).toBe(0);
    expect(result.appointmentCount).toBe(0);
  });
});
