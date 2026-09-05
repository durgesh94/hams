import { useMemo, useState } from "react";
import { Add, Delete, Edit, Search, Visibility } from "@mui/icons-material";
import {
  Box,
  Button,
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

interface Patient {
  id: number;
  name: string;
  email: string;
  phone: string;
  dateOfBirth: string;
}

const mockPatients: Patient[] = [
  {
    id: 1,
    name: "Rahul Kumar",
    email: "rahul.kumar@gmail.com",
    phone: "9876543210",
    dateOfBirth: "15 Jan 1990",
  },
  {
    id: 2,
    name: "Priya Sharma",
    email: "priya.sharma@gmail.com",
    phone: "9876543211",
    dateOfBirth: "20 Mar 1988",
  },
  {
    id: 3,
    name: "Amit Patil",
    email: "amit.patil@gmail.com",
    phone: "9876543212",
    dateOfBirth: "08 Jul 1995",
  },
  {
    id: 4,
    name: "Sneha Deshmukh",
    email: "sneha.deshmukh@gmail.com",
    phone: "9876543213",
    dateOfBirth: "12 Nov 1992",
  },
  {
    id: 5,
    name: "Vikram Joshi",
    email: "vikram.joshi@gmail.com",
    phone: "9876543214",
    dateOfBirth: "25 Feb 1985",
  },
  {
    id: 6,
    name: "Neha Kulkarni",
    email: "neha.kulkarni@gmail.com",
    phone: "9876543215",
    dateOfBirth: "03 May 1998",
  },
  {
    id: 7,
    name: "Suresh More",
    email: "suresh.more@gmail.com",
    phone: "9876543216",
    dateOfBirth: "19 Sep 1979",
  },
];

const Patient = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  const filteredPatients = useMemo(() => {
    const searchValue = search.toLowerCase().trim();

    if (!searchValue) {
      return mockPatients;
    }

    return mockPatients.filter(
      (patient) =>
        patient.name.toLowerCase().includes(searchValue) ||
        patient.email.toLowerCase().includes(searchValue) ||
        patient.phone.includes(searchValue),
    );
  }, [search]);

  const paginatedPatients = filteredPatients.slice(
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
    console.log("Add patient");
  };

  const handleView = (patient: Patient) => {
    console.log("View patient:", patient);
  };

  const handleEdit = (patient: Patient) => {
    console.log("Edit patient:", patient);
  };

  const handleDelete = (patient: Patient) => {
    console.log("Delete patient:", patient);
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
          <Typography variant="h4">Patients</Typography>

          <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
            Manage hospital patients
          </Typography>
        </Box>

        <Button variant="contained" startIcon={<Add />} onClick={handleAdd}>
          Add Patient
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
          placeholder="Search by name, email or phone"
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

      {/* Patient Table */}
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
                <TableCell>Email</TableCell>
                <TableCell>Phone</TableCell>
                <TableCell>Date of Birth</TableCell>
                <TableCell align="center">Actions</TableCell>
              </TableRow>
            </TableHead>

            <TableBody>
              {paginatedPatients.length > 0 ? (
                paginatedPatients.map((patient) => (
                  <TableRow
                    key={patient.id}
                    hover
                    sx={{
                      "&:last-child td": {
                        borderBottom: 0,
                      },
                    }}
                  >
                    <TableCell>{patient.id}</TableCell>

                    <TableCell>
                      <Typography variant="body2">{patient.name}</Typography>
                    </TableCell>

                    <TableCell>{patient.email}</TableCell>

                    <TableCell>{patient.phone}</TableCell>

                    <TableCell>{patient.dateOfBirth}</TableCell>

                    <TableCell align="center">
                      <Tooltip title="View">
                        <IconButton
                          size="small"
                          color="primary"
                          onClick={() => handleView(patient)}
                          aria-label={`View ${patient.name}`}
                        >
                          <Visibility fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Edit">
                        <IconButton
                          size="small"
                          color="secondary"
                          onClick={() => handleEdit(patient)}
                          aria-label={`Edit ${patient.name}`}
                        >
                          <Edit fontSize="small" />
                        </IconButton>
                      </Tooltip>

                      <Tooltip title="Delete">
                        <IconButton
                          size="small"
                          color="error"
                          onClick={() => handleDelete(patient)}
                          aria-label={`Delete ${patient.name}`}
                        >
                          <Delete fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={6} align="center" sx={{ py: 6 }}>
                    <Typography color="text.secondary">
                      No patients found
                    </Typography>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>

        <TablePagination
          component="div"
          count={filteredPatients.length}
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

export default Patient;
