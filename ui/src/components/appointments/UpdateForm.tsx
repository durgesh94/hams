import { useEffect } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import {
	Box,
	FormControl,
	FormHelperText,
	InputLabel,
	MenuItem,
	Select,
	TextField,
} from "@mui/material";
import { Controller, useForm } from "react-hook-form";
import { z } from "zod";

import type {
	Appointment,
	AppointmentStatus,
	UpdateAppointmentRequest,
} from "../../features/appointments/types";
import type { Doctor } from "../../features/doctors/types";
import type { Patient } from "../../features/patients/types";

const updateAppointmentSchema = z.object({
	patientId: z.number().min(1, "Please select patient"),
	doctorId: z.number().min(1, "Please select doctor"),
	appointmentDate: z.string().min(1, "Appointment date is required"),
	appointmentTime: z.string().min(1, "Appointment time is required"),
	reason: z
		.string()
		.trim()
		.min(2, "Reason must be at least 2 characters")
		.max(250, "Reason must not exceed 250 characters"),
	notes: z.string().trim().max(500, "Notes must not exceed 500 characters"),
	status: z.enum(["BOOKED", "CONFIRMED", "COMPLETED", "CANCELLED"], {
		message: "Please select status",
	}),
});

type UpdateAppointmentFormValues = z.infer<typeof updateAppointmentSchema>;

interface UpdateFormProps {
	appointment: Appointment | null;
	doctors: Doctor[];
	patients: Patient[];
	onSubmit: (data: UpdateAppointmentRequest) => void;
	isSubmitting?: boolean;
}

const UpdateForm = ({
	appointment,
	doctors,
	patients,
	onSubmit,
	isSubmitting = false,
}: UpdateFormProps) => {
	const {
		control,
		register,
		handleSubmit,
		reset,
		formState: { errors },
	} = useForm<UpdateAppointmentFormValues>({
		resolver: zodResolver(updateAppointmentSchema),
		defaultValues: {
			patientId: 0,
			doctorId: 0,
			appointmentDate: "",
			appointmentTime: "",
			reason: "",
			notes: "",
			status: "BOOKED",
		},
	});

	useEffect(() => {
		if (appointment) {
			reset({
				patientId: appointment.patientId,
				doctorId: appointment.doctorId,
				appointmentDate: appointment.appointmentDate,
				appointmentTime: appointment.appointmentTime,
				reason: appointment.reason,
				notes: appointment.notes ?? "",
				status: appointment.status,
			});
		}
	}, [appointment, reset]);

	const handleFormSubmit = (data: UpdateAppointmentFormValues) => {
		if (appointment?.id) {
			onSubmit({
				id: appointment.id,
				patientId: data.patientId,
				doctorId: data.doctorId,
				appointmentDate: data.appointmentDate,
				appointmentTime: data.appointmentTime,
				reason: data.reason.trim(),
				notes: data.notes.trim(),
				status: data.status as AppointmentStatus,
			});
		}
	};

	if (!appointment) {
		return <Box>No appointment selected</Box>;
	}

	return (
		<Box
			component="form"
			id="update-appointment-form"
			onSubmit={handleSubmit(handleFormSubmit)}
			noValidate
			sx={{ display: "flex", flexDirection: "column", gap: 2, pt: 1 }}
		>
			<Controller
				name="patientId"
				control={control}
				render={({ field }) => (
					<FormControl
						fullWidth
						required
						disabled={isSubmitting}
						error={Boolean(errors.patientId)}
					>
						<InputLabel id="update-appointment-patient-label">Patient</InputLabel>
						<Select
							{...field}
							labelId="update-appointment-patient-label"
							label="Patient"
							onChange={(event) => field.onChange(Number(event.target.value))}
						>
							<MenuItem value={0} disabled>
								Select patient
							</MenuItem>
							{patients.map((patient) => (
								<MenuItem key={patient.id} value={patient.id}>
									{patient.firstName} {patient.lastName}
								</MenuItem>
							))}
						</Select>
						<FormHelperText>{errors.patientId?.message}</FormHelperText>
					</FormControl>
				)}
			/>

			<Controller
				name="doctorId"
				control={control}
				render={({ field }) => (
					<FormControl
						fullWidth
						required
						disabled={isSubmitting}
						error={Boolean(errors.doctorId)}
					>
						<InputLabel id="update-appointment-doctor-label">Doctor</InputLabel>
						<Select
							{...field}
							labelId="update-appointment-doctor-label"
							label="Doctor"
							onChange={(event) => field.onChange(Number(event.target.value))}
						>
							<MenuItem value={0} disabled>
								Select doctor
							</MenuItem>
							{doctors.map((doctor) => (
								<MenuItem key={doctor.id} value={doctor.id}>
									Dr. {doctor.firstName} {doctor.lastName}
								</MenuItem>
							))}
						</Select>
						<FormHelperText>{errors.doctorId?.message}</FormHelperText>
					</FormControl>
				)}
			/>

			<TextField
				label="Appointment Date"
				type="date"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("appointmentDate")}
				error={Boolean(errors.appointmentDate)}
				helperText={errors.appointmentDate?.message}
				slotProps={{ inputLabel: { shrink: true } }}
			/>

			<TextField
				label="Appointment Time"
				type="time"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("appointmentTime")}
				error={Boolean(errors.appointmentTime)}
				helperText={errors.appointmentTime?.message}
				slotProps={{ inputLabel: { shrink: true } }}
			/>

			<TextField
				label="Reason"
				placeholder="Enter reason for appointment"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("reason")}
				error={Boolean(errors.reason)}
				helperText={errors.reason?.message}
			/>

			<TextField
				label="Notes"
				placeholder="Optional notes"
				fullWidth
				multiline
				minRows={3}
				disabled={isSubmitting}
				{...register("notes")}
				error={Boolean(errors.notes)}
				helperText={errors.notes?.message}
			/>

			<Controller
				name="status"
				control={control}
				render={({ field }) => (
					<FormControl
						fullWidth
						required
						disabled={isSubmitting}
						error={Boolean(errors.status)}
					>
						<InputLabel id="update-appointment-status-label">Status</InputLabel>
						<Select
							{...field}
							labelId="update-appointment-status-label"
							label="Status"
						>
							<MenuItem value="BOOKED">Booked</MenuItem>
							<MenuItem value="CONFIRMED">Confirmed</MenuItem>
							<MenuItem value="COMPLETED">Completed</MenuItem>
							<MenuItem value="CANCELLED">Cancelled</MenuItem>
						</Select>
						<FormHelperText>{errors.status?.message}</FormHelperText>
					</FormControl>
				)}
			/>
		</Box>
	);
};

export default UpdateForm;
