import { baseApi } from "../api/baseApi";
import type { ApiResponse } from "../api/types";
import type {
  CreatePatientRequest,
  Patient,
  UpdatePatientRequest,
} from "./types";

export const patientApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getPatients: builder.query<Patient[], void>({
      query: () => "/api/v1/patients",
      transformResponse: (response: ApiResponse<Patient[]>) => response.data,
      providesTags: ["Patient"],
    }),

    getPatientById: builder.query<Patient, number>({
      query: (id) => `/api/v1/patients/${id}`,
      transformResponse: (response: ApiResponse<Patient>) => response.data,
      providesTags: (_result, _error, id) => [{ type: "Patient", id }],
    }),

    createPatient: builder.mutation<Patient, CreatePatientRequest>({
      query: (patient) => ({
        url: "/api/v1/patients",
        method: "POST",
        body: patient,
      }),
      transformResponse: (response: ApiResponse<Patient>) => response.data,
      invalidatesTags: ["Patient"],
    }),

    updatePatient: builder.mutation<Patient, UpdatePatientRequest>({
      query: ({ id, ...patient }) => ({
        url: `/api/v1/patients/${id}`,
        method: "PUT",
        body: patient,
      }),
      transformResponse: (response: ApiResponse<Patient>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [
        "Patient",
        { type: "Patient", id },
      ],
    }),

    deletePatient: builder.mutation<void, number>({
      query: (id) => ({
        url: `/api/v1/patients/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Patient"],
    }),
  }),
});

export const {
  useGetPatientsQuery,
  useGetPatientByIdQuery,
  useCreatePatientMutation,
  useUpdatePatientMutation,
  useDeletePatientMutation,
} = patientApi;
