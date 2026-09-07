import { baseApi } from "../api/baseApi";
import type { ApiResponse } from "../api/types";
import type {
  Appointment,
  AppointmentPage,
  CreateAppointmentRequest,
  UpdateAppointmentRequest,
} from "./types";

export const appointmentApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getAppointments: builder.query<ApiResponse<AppointmentPage>, void>({
      query: () => "/api/v1/appointments/filter",
      providesTags: ["Appointment"],
    }),

    getAppointmentById: builder.query<ApiResponse<Appointment>, number>({
      query: (id) => `/api/v1/appointments/${id}`,
      providesTags: (_result, _error, id) => [{ type: "Appointment", id }],
    }),

    createAppointment: builder.mutation<
      ApiResponse<Appointment>,
      CreateAppointmentRequest
    >({
      query: (appointment) => ({
        url: "/api/v1/appointments",
        method: "POST",
        body: appointment,
      }),
      invalidatesTags: ["Appointment"],
    }),

    updateAppointment: builder.mutation<
      ApiResponse<Appointment>,
      UpdateAppointmentRequest
    >({
      query: ({ id, ...appointment }) => ({
        url: `/api/v1/appointments/${id}`,
        method: "PUT",
        body: appointment,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        "Appointment",
        { type: "Appointment", id },
      ],
    }),

    deleteAppointment: builder.mutation<ApiResponse<null>, number>({
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
