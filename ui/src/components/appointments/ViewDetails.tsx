import {
	Avatar,
	Box,
	Chip,
	Divider,
	Grid,
	Paper,
	Stack,
	Typography,
} from "@mui/material";
import {
	DescriptionOutlined,
	EventOutlined,
	LocalHospitalOutlined,
	NotesOutlined,
	PersonOutlined,
	ScheduleOutlined,
} from "@mui/icons-material";
import type { ReactNode } from "react";

import type {
	Appointment,
	AppointmentStatus,
} from "../../features/appointments/types";

interface ViewDetailsProps {
	appointment: Appointment | null;
}

const getStatusColor = (
	status: AppointmentStatus,
): "success" | "warning" | "error" | "primary" | "default" => {
	switch (status) {
		case "CONFIRMED":
			return "success";
		case "BOOKED":
			return "primary";
		case "CANCELLED":
			return "error";
		case "COMPLETED":
			return "default";
		default:
			return "default";
	}
};

const formatDate = (date: string): string => {
	const parsedDate = new Date(`${date}T00:00:00`);

	if (Number.isNaN(parsedDate.getTime())) {
		return date;
	}

	return parsedDate.toLocaleDateString("en-IN", {
		day: "2-digit",
		month: "short",
		year: "numeric",
	});
};

const formatTime = (time: string): string => {
	const parsedTime = new Date(`1970-01-01T${time}`);

	if (Number.isNaN(parsedTime.getTime())) {
		return time;
	}

	return parsedTime.toLocaleTimeString("en-IN", {
		hour: "2-digit",
		minute: "2-digit",
		hour12: true,
	});
};

const ViewDetails = ({ appointment }: ViewDetailsProps) => {
	if (!appointment) {
		return (
			<Box sx={{ py: 5, textAlign: "center" }}>
				<Typography color="text.secondary">No details available</Typography>
			</Box>
		);
	}

	return (
		<Box>
			<Paper
				elevation={0}
				sx={{
					p: 3,
					mb: 3,
					border: 1,
					borderColor: "divider",
					borderRadius: 2,
				}}
			>
				<Stack
					direction={{ xs: "column", sm: "row" }}
					spacing={2}
					sx={{ alignItems: { xs: "flex-start", sm: "center" } }}
				>
					<Avatar sx={{ width: 72, height: 72 }}>
						<EventOutlined fontSize="large" />
					</Avatar>

					<Box sx={{ flexGrow: 1 }}>
						<Typography variant="h5">Appointment #{appointment.id}</Typography>
						<Typography variant="body1" color="text.secondary" sx={{ mt: 0.75 }}>
							{formatDate(appointment.appointmentDate)} at {formatTime(appointment.appointmentTime)}
						</Typography>
					</Box>

					<Chip
						label={appointment.status}
						color={getStatusColor(appointment.status)}
						size="small"
					/>
				</Stack>
			</Paper>

			<Typography variant="subtitle1" sx={{ mb: 1.5 }}>
				Appointment Information
			</Typography>

			<Grid container spacing={2} sx={{ mb: 3 }}>
				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<PersonOutlined />}
						label="Patient"
						value={appointment.patientName}
					/>
				</Grid>

				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<LocalHospitalOutlined />}
						label="Doctor"
						value={appointment.doctorName}
					/>
				</Grid>

				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<EventOutlined />}
						label="Date"
						value={formatDate(appointment.appointmentDate)}
					/>
				</Grid>

				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<ScheduleOutlined />}
						label="Time"
						value={formatTime(appointment.appointmentTime)}
					/>
				</Grid>
			</Grid>

			<Divider sx={{ mb: 3 }} />

			<Typography variant="subtitle1" sx={{ mb: 1.5 }}>
				Visit Details
			</Typography>

			<Stack spacing={2}>
				<InfoItem
					icon={<DescriptionOutlined />}
					label="Reason"
					value={appointment.reason}
				/>
				<InfoItem
					icon={<NotesOutlined />}
					label="Notes"
					value={appointment.notes}
				/>
			</Stack>
		</Box>
	);
};

interface InfoItemProps {
	icon: ReactNode;
	label: string;
	value: string;
}

const InfoItem = ({ icon, label, value }: InfoItemProps) => {
	return (
		<Stack direction="row" spacing={1.5} sx={{ alignItems: "center" }}>
			<Box
				sx={{
					display: "flex",
					alignItems: "center",
					justifyContent: "center",
					width: 40,
					height: 40,
					borderRadius: 1.5,
					bgcolor: "action.hover",
					color: "text.secondary",
					flexShrink: 0,
				}}
			>
				{icon}
			</Box>

			<Box sx={{ minWidth: 0 }}>
				<Typography variant="caption" color="text.secondary">
					{label}
				</Typography>
				<Typography variant="body2" sx={{ overflowWrap: "anywhere" }}>
					{value || "-"}
				</Typography>
			</Box>
		</Stack>
	);
};

export default ViewDetails;
