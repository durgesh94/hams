import {
	Avatar,
	Box,
	Divider,
	Grid,
	Paper,
	Stack,
	Typography,
} from "@mui/material";
import {
	EmailOutlined,
	EventOutlined,
	LocalPhoneOutlined,
	PersonOutlined,
} from "@mui/icons-material";
import type { ReactNode } from "react";

import type { Patient } from "../../features/patients/types";

interface ViewDetailsProps {
	patient: Patient | null;
}

const formatValue = (value: string) =>
	value
		? value.charAt(0).toUpperCase() + value.slice(1).toLowerCase()
		: "-";

const ViewDetails = ({ patient }: ViewDetailsProps) => {
	if (!patient) {
		return (
			<Box sx={{ py: 5, textAlign: "center" }}>
				<Typography color="text.secondary">No details available</Typography>
			</Box>
		);
	}

	const fullName = `${patient.firstName} ${patient.lastName}`;

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
					<Avatar sx={{ width: 72, height: 72, fontSize: 28, fontWeight: 600 }}>
						{patient.firstName.charAt(0)}
						{patient.lastName.charAt(0)}
					</Avatar>

					<Box sx={{ flexGrow: 1 }}>
						<Typography variant="h5">{fullName}</Typography>
						<Typography variant="body1" color="text.secondary" sx={{ mt: 0.75 }}>
							Patient ID: {patient.id}
						</Typography>
					</Box>
				</Stack>
			</Paper>

			<Typography variant="subtitle1" sx={{ mb: 1.5 }}>
				Basic Information
			</Typography>

			<Grid container spacing={2} sx={{ mb: 3 }}>
				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<PersonOutlined />}
						label="Gender"
						value={formatValue(patient.gender)}
					/>
				</Grid>

				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<EventOutlined />}
						label="Date of Birth"
						value={patient.dateOfBirth}
					/>
				</Grid>
			</Grid>

			<Divider sx={{ mb: 3 }} />

			<Typography variant="subtitle1" sx={{ mb: 1.5 }}>
				Contact Information
			</Typography>

			<Grid container spacing={2}>
				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<LocalPhoneOutlined />}
						label="Phone"
						value={patient.phone}
					/>
				</Grid>

				<Grid size={{ xs: 12, sm: 6 }}>
					<InfoItem
						icon={<EmailOutlined />}
						label="Email"
						value={patient.email}
					/>
				</Grid>
			</Grid>
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
