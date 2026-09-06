export const DoctorStatus = {
  ACTIVE: "ACTIVE",
  INACTIVE: "INACTIVE",
  ON_LEAVE: "ON_LEAVE",
} as const;

export type DoctorStatus = (typeof DoctorStatus)[keyof typeof DoctorStatus];

export interface Doctor {
  id: number;
  firstName: string;
  lastName: string;
  specialization: string;
  qualification: string;
  experienceYears: number;
  gender: string;
  phone: string;
  email: string;
  status: DoctorStatus;
  appointmentCount: number;
}

export interface CreateDoctorRequest {
  firstName: string;
  lastName: string;
  specialization: string;
  qualification: string;
  experienceYears: number;
  gender: string;
  phone: string;
  email: string;
}

export interface UpdateDoctorRequest {
  id: number;
  firstName: string;
  lastName: string;
  specialization: string;
  qualification: string;
  experienceYears: number;
  gender: string;
  phone: string;
  email: string;
}
