import { useMemo, useState } from "react";
import { Add, Delete, Edit, Search, Visibility } from "@mui/icons-material";
import {
  Box,
  CircularProgress,
  Chip,
  IconButton,
  InputAdornment,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TablePagination,
  TableRow,
  TableHead,
  TextField,
  Tooltip,
  Typography,
  Button,
} from "@mui/material";

import type {
  Appointment,
  AppointmentStatus,
} from "../../features/appointments/types";
import { useGetAppointmentsQuery } from "../../features/appointments/appointmentApi";
import { getApiErrorMessage } from "../../features/api/apiError";

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

const Appointments = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const { data, isLoading, isError, error } = useGetAppointmentsQuery();

  const appointments = data?.data.content ?? [];

  const filteredAppointments = useMemo(() => {
    const searchValue = search.toLowerCase().trim();

    if (!searchValue) {
      return appointments;
    }

    return appointments.filter(
      (appointment) =>
        appointment.patientName.toLowerCase().includes(searchValue) ||
        appointment.doctorName.toLowerCase().includes(searchValue) ||
        appointment.reason.toLowerCase().includes(searchValue) ||
        appointment.status.toLowerCase().includes(searchValue),
    );
  }, [search, appointments]);

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
          placeholder="Search by patient, doctor, reason or status"
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
                <TableCell>Patient</TableCell>
                <TableCell>Doctor</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Time</TableCell>
                <TableCell>Reason</TableCell>
                <TableCell align="center">Status</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={8} align="center" sx={{ py: 6 }}>
                    <CircularProgress size={32} />
                  </TableCell>
                </TableRow>
              ) : isError ? (
                <TableRow>
                  <TableCell colSpan={8} align="center" sx={{ py: 6 }}>
                    <Typography color="error">
                      {getApiErrorMessage(error)}
                    </Typography>
                  </TableCell>
                </TableRow>
              ) : paginatedAppointments.length > 0 ? (
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

                    <TableCell>{appointment.doctorName}</TableCell>

                    <TableCell>
                      {formatDate(appointment.appointmentDate)}
                    </TableCell>

                    <TableCell>
                      {formatTime(appointment.appointmentTime)}
                    </TableCell>

                    <TableCell>{appointment.reason}</TableCell>

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
