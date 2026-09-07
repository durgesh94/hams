import type { Gender } from "../../constants/genderEnum";

export interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  gender: Gender;
  email: string;
  phone: string;
}

export interface CreatePatientRequest {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  gender: Gender;
  email: string;
  phone: string;
}

export interface UpdatePatientRequest {
  id: number;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string;
  gender?: Gender;
  email?: string;
  phone?: string;
}
