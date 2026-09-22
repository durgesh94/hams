import { configureStore } from "@reduxjs/toolkit";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { baseApi } from "../api/baseApi";
import { doctorApi } from "./doctorApi";

const doctor = {
  id: 1,
  firstName: "John",
  lastName: "Smith",
  specialization: "Cardiology",
  qualification: "MBBS, MD",
  experienceYears: 10,
  gender: "MALE",
  phone: "9876543210",
  email: "john.smith@test.com",
  status: "ACTIVE" as const,
  appointmentCount: 4,
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

describe("doctorApi", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("should fetch and transform the doctor list", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: [doctor] }));
    const store = createStore();

    const result = await store
      .dispatch(doctorApi.endpoints.getDoctors.initiate())
      .unwrap();

    expect(result).toEqual([doctor]);

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/doctors");
    expect(request.method).toBe("GET");
    expect(request.headers.get("Authorization")).toBe("Bearer test-token");
  });

  it("should fetch and transform a doctor by id", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: doctor }));
    const store = createStore();

    const result = await store
      .dispatch(doctorApi.endpoints.getDoctorById.initiate(1))
      .unwrap();

    expect(result).toEqual(doctor);

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/doctors/1");
    expect(request.method).toBe("GET");
  });

  it("should create a doctor with a POST request", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: doctor }));
    const store = createStore();
    const requestBody = {
      firstName: "John",
      lastName: "Smith",
      specialization: "Cardiology",
      qualification: "MBBS, MD",
      experienceYears: 10,
      gender: "MALE",
      phone: "9876543210",
      email: "john.smith@test.com",
      status: "ACTIVE" as const,
    };

    const result = await store
      .dispatch(doctorApi.endpoints.createDoctor.initiate(requestBody))
      .unwrap();

    expect(result).toEqual(doctor);

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/doctors");
    expect(request.method).toBe("POST");
    expect(await request.clone().text()).toBe(JSON.stringify(requestBody));
  });

  it("should update a doctor without sending its id in the body", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: doctor }));
    const store = createStore();
    const requestBody = {
      id: 1,
      firstName: "John",
      lastName: "Updated",
      specialization: "Neurology",
      qualification: "MBBS, DM",
      experienceYears: 11,
      gender: "MALE",
      phone: "9876543210",
      email: "john.updated@test.com",
      status: "ACTIVE" as const,
    };

    await store
      .dispatch(doctorApi.endpoints.updateDoctor.initiate(requestBody))
      .unwrap();

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/doctors/1");
    expect(request.method).toBe("PUT");
    expect(await request.clone().text()).toBe(
      JSON.stringify({
        firstName: "John",
        lastName: "Updated",
        specialization: "Neurology",
        qualification: "MBBS, DM",
        experienceYears: 11,
        gender: "MALE",
        phone: "9876543210",
        email: "john.updated@test.com",
        status: "ACTIVE",
      }),
    );
  });

  it("should delete a doctor with a DELETE request", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(new Response(null, { status: 204 }));
    const store = createStore();

    await store.dispatch(doctorApi.endpoints.deleteDoctor.initiate(1)).unwrap();

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/doctors/1");
    expect(request.method).toBe("DELETE");
  });
});
