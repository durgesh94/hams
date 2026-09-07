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
import { useForm } from "react-hook-form";
import { z } from "zod";

import type {
  UpdateDoctorRequest,
  Doctor,
} from "../../features/doctors/types";
import { Gender } from "../../constants/genderEnum";

const updateDoctorSchema = z.object({
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

  gender: z.enum([Gender.MALE, Gender.FEMALE, Gender.OTHER], {
    message: "Please select gender",
  }),

  specialization: z
    .string()
    .trim()
    .min(2, "Specialization is required")
    .max(100, "Specialization must not exceed 100 characters"),

  qualification: z
    .string()
    .trim()
    .min(2, "Qualification is required")
    .max(150, "Qualification must not exceed 150 characters"),

  experienceYears: z
    .number({
      message: "Experience is required",
    })
    .int("Experience must be a whole number")
    .min(0, "Experience cannot be negative")
    .max(60, "Experience cannot exceed 60 years"),

  email: z
    .string()
    .trim()
    .email("Enter a valid email address")
    .max(150, "Email must not exceed 150 characters"),

  phone: z
    .string()
    .trim()
    .regex(/^[6-9]\d{9}$/, "Enter a valid 10-digit mobile number"),

  status: z.enum(["ACTIVE", "INACTIVE"], {
    message: "Please select status",
  }),
});

type UpdateDoctorFormValues = z.infer<typeof updateDoctorSchema>;

interface UpdateFormProps {
  doctor: Doctor | null;
  onSubmit: (data: UpdateDoctorRequest) => void;
  isSubmitting?: boolean;
}

const UpdateForm = ({
  doctor,
  onSubmit,
  isSubmitting = false,
}: UpdateFormProps) => {
  const {
    register,
    handleSubmit,
    reset,
    setValue,
    watch,
    formState: { errors },
  } = useForm<UpdateDoctorFormValues>({
    resolver: zodResolver(updateDoctorSchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      gender: Gender.MALE,
      specialization: "",
      qualification: "",
      experienceYears: 0,
      email: "",
      phone: "",
      status: "ACTIVE",
    },
  });

  const gender = watch("gender");
  const status = watch("status");

  useEffect(() => {
    if (doctor) {
      reset({
        firstName: doctor.firstName,
        lastName: doctor.lastName,
        gender: doctor.gender as Gender,
        specialization: doctor.specialization,
        qualification: doctor.qualification,
        experienceYears: doctor.experienceYears,
        email: doctor.email,
        phone: doctor.phone,
        status: (doctor.status === "ACTIVE" || doctor.status === "INACTIVE")
          ? doctor.status
          : "ACTIVE",
      });
    }
  }, [doctor, reset]);

  const handleFormSubmit = (data: UpdateDoctorFormValues) => {
    if (doctor?.id) {
      onSubmit({
        id: doctor.id,
        firstName: data.firstName.trim(),
        lastName: data.lastName.trim(),
          gender: data.gender as Gender,
        specialization: data.specialization.trim(),
        qualification: data.qualification.trim(),
        experienceYears: data.experienceYears,
        email: data.email.trim(),
        phone: data.phone.trim(),
        status: data.status as "ACTIVE" | "INACTIVE",
      });
    }
  };

  if (!doctor) {
    return <Box>No doctor selected</Box>;
  }

  return (
    <Box
      component="form"
      id="update-doctor-form"
      onSubmit={handleSubmit(handleFormSubmit)}
      noValidate
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: 2,
        pt: 1,
      }}
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

      <FormControl
        fullWidth
        required
        disabled={isSubmitting}
        error={Boolean(errors.gender)}
      >
        <InputLabel id="doctor-gender-label">Gender</InputLabel>

        <Select
          labelId="doctor-gender-label"
          value={gender}
          label="Gender"
          onChange={(event) => {
            setValue("gender", event.target.value as Gender, {
              shouldValidate: true,
            });
          }}
        >
          <MenuItem value={Gender.MALE}>Male</MenuItem>
          <MenuItem value={Gender.FEMALE}>Female</MenuItem>
          <MenuItem value={Gender.OTHER}>Other</MenuItem>
        </Select>

        <FormHelperText>{errors.gender?.message}</FormHelperText>
      </FormControl>

      <TextField
        label="Specialization"
        placeholder="e.g. Gynecology"
        fullWidth
        required
        disabled={isSubmitting}
        {...register("specialization")}
        error={Boolean(errors.specialization)}
        helperText={errors.specialization?.message}
      />

      <TextField
        label="Qualification"
        placeholder="e.g. MBBS, MD Gynecology"
        fullWidth
        required
        disabled={isSubmitting}
        {...register("qualification")}
        error={Boolean(errors.qualification)}
        helperText={errors.qualification?.message}
      />

      <TextField
        label="Experience (Years)"
        type="number"
        fullWidth
        required
        disabled={isSubmitting}
        {...register("experienceYears", {
          valueAsNumber: true,
        })}
        error={Boolean(errors.experienceYears)}
        helperText={errors.experienceYears?.message}
        slotProps={{
          htmlInput: {
            min: 0,
            max: 60,
          },
        }}
      />

      <TextField
        label="Email"
        placeholder="doctor@example.com"
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
        helperText={
          errors.phone?.message ?? "Enter a valid 10-digit mobile number"
        }
        slotProps={{
          htmlInput: {
            maxLength: 10,
            inputMode: "numeric",
          },
        }}
      />

      <FormControl
        fullWidth
        required
        disabled={isSubmitting}
        error={Boolean(errors.status)}
      >
        <InputLabel id="doctor-status-label">Status</InputLabel>

        <Select
          labelId="doctor-status-label"
          value={status}
          label="Status"
          onChange={(event) => {
            setValue("status", event.target.value as "ACTIVE" | "INACTIVE", {
              shouldValidate: true,
            });
          }}
        >
          <MenuItem value="ACTIVE">Active</MenuItem>
          <MenuItem value="INACTIVE">Inactive</MenuItem>
        </Select>

        <FormHelperText>{errors.status?.message}</FormHelperText>
      </FormControl>
    </Box>
  );
};

export default UpdateForm;
