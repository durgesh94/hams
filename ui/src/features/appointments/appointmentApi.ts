import { baseApi } from "../api/baseApi";
import type { ApiResponse } from "../api/types";
import type {
  Appointment,
  CreateAppointmentRequest,
  UpdateAppointmentRequest,
} from "./types";

export const appointmentApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getAppointments: builder.query<Appointment[], void>({
      query: () => "/api/v1/appointments",
      transformResponse: (response: ApiResponse<Appointment[]>) =>
        response.data,
      providesTags: ["Appointment"],
    }),

    getAppointmentById: builder.query<Appointment, number>({
      query: (id) => `/api/v1/appointments/${id}`,
      transformResponse: (response: ApiResponse<Appointment>) => response.data,
      providesTags: (_result, _error, id) => [{ type: "Appointment", id }],
    }),

    createAppointment: builder.mutation<Appointment, CreateAppointmentRequest>({
      query: (appointment) => ({
        url: "/api/v1/appointments",
        method: "POST",
        body: appointment,
      }),
      transformResponse: (response: ApiResponse<Appointment>) => response.data,
      invalidatesTags: ["Appointment"],
    }),

    updateAppointment: builder.mutation<Appointment, UpdateAppointmentRequest>({
      query: ({ id, ...appointment }) => ({
        url: `/api/v1/appointments/${id}`,
        method: "PUT",
        body: appointment,
      }),
      transformResponse: (response: ApiResponse<Appointment>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [
        "Appointment",
        { type: "Appointment", id },
      ],
    }),

    deleteAppointment: builder.mutation<VoidFunction, number>({
      query: (id) => ({
        url: `/api/v1/appointments/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: ["Appointment"],
    }),
  }),
});

export const {
  useGetAppointmentsQuery,
  useGetAppointmentByIdQuery,
  useCreateAppointmentMutation,
  useUpdateAppointmentMutation,
  useDeleteAppointmentMutation,
} = appointmentApi;
