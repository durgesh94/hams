import { useMemo, useState } from "react";
import { Add, Delete, Edit, Search, Visibility } from "@mui/icons-material";
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
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
import { useGetDoctorsQuery } from "../../features/doctors/doctorApi";
import type { Doctor } from "../../features/doctors/types";

const DoctorPage = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const {
    data: doctors = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useGetDoctorsQuery();

  const errorMessage = useMemo(() => {
    if (!error) {
      return "Unable to load doctors. Please try again.";
    }

    if ("status" in error) {
      const errorData = error.data;

      if (
        errorData &&
        typeof errorData === "object" &&
        "message" in errorData
      ) {
        return String(errorData.message);
      }

      return `Unable to load doctors. Request failed with status ${error.status}.`;
    }

    return error.message ?? "Unable to load doctors. Please try again.";
  }, [error]);

  const filteredDoctors = useMemo(() => {
    const searchValue = search.toLowerCase().trim();

    if (!searchValue) {
      return doctors;
    }

    return doctors.filter(
      (doctor) =>
        doctor.firstName.toLowerCase().includes(searchValue) ||
        doctor.lastName.toLowerCase().includes(searchValue) ||
        doctor.specialization.toLowerCase().includes(searchValue) ||
        doctor.qualification.toLowerCase().includes(searchValue) ||
        doctor.phone.includes(searchValue),
    );
  }, [doctors, search]);

  const paginatedDoctors = filteredDoctors.slice(
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
    console.log("Add doctor");
  };

  const handleView = (doctor: Doctor) => {
    console.log("View doctor:", doctor);
  };

  const handleEdit = (doctor: Doctor) => {
    console.log("Edit doctor:", doctor);
  };

  const handleDelete = (doctor: Doctor) => {
    console.log("Delete doctor:", doctor);
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
          <Typography variant="h4">Doctors</Typography>

          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            Manage hospital doctors
          </Typography>
        </Box>

        <Button variant="contained" startIcon={<Add />} onClick={handleAdd}>
          Add Doctor
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
          placeholder="Search by name, specialization, qualification or phone"
          value={search}
          onChange={handleSearchChange}
          size="small"
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

      {/* Doctor Table */}
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
                <TableCell>Doctor Name</TableCell>
                <TableCell>Specialization</TableCell>
                <TableCell>Qualification</TableCell>
                <TableCell>Phone</TableCell>
                <TableCell align="center">Appointments</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {isLoading ? (
                <TableRow>
                  <TableCell colSpan={7} align="center" sx={{ py: 6 }}>
                    <CircularProgress size={28} />
                    <Typography color="text.secondary" sx={{ mt: 2 }}>
                      Loading doctors...
                    </Typography>
                  </TableCell>
                </TableRow>
              ) : isError ? (
                <TableRow>
                  <TableCell colSpan={7} sx={{ py: 4 }}>
                    <Alert
                      severity="error"
                      action={
                        <Button color="inherit" size="small" onClick={refetch}>
                          Retry
                        </Button>
                      }
                    >
                      {errorMessage}
                    </Alert>
                  </TableCell>
                </TableRow>
              ) : paginatedDoctors.length > 0 ? (
                paginatedDoctors.map((doctor) => (
                  <TableRow
                    key={doctor.id}
                    hover
                    sx={{
                      "&:last-child td": {
                        borderBottom: 0,
                      },
                    }}
                  >
                    <TableCell>{doctor.id}</TableCell>

                    <TableCell>
                      <Typography>{doctor.firstName} {doctor.lastName}</Typography>
                    </TableCell>

                    <TableCell>{doctor.specialization}</TableCell>

                    <TableCell>{doctor.qualification}</TableCell>

                    <TableCell>{doctor.phone}</TableCell>

                    <TableCell align="center">
                      <Chip
                        label={doctor.appointmentCount}
                        color="primary"
                        size="small"
                      />
                    </TableCell>

                    <TableCell align="center">
                      <Tooltip title="View">
                        <IconButton
                          size="small"
                          color="primary"
                          onClick={() => handleView(doctor)}
                          aria-label={`View ${doctor.firstName}`}
                        >
                          <Visibility fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Edit">
                        <IconButton
                          size="small"
                          color="secondary"
                          onClick={() => handleEdit(doctor)}
                          aria-label={`Edit ${doctor.firstName}`}
                        >
                          <Edit fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Delete">
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() => handleDelete(doctor)}
                          aria-label={`Delete ${doctor.firstName}`}
                        >
                          <Delete fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={7} align="center" sx={{ py: 6 }}>
                    <Typography color="text.secondary">
                      No doctors found
                    </Typography>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>

        {!isLoading && !isError && (
          <TablePagination
            component="div"
            count={filteredDoctors.length}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            rowsPerPageOptions={[5, 10, 25]}
          />
        )}
      </Paper>
    </Box>
  );
};

export default DoctorPage;
