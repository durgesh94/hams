import { Box, Button, Grid, Typography } from "@mui/material";
import PageHeader from "../../components/common/PageHeader";
import { useGetDashboardDataQuery as useDashboardDataQuery } from "../../features/dashboard/dashboardApi";
import Card from "../../components/common/Card";
import type { DashboardData } from "../../features/dashboard/types";

const Dashboard = () => {
  // Get the current month in YYYY-MM format to fetch dashboard data for this month
  const month = new Date().toISOString().slice(0, 7);

  const {
    data: dashboardData = {} as DashboardData,
    isLoading,
    isError,
    error,
    refetch,
  } = useDashboardDataQuery(month);

  const errorMessage =
    error && "message" in error
      ? error.message
      : error && "error" in error
        ? error.error
        : "Something went wrong";

  return (
    <Box sx={{ flexGrow: 1 }}>
      <PageHeader
        title="Dashboard"
        subtitle="Overview of hospital management system"
        rightLabel={`${new Date().toLocaleString("default", { month: "long" })} ${new Date().getFullYear()}`}
      />

      {isLoading && (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            height: "100vh",
          }}
        >
          Loading...
        </Box>
      )}

      {isError && (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            height: "100vh",
          }}
        >
          <Typography>Error: {errorMessage}</Typography>
          <Button onClick={refetch}>Retry</Button>
        </Box>
      )}
      {dashboardData && (
        <Grid container spacing={3}>
          <Card
            title="Doctors"
            count={dashboardData?.activeDoctorCount ?? 0}
            description="Active Doctors in this month"
          />
          <Card
            title="Patients"
            count={dashboardData?.newPatientCount ?? 0}
            description="Patients newly registered in this month"
          />
          <Card
            title="Appointments"
            count={dashboardData?.appointmentCount ?? 0}
            description="Scheduled Appointments in this month"
          />
        </Grid>
      )}
    </Box>
  );
};

export default Dashboard;
