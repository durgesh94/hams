import { configureStore } from "@reduxjs/toolkit";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { baseApi } from "../api/baseApi";
import { patientApi } from "./patientApi";

const patient = {
  id: 1,
  firstName: "Jane",
  lastName: "Doe",
  dateOfBirth: "1990-05-20",
  gender: "FEMALE" as const,
  email: "jane.doe@test.com",
  phone: "9876543210",
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

describe("patientApi", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it("should fetch and transform the patient list", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: [patient] }));
    const store = createStore();

    const result = await store
      .dispatch(patientApi.endpoints.getPatients.initiate())
      .unwrap();

    expect(result).toEqual([patient]);

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/patients");
    expect(request.method).toBe("GET");
    expect(request.headers.get("Authorization")).toBe("Bearer test-token");
  });

  it("should fetch and transform a patient by id", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: patient }));
    const store = createStore();

    const result = await store
      .dispatch(patientApi.endpoints.getPatientById.initiate(1))
      .unwrap();

    expect(result).toEqual(patient);

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/patients/1");
    expect(request.method).toBe("GET");
  });

  it("should create a patient with a POST request", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: patient }));
    const store = createStore();
    const requestBody = {
      firstName: "Jane",
      lastName: "Doe",
      dateOfBirth: "1990-05-20",
      gender: "FEMALE" as const,
      email: "jane.doe@test.com",
      phone: "9876543210",
    };

    const result = await store
      .dispatch(patientApi.endpoints.createPatient.initiate(requestBody))
      .unwrap();

    expect(result).toEqual(patient);

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/patients");
    expect(request.method).toBe("POST");
    expect(await request.clone().text()).toBe(JSON.stringify(requestBody));
  });

  it("should update a patient without sending its id in the body", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(jsonResponse({ data: patient }));
    const store = createStore();
    const requestBody = {
      id: 1,
      firstName: "Jane",
      lastName: "Updated",
      email: "jane.updated@test.com",
      phone: "9876543211",
    };

    await store
      .dispatch(patientApi.endpoints.updatePatient.initiate(requestBody))
      .unwrap();

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/patients/1");
    expect(request.method).toBe("PUT");
    expect(await request.clone().text()).toBe(
      JSON.stringify({
        firstName: "Jane",
        lastName: "Updated",
        email: "jane.updated@test.com",
        phone: "9876543211",
      }),
    );
  });

  it("should delete a patient with a DELETE request", async () => {
    const fetchMock = vi
      .spyOn(globalThis, "fetch")
      .mockResolvedValue(new Response(null, { status: 204 }));
    const store = createStore();

    await store
      .dispatch(patientApi.endpoints.deletePatient.initiate(1))
      .unwrap();

    const request = getFetchRequest(fetchMock);
    expect(request.url).toContain("/api/v1/patients/1");
    expect(request.method).toBe("DELETE");
  });
});
