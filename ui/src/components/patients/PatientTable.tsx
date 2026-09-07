import { useMemo, useState } from "react";
import { Delete, Edit, Search, Visibility } from "@mui/icons-material";
import {
	Alert,
	Box,
	Button,
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

import type { Patient } from "../../features/patients/types";

type ApiError =
	| {
			status: number | string;
			data?: unknown;
		}
	| {
			message?: string;
		};

interface PatientTableProps {
	patients: Patient[];
	isLoading: boolean;
	isError: boolean;
	error: ApiError | null;
	onRefetch: () => void;
	onView: (patient: Patient) => void;
	onEdit: (patient: Patient) => void;
	onDelete: (patient: Patient) => void;
}

const PatientTable = ({
	patients,
	isLoading,
	isError,
	error,
	onRefetch,
	onView,
	onEdit,
	onDelete,
}: PatientTableProps) => {
	const [search, setSearch] = useState("");
	const [page, setPage] = useState(0);
	const [rowsPerPage, setRowsPerPage] = useState(5);

	const errorMessage = useMemo(() => {
		if (!error) {
			return "Unable to load patients. Please try again.";
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

			return `Unable to load patients. Request failed with status ${error.status}.`;
		}

		return error.message ?? "Unable to load patients. Please try again.";
	}, [error]);

	const filteredPatients = useMemo(() => {
		const searchValue = search.toLowerCase().trim();

		if (!searchValue) {
			return patients;
		}

		return patients.filter(
			(patient) =>
				patient.firstName.toLowerCase().includes(searchValue) ||
				patient.lastName.toLowerCase().includes(searchValue) ||
				patient.email.toLowerCase().includes(searchValue) ||
				patient.phone.includes(searchValue),
		);
	}, [patients, search]);

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

	return (
		<Box>
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
					placeholder="Search by name, phone or email"
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
								<TableCell>Gender</TableCell>
								<TableCell>Date of Birth</TableCell>
								<TableCell>Email</TableCell>
								<TableCell>Phone</TableCell>
								<TableCell align="center">Actions</TableCell>
							</TableRow>
						</TableHead>

						<TableBody>
							{isLoading ? (
								<TableRow>
									<TableCell colSpan={7} align="center" sx={{ py: 6 }}>
										<CircularProgress size={28} />
										<Typography color="text.secondary" sx={{ mt: 2 }}>
											Loading patients...
										</Typography>
									</TableCell>
								</TableRow>
							) : isError ? (
								<TableRow>
									<TableCell colSpan={7} sx={{ py: 4 }}>
										<Alert
											severity="error"
											action={
												<Button color="inherit" size="small" onClick={onRefetch}>
													Retry
												</Button>
											}
										>
											{errorMessage}
										</Alert>
									</TableCell>
								</TableRow>
							) : paginatedPatients.length > 0 ? (
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
											<Typography>
												{patient.firstName} {patient.lastName}
											</Typography>
										</TableCell>

										<TableCell>{patient.gender}</TableCell>
										<TableCell>{patient.dateOfBirth}</TableCell>
										<TableCell>{patient.email}</TableCell>
										<TableCell>{patient.phone}</TableCell>

										<TableCell align="center">
											<Tooltip title="View">
												<IconButton
													size="small"
													color="primary"
													onClick={() => onView(patient)}
													aria-label={`View ${patient.firstName}`}
												>
													<Visibility fontSize="small" />
												</IconButton>
											</Tooltip>

											<Tooltip title="Edit">
												<IconButton
													size="small"
													color="secondary"
													onClick={() => onEdit(patient)}
													aria-label={`Edit ${patient.firstName}`}
												>
													<Edit fontSize="small" />
												</IconButton>
											</Tooltip>

											<Tooltip title="Delete">
												<IconButton
													size="small"
													color="error"
													onClick={() => onDelete(patient)}
													aria-label={`Delete ${patient.firstName}`}
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
											No patients found
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
						count={filteredPatients.length}
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

export default PatientTable;
