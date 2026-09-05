import { useMemo, useState } from "react";
import { Add, Delete, Edit, Search, Visibility } from "@mui/icons-material";
import {
  Box,
  Button,
  Chip,
  IconButton,
  InputAdornment,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Tooltip,
  Typography,
} from "@mui/material";

type AppointmentStatus = "CONFIRMED" | "PENDING" | "COMPLETED" | "CANCELLED";

interface Appointment {
  id: number;
  patientName: string;
  patientEmail: string;
  doctorName: string;
  date: string;
  time: string;
  status: AppointmentStatus;
}

const mockAppointments: Appointment[] = [
  {
    id: 1,
    patientName: "Rahul Kumar",
    patientEmail: "rahul.kumar@gmail.com",
    doctorName: "Dr. Rajesh Sharma",
    date: "05 Sep 2026",
    time: "10:00 AM",
    status: "CONFIRMED",
  },
  {
    id: 2,
    patientName: "Priya Sharma",
    patientEmail: "priya.sharma@gmail.com",
    doctorName: "Dr. Priya Patil",
    date: "05 Sep 2026",
    time: "11:30 AM",
    status: "PENDING",
  },
  {
    id: 3,
    patientName: "Amit Patil",
    patientEmail: "amit.patil@gmail.com",
    doctorName: "Dr. Amit Deshmukh",
    date: "06 Sep 2026",
    time: "02:00 PM",
    status: "COMPLETED",
  },
  {
    id: 4,
    patientName: "Sneha Deshmukh",
    patientEmail: "sneha.deshmukh@gmail.com",
    doctorName: "Dr. Sneha Kulkarni",
    date: "07 Sep 2026",
    time: "04:30 PM",
    status: "CANCELLED",
  },
  {
    id: 5,
    patientName: "Vikram Joshi",
    patientEmail: "vikram.joshi@gmail.com",
    doctorName: "Dr. Rahul Joshi",
    date: "08 Sep 2026",
    time: "09:30 AM",
    status: "CONFIRMED",
  },
  {
    id: 6,
    patientName: "Neha More",
    patientEmail: "neha.more@gmail.com",
    doctorName: "Dr. Neha More",
    date: "08 Sep 2026",
    time: "01:00 PM",
    status: "PENDING",
  },
  {
    id: 7,
    patientName: "Suresh More",
    patientEmail: "suresh.more@gmail.com",
    doctorName: "Dr. Vikram Singh",
    date: "09 Sep 2026",
    time: "03:30 PM",
    status: "CONFIRMED",
  },
  {
    id: 8,
    patientName: "Kiran Shah",
    patientEmail: "kiran.shah@gmail.com",
    doctorName: "Dr. Rajesh Sharma",
    date: "10 Sep 2026",
    time: "11:00 AM",
    status: "COMPLETED",
  },
];

const getStatusColor = (
  status: AppointmentStatus,
): "success" | "warning" | "error" | "default" => {
  switch (status) {
    case "CONFIRMED":
      return "success";

    case "PENDING":
      return "warning";

    case "CANCELLED":
      return "error";

    case "COMPLETED":
      return "default";

    default:
      return "default";
  }
};

const Appointments = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const filteredAppointments = useMemo(() => {
    const searchValue = search.toLowerCase().trim();

    if (!searchValue) {
      return mockAppointments;
    }

    return mockAppointments.filter(
      (appointment) =>
        appointment.patientName.toLowerCase().includes(searchValue) ||
        appointment.patientEmail.toLowerCase().includes(searchValue) ||
        appointment.doctorName.toLowerCase().includes(searchValue) ||
        appointment.status.toLowerCase().includes(searchValue),
    );
  }, [search]);

  const paginatedAppointments = filteredAppointments.slice(
    page * rowsPerPage,
    page * rowsPerPage + rowsPerPage,
  );

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(event.target.value);
    setPage(0);
  };

  const handleChangePage = (_event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    setRowsPerPage(Number.parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleAdd = () => {
    console.log("Add appointment");
  };

  const handleView = (appointment: Appointment) => {
    console.log("View appointment:", appointment);
  };

  const handleEdit = (appointment: Appointment) => {
    console.log("Edit appointment:", appointment);
  };

  const handleDelete = (appointment: Appointment) => {
    console.log("Delete appointment:", appointment);
  };

  return (
    <Box>
      {/* Page Header */}
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: { xs: "flex-start", sm: "center" },
          flexDirection: { xs: "column", sm: "row" },
          gap: 2,
          mb: 3,
        }}
      >
        <Box>
          <Typography variant="h4">Appointments</Typography>

          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            Manage hospital appointments
          </Typography>
        </Box>

        <Button variant="contained" startIcon={<Add />} onClick={handleAdd}>
          Add Appointment
        </Button>
      </Box>

      {/* Search */}
      <Paper
        elevation={0}
        sx={{
          p: 2,
          mb: 2,
          border: "1px solid",
          borderColor: "divider",
        }}
      >
        <TextField
          fullWidth
          size="small"
          placeholder="Search by patient, doctor, email or status"
          value={search}
          onChange={handleSearchChange}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <Search color="action" />
                </InputAdornment>
              ),
            },
          }}
        />
      </Paper>

      {/* Appointment Table */}
      <Paper
        elevation={0}
        sx={{
          border: "1px solid",
          borderColor: "divider",
          overflow: "hidden",
        }}
      >
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow
                sx={{
                  backgroundColor: "primary.main",
                  "& .MuiTableCell-head": {
                    color: "primary.contrastText",
                    fontWeight: 600,
                  },
                }}
              >
                <TableCell>ID</TableCell>
                <TableCell>Patient Name</TableCell>
                <TableCell>Patient Email</TableCell>
                <TableCell>Doctor</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Time</TableCell>
                <TableCell align="center">Status</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {paginatedAppointments.length > 0 ? (
                paginatedAppointments.map((appointment) => (
                  <TableRow
                    key={appointment.id}
                    hover
                    sx={{
                      "&:last-child td": {
                        borderBottom: 0,
                      },
                    }}
                  >
                    <TableCell>{appointment.id}</TableCell>

                    <TableCell>
                      <Typography variant="body2">
                        {appointment.patientName}
                      </Typography>
                    </TableCell>

                    <TableCell>{appointment.patientEmail}</TableCell>

                    <TableCell>{appointment.doctorName}</TableCell>

                    <TableCell>{appointment.date}</TableCell>

                    <TableCell>{appointment.time}</TableCell>

                    <TableCell align="center">
                      <Chip
                        label={appointment.status}
                        color={getStatusColor(appointment.status)}
                        size="small"
                        sx={{
                          fontWeight: 500,
                          minWidth: 90,
                        }}
                      />
                    </TableCell>

                    <TableCell align="center">
                      <Tooltip title="View">
                        <IconButton
                          size="small"
                          color="primary"
                          onClick={() => handleView(appointment)}
                          aria-label={`View appointment ${appointment.id}`}
                        >
                          <Visibility fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Edit">
                        <IconButton
                          size="small"
                          color="secondary"
                          onClick={() => handleEdit(appointment)}
                          aria-label={`Edit appointment ${appointment.id}`}
                        >
                          <Edit fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Delete">
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() => handleDelete(appointment)}
                          aria-label={`Delete appointment ${appointment.id}`}
                        >
                          <Delete fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={8} align="center" sx={{ py: 6 }}>
                    <Typography color="text.secondary">
                      No appointments found
                    </Typography>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>

        <TablePagination
          component="div"
          count={filteredAppointments.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          rowsPerPageOptions={[5, 10, 25]}
        />
      </Paper>
    </Box>
  );
};

export default Appointments;
