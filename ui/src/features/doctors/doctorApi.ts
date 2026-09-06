import { baseApi } from "../api/baseApi";
import type { CreateDoctorRequest, Doctor, UpdateDoctorRequest } from "./types";

type ApiResponse<T> = {
  status: number;
  message: string;
  data: T;
};

export const doctorApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getDoctors: builder.query<Doctor[], void>({
      query: () => "/api/v1/doctors",
      transformResponse: (response: ApiResponse<Doctor[]>) => response.data,
      providesTags: ["Doctor"],
    }),

    getDoctorById: builder.query<Doctor, number>({
      query: (id) => `/api/v1/doctors/${id}`,
      transformResponse: (response: ApiResponse<Doctor>) => response.data,
      providesTags: (_result, _error, id) => [{ type: "Doctor", id }],
    }),

    createDoctor: builder.mutation<Doctor, CreateDoctorRequest>({
      query: (doctor) => ({
        url: "/api/v1/doctors",
        method: "POST",
        body: doctor,
      }),
      transformResponse: (response: ApiResponse<Doctor>) => response.data,
      invalidatesTags: ["Doctor"],
    }),

    updateDoctor: builder.mutation<Doctor, UpdateDoctorRequest>({
      query: ({ id, ...doctor }) => ({
        url: `/api/v1/doctors/${id}`,
        method: "PUT",
        body: doctor,
      }),
      transformResponse: (response: ApiResponse<Doctor>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [
        "Doctor",
        { type: "Doctor", id },
      ],
    }),

    deleteDoctor: builder.mutation<void, number>({
      query: (id) => ({
        url: `/api/v1/doctors/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Doctor"],
    }),
  }),
});

export const {
  useGetDoctorsQuery,
  useGetDoctorByIdQuery,
  useCreateDoctorMutation,
  useUpdateDoctorMutation,
  useDeleteDoctorMutation,
} = doctorApi;
