import { configureStore } from "@reduxjs/toolkit";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { baseApi } from "../api/baseApi";
import { appointmentApi } from "./appointmentApi";

const appointment = {
  id: 1,
  patientId: 10,
  patientName: "Jane Doe",
  doctorId: 20,
  doctorName: "John Smith",
  appointmentDate: "2026-10-01",
  appointmentTime: "10:30:00",
  reason: "Routine check-up",
  notes: "Bring previous reports",
  status: "BOOKED" as const,
  createdAt: "2026-09-01T10:00:00",
  updatedAt: "2026-09-01T10:00:00",
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

const getFetchRequest = (fetchMock: ReturnType<typeof vi.spyOn>) =>
  fetchMock.mock.calls[0]?.[0] as Request;

describe("appointmentApi", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("should fetch and transform the appointment list", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: [appointment] }));
    const store = createStore();

    const result = await store
      .dispatch(appointmentApi.endpoints.getAppointments.initiate())
      .unwrap();

    expect(result).toEqual([appointment]);
    const request = getFetchRequest(fetchMock);

    expect(request.url).toContain("/api/v1/appointments/filter");
    expect(request.method).toBe("GET");
    expect(request.headers.get("Authorization")).toBe("Bearer test-token");
  });

  it("should fetch and transform an appointment by id", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: appointment }));
    const store = createStore();

    const result = await store
      .dispatch(appointmentApi.endpoints.getAppointmentById.initiate(1))
      .unwrap();

    expect(result).toEqual(appointment);
    const request = getFetchRequest(fetchMock);

    expect(request.url).toContain("/api/v1/appointments/1");
    expect(request.method).toBe("GET");
  });

  it("should create an appointment with a POST request", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: appointment }));
    const store = createStore();
    const request = {
      patientId: 10,
      doctorId: 20,
      appointmentDate: "2026-10-01",
      appointmentTime: "10:30:00",
      reason: "Routine check-up",
      notes: "Bring previous reports",
    };

    const result = await store
      .dispatch(appointmentApi.endpoints.createAppointment.initiate(request))
      .unwrap();

    expect(result).toEqual(appointment);
    const fetchRequest = getFetchRequest(fetchMock);

    expect(fetchRequest.url).toContain("/api/v1/appointments");
    expect(fetchRequest.method).toBe("POST");
    expect(await fetchRequest.clone().text()).toBe(JSON.stringify(request));
  });

  it("should update an appointment without sending its id in the body", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: appointment }));
    const store = createStore();
    const request = {
      id: 1,
      patientId: 10,
      doctorId: 20,
      appointmentDate: "2026-10-01",
      appointmentTime: "11:00:00",
      reason: "Updated reason",
      notes: "Updated notes",
      status: "CONFIRMED" as const,
    };

    await store
      .dispatch(appointmentApi.endpoints.updateAppointment.initiate(request))
      .unwrap();

    const fetchRequest = getFetchRequest(fetchMock);

    expect(fetchRequest.url).toContain("/api/v1/appointments/1");
    expect(fetchRequest.method).toBe("PUT");
    expect(await fetchRequest.clone().text()).toBe(
      JSON.stringify({
        patientId: 10,
        doctorId: 20,
        appointmentDate: "2026-10-01",
        appointmentTime: "11:00:00",
        reason: "Updated reason",
        notes: "Updated notes",
        status: "CONFIRMED",
      }),
    );
  });

  it("should delete an appointment with a DELETE request", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(new Response(null, { status: 204 }));
    const store = createStore();

    await store
      .dispatch(appointmentApi.endpoints.deleteAppointment.initiate(1))
      .unwrap();

    const request = getFetchRequest(fetchMock);

    expect(request.url).toContain("/api/v1/appointments/1");
    expect(request.method).toBe("DELETE");
  });
});
