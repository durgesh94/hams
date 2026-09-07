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

import { Gender } from "../../constants/genderEnum";
import type {
	Patient,
	UpdatePatientRequest,
} from "../../features/patients/types";

const updatePatientSchema = z.object({
	firstName: z
		.string()
		.trim()
		.min(2, "First name must be at least 2 characters")
		.max(50, "First name must not exceed 50 characters"),
	lastName: z
		.string()
		.trim()
		.min(2, "Last name must be at least 2 characters")
		.max(50, "Last name must not exceed 50 characters"),
	dateOfBirth: z.string().min(1, "Date of birth is required"),
	gender: z.enum([Gender.MALE, Gender.FEMALE, Gender.OTHER], {
		message: "Please select gender",
	}),
	email: z
		.string()
		.trim()
		.email("Enter a valid email address")
		.max(150, "Email must not exceed 150 characters"),
	phone: z
		.string()
		.trim()
		.regex(/^[6-9]\d{9}$/, "Enter a valid 10-digit mobile number"),
});

type UpdatePatientFormValues = z.infer<typeof updatePatientSchema>;

interface UpdateFormProps {
	patient: Patient | null;
	onSubmit: (data: UpdatePatientRequest) => void;
	isSubmitting?: boolean;
}

const UpdateForm = ({
	patient,
	onSubmit,
	isSubmitting = false,
}: UpdateFormProps) => {
	const {
		control,
		register,
		handleSubmit,
		reset,
		formState: { errors },
	} = useForm<UpdatePatientFormValues>({
		resolver: zodResolver(updatePatientSchema),
		defaultValues: {
			firstName: "",
			lastName: "",
			dateOfBirth: "",
			gender: Gender.MALE,
			email: "",
			phone: "",
		},
	});

	useEffect(() => {
		if (patient) {
			reset({
				firstName: patient.firstName,
				lastName: patient.lastName,
				dateOfBirth: patient.dateOfBirth,
				gender: patient.gender,
				email: patient.email,
				phone: patient.phone,
			});
		}
	}, [patient, reset]);

	const handleFormSubmit = (data: UpdatePatientFormValues) => {
		if (patient?.id) {
			onSubmit({
				id: patient.id,
				firstName: data.firstName.trim(),
				lastName: data.lastName.trim(),
				dateOfBirth: data.dateOfBirth,
				gender: data.gender,
				email: data.email.trim(),
				phone: data.phone.trim(),
			});
		}
	};

	if (!patient) {
		return <Box>No patient selected</Box>;
	}

	return (
		<Box
			component="form"
			id="update-patient-form"
			onSubmit={handleSubmit(handleFormSubmit)}
			noValidate
			sx={{ display: "flex", flexDirection: "column", gap: 2, pt: 1 }}
		>
			<TextField
				label="First Name"
				placeholder="Enter first name"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("firstName")}
				error={Boolean(errors.firstName)}
				helperText={errors.firstName?.message}
			/>

			<TextField
				label="Last Name"
				placeholder="Enter last name"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("lastName")}
				error={Boolean(errors.lastName)}
				helperText={errors.lastName?.message}
			/>

			<TextField
				label="Date of Birth"
				type="date"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("dateOfBirth")}
				error={Boolean(errors.dateOfBirth)}
				helperText={errors.dateOfBirth?.message}
				slotProps={{
					inputLabel: { shrink: true },
					htmlInput: { max: new Date().toISOString().slice(0, 10) },
				}}
			/>

			<Controller
				name="gender"
				control={control}
				render={({ field }) => (
					<FormControl
						fullWidth
						required
						disabled={isSubmitting}
						error={Boolean(errors.gender)}
					>
						<InputLabel id="update-patient-gender-label">Gender</InputLabel>
						<Select
							{...field}
							labelId="update-patient-gender-label"
							label="Gender"
						>
							<MenuItem value={Gender.MALE}>Male</MenuItem>
							<MenuItem value={Gender.FEMALE}>Female</MenuItem>
							<MenuItem value={Gender.OTHER}>Other</MenuItem>
						</Select>
						<FormHelperText>{errors.gender?.message}</FormHelperText>
					</FormControl>
				)}
			/>

			<TextField
				label="Email"
				placeholder="patient@example.com"
				type="email"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("email")}
				error={Boolean(errors.email)}
				helperText={errors.email?.message}
			/>

			<TextField
				label="Phone"
				placeholder="Enter 10-digit mobile number"
				fullWidth
				required
				disabled={isSubmitting}
				{...register("phone")}
				error={Boolean(errors.phone)}
				helperText={errors.phone?.message ?? "Enter a valid 10-digit mobile number"}
				slotProps={{
					htmlInput: { maxLength: 10, inputMode: "numeric" },
				}}
			/>
		</Box>
	);
};

export default UpdateForm;
