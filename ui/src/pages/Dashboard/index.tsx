import { Box, Card, CardContent, Grid, Typography } from "@mui/material";

const Dashboard = () => {
  return (
    <Box>
      <Box sx={{ mb: 3, display: "flex", justifyContent: "space-between", alignItems: "center" }} >
        <Typography variant="h4" sx={{ mb: 3 }}>
          Dashboard
        </Typography>
        <Typography variant="h6" sx={{ mb: 3 }}>
          {new Date().toLocaleString("default", { month: "long" })} {new Date().getFullYear()}
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card>
            <CardContent>
              <Typography variant="h6">Doctors</Typography>

              <Typography variant="h3">12</Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 4 }}>
          <Card>
            <CardContent>
              <Typography variant="h6">Patients</Typography>

              <Typography variant="h3">45</Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 4 }}>
          <Card>
            <CardContent>
              <Typography variant="h6">Appointments</Typography>

              <Typography variant="h3">128</Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
