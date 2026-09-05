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

interface Doctor {
  id: number;
  name: string;
  specialization: string;
  phone: string;
  email: string;
  appointmentCount: number;
}

const mockDoctors: Doctor[] = [
  {
    id: 1,
    name: "Dr. Rajesh Sharma",
    specialization: "Cardiologist",
    phone: "9876543210",
    email: "rajesh.sharma@hospital.com",
    appointmentCount: 12,
  },
  {
    id: 2,
    name: "Dr. Priya Patil",
    specialization: "Neurologist",
    phone: "9876543211",
    email: "priya.patil@hospital.com",
    appointmentCount: 8,
  },
  {
    id: 3,
    name: "Dr. Amit Deshmukh",
    specialization: "Dermatologist",
    phone: "9876543212",
    email: "amit.deshmukh@hospital.com",
    appointmentCount: 5,
  },
  {
    id: 4,
    name: "Dr. Sneha Kulkarni",
    specialization: "Pediatrician",
    phone: "9876543213",
    email: "sneha.kulkarni@hospital.com",
    appointmentCount: 15,
  },
  {
    id: 5,
    name: "Dr. Rahul Joshi",
    specialization: "Orthopedic",
    phone: "9876543214",
    email: "rahul.joshi@hospital.com",
    appointmentCount: 10,
  },
  {
    id: 6,
    name: "Dr. Neha More",
    specialization: "Gynecologist",
    phone: "9876543215",
    email: "neha.more@hospital.com",
    appointmentCount: 7,
  },
  {
    id: 7,
    name: "Dr. Vikram Singh",
    specialization: "General Physician",
    phone: "9876543216",
    email: "vikram.singh@hospital.com",
    appointmentCount: 18,
  },
];

const Doctor = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const filteredDoctors = useMemo(() => {
    const searchValue = search.toLowerCase().trim();

    if (!searchValue) {
      return mockDoctors;
    }

    return mockDoctors.filter(
      (doctor) =>
        doctor.name.toLowerCase().includes(searchValue) ||
        doctor.specialization.toLowerCase().includes(searchValue) ||
        doctor.phone.includes(searchValue) ||
        doctor.email.toLowerCase().includes(searchValue),
    );
  }, [search]);

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
          placeholder="Search by name, specialization, phone or email"
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
                <TableCell>Phone</TableCell>
                <TableCell>Email</TableCell>
                <TableCell align="center">Appointments</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {paginatedDoctors.length > 0 ? (
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
                      <Typography>{doctor.name}</Typography>
                    </TableCell>

                    <TableCell>{doctor.specialization}</TableCell>

                    <TableCell>{doctor.phone}</TableCell>

                    <TableCell>{doctor.email}</TableCell>

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
                          aria-label={`View ${doctor.name}`}
                        >
                          <Visibility fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Edit">
                        <IconButton
                          size="small"
                          color="secondary"
                          onClick={() => handleEdit(doctor)}
                          aria-label={`Edit ${doctor.name}`}
                        >
                          <Edit fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Delete">
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() => handleDelete(doctor)}
                          aria-label={`Delete ${doctor.name}`}
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

        <TablePagination
          component="div"
          count={filteredDoctors.length}
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

export default Doctor;
