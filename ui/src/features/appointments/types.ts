export type AppointmentStatus = "BOOKED" | "CONFIRMED" | "COMPLETED" | "CANCELLED";
export interface Appointment {
  id: number;
  patientId: number;
  patientName: string;
  doctorId: number;
  doctorName: string;
  appointmentDate: string;
  appointmentTime: string;
  reason: string;
  notes: string;
  status: AppointmentStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CreateAppointmentRequest {
  patientId: number;
  doctorId: number;
  appointmentDate: string;
  appointmentTime: string;
  reason: string;
  notes: string;
}

export interface UpdateAppointmentRequest {
  id: number;
  patientId: number;
  doctorId: number;
  appointmentDate: string;
  appointmentTime: string;
  reason: string;
  notes: string;
  status: AppointmentStatus;
}