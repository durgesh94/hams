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

import type { CreateAppointmentRequest } from "../../features/appointments/types";
import type { Doctor } from "../../features/doctors/types";
import type { Patient } from "../../features/patients/types";

const appointmentSchema = z.object({
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
});

type AppointmentFormValues = z.infer<typeof appointmentSchema>;

interface AddFormProps {
	doctors: Doctor[];
	patients: Patient[];
	onSubmit: (data: CreateAppointmentRequest) => void;
	isSubmitting?: boolean;
}

const AddForm = ({
	doctors,
	patients,
	onSubmit,
	isSubmitting = false,
}: AddFormProps) => {
	const {
		control,
		register,
		handleSubmit,
		formState: { errors },
	} = useForm<AppointmentFormValues>({
		resolver: zodResolver(appointmentSchema),
		defaultValues: {
			patientId: 0,
			doctorId: 0,
			appointmentDate: "",
			appointmentTime: "",
			reason: "",
			notes: "",
		},
	});

	const handleFormSubmit = (data: AppointmentFormValues) => {
		onSubmit({
			patientId: data.patientId,
			doctorId: data.doctorId,
			appointmentDate: data.appointmentDate,
			appointmentTime: data.appointmentTime,
			reason: data.reason.trim(),
			notes: data.notes.trim(),
		});
	};

	return (
		<Box
			component="form"
			id="add-appointment-form"
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
						<InputLabel id="appointment-patient-label">Patient</InputLabel>
						<Select
							{...field}
							labelId="appointment-patient-label"
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
						<InputLabel id="appointment-doctor-label">Doctor</InputLabel>
						<Select
							{...field}
							labelId="appointment-doctor-label"
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
		</Box>
	);
};

export default AddForm;
