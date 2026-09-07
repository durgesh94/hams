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
  EmailOutlined,
  LocalPhoneOutlined,
  MedicalServicesOutlined,
  SchoolOutlined,
  EventOutlined,
  PersonOutlined,
} from "@mui/icons-material";

interface ViewDetailsProps {
  doctor: {
    firstName: string;
    lastName: string;
    gender: string;
    specialization: string;
    qualification: string;
    phone: string;
    appointmentCount: number;
    email: string;
    status: string;
  } | null;
}

const formatValue = (value: string) =>
  value
    ? value.charAt(0).toUpperCase() + value.slice(1).toLowerCase()
    : "-";

const ViewDetails = ({ doctor }: ViewDetailsProps) => {
  if (!doctor) {
    return (
      <Box
        sx={{
          py: 5,
          textAlign: "center",
        }}
      >
        <Typography color="text.secondary">
          No details available
        </Typography>
      </Box>
    );
  }

  const fullName = `${doctor.firstName} ${doctor.lastName}`;

  return (
    <Box>
      {/* Doctor Header */}
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
          <Avatar
            sx={{
              width: 72,
              height: 72,
              fontSize: 28,
              fontWeight: 600,
            }}
          >
            {doctor.firstName.charAt(0)}
            {doctor.lastName.charAt(0)}
          </Avatar>

          <Box sx={{ flexGrow: 1 }}>
            <Typography variant="h5">
              Dr. {fullName}
            </Typography>

            <Stack
              direction="row"
              spacing={1}
              sx={{ mt: 0.75, alignItems: "center", flexWrap: "wrap" }}
            >
              <MedicalServicesOutlined
                fontSize="small"
                color="action"
              />

              <Typography
                variant="body1"
                color="text.secondary"
              >
                {doctor.specialization}
              </Typography>
            </Stack>
          </Box>

          <Chip
            label={formatValue(doctor.status)}
            color={
              doctor.status.toLowerCase() === "active"
                ? "success"
                : "default"
            }
            size="small"
          />
        </Stack>
      </Paper>

      {/* Basic Information */}
      <Typography
        variant="subtitle1"
        sx={{ mb: 1.5 }}
      >
        Professional Information
      </Typography>

      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoItem
            icon={<PersonOutlined />}
            label="Gender"
            value={formatValue(doctor.gender)}
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoItem
            icon={<SchoolOutlined />}
            label="Qualification"
            value={doctor.qualification}
          />
        </Grid>
      </Grid>

      <Divider sx={{ mb: 3 }} />

      {/* Contact Information */}
      <Typography
        variant="subtitle1"
        sx={{ mb: 1.5 }}
      >
        Contact Information
      </Typography>

      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoItem
            icon={<LocalPhoneOutlined />}
            label="Phone"
            value={doctor.phone}
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6 }}>
          <InfoItem
            icon={<EmailOutlined />}
            label="Email"
            value={doctor.email}
          />
        </Grid>
      </Grid>

      <Divider sx={{ mb: 3 }} />

      {/* Appointment Statistics */}
      <Typography
        variant="subtitle1"
        sx={{ mb: 1.5 }}
      >
        Appointment Statistics
      </Typography>

      <Paper
        elevation={0}
        sx={{
          p: 2,
          border: 1,
          borderColor: "divider",
          borderRadius: 2,
        }}
      >
        <Stack direction="row" spacing={2} sx={{ alignItems: "center" }}>
          <Avatar>
            <EventOutlined />
          </Avatar>

          <Box>
            <Typography
              variant="h5"
            >
              {doctor.appointmentCount}
            </Typography>

            <Typography
              variant="body2"
              color="text.secondary"
            >
              Total Appointments
            </Typography>
          </Box>
        </Stack>
      </Paper>
    </Box>
  );
};

interface InfoItemProps {
  icon: React.ReactNode;
  label: string;
  value: string;
}

const InfoItem = ({
  icon,
  label,
  value,
}: InfoItemProps) => {
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
        <Typography
          variant="caption"
          color="text.secondary"
        >
          {label}
        </Typography>

        <Typography
          variant="body2"
          sx={{
            overflowWrap: "anywhere",
          }}
        >
          {value || "-"}
        </Typography>
      </Box>
    </Stack>
  );
};

export default ViewDetails;