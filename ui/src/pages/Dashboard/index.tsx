import { Box, Button, Grid, Typography } from "@mui/material";
import { useState } from "react";
import { Select, MenuItem } from "@mui/material";
import PageHeader from "../../components/common/PageHeader";
import { useGetDashboardDataQuery as useDashboardDataQuery } from "../../features/dashboard/dashboardApi";
import Card from "../../components/common/Card";
import type { DashboardData } from "../../features/dashboard/types";

const Dashboard = () => {
  // Get the current month in YYYY-MM format to fetch dashboard data for this month
  const month = new Date().toISOString().slice(0, 7);
  const [selectedMonth, setSelectedMonth] = useState(month);
  console.log(selectedMonth, month);

  const {
    data: dashboardData = {} as DashboardData,
    isLoading,
    isError,
    error,
    refetch,
  } = useDashboardDataQuery(selectedMonth);

  const errorMessage =
    error && "message" in error
      ? error.message
      : error && "error" in error
        ? error.error
        : "Something went wrong";


  // dropdown for month selection
  const handleMonthChange = (event: React.ChangeEvent<{ value: string }>) => {
    setSelectedMonth(event.target.value as string);
  };

  const monthOptions = Array.from({ length: 12 }, (_, i) => {
    const date = new Date();
    date.setMonth(i);
    return {
      value: date.toISOString().slice(0, 7),
      label: date.toLocaleString("default", { month: "long" }),
    };
  });

  const monthDropdown = (
    <Select value={selectedMonth} onChange={handleMonthChange}>
      {monthOptions.map((option) => (
        <MenuItem key={option.value} value={option.value}>
          {option.label} {new Date(option.value).getFullYear()}
        </MenuItem>
      ))}
    </Select>
  );

  return (
    <Box sx={{ flexGrow: 1 }}>
      <PageHeader
        title="Dashboard"
        subtitle="Overview of hospital management system"
        rightContent={monthDropdown}
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
      {dashboardData && !isLoading && !isError && (
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
