import { Box, Grid } from "@mui/material";
import PageHeader from "../../components/common/PageHeader";
import Card from "../../components/common/Card";

const Dashboard = () => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <PageHeader
        title="Dashboard"
        subtitle="Overview of hospital management system"
        rightLabel={`${new Date().toLocaleString("default", { month: "long" })} ${new Date().getFullYear()}`}
      />

      <Grid container spacing={3}>
        <Card
          title="Doctors"
          count={12}
          description="Active Doctors in this month"
        />
        <Card
          title="Patients"
          count={45}
          description="Patients newly registered in this month"
        />
        <Card
          title="Appointments"
          count={128}
          description="Scheduled Appointments in this month"
        />
      </Grid>
    </Box>
  );
};

export default Dashboard;
