import { useState } from "react";
import { Box, Snackbar, Alert } from "@mui/material";
import {
  useCreateDoctorMutation,
  useUpdateDoctorMutation,
  useDeleteDoctorMutation,
  useGetDoctorsQuery,
} from "../../features/doctors/doctorApi";
import type {
  CreateDoctorRequest,
  Doctor,
  UpdateDoctorRequest,
} from "../../features/doctors/types";
import AppDialog from "../../components/common/AppDialog";
import DoctorTable from "../../components/doctors/DoctorTable";
import AddForm from "../../components/doctors/AddForm";
import UpdateForm from "../../components/doctors/UpdateForm";
import ViewDetails from "../../components/doctors/ViewDetails";
import PageHeader from "../../components/common/PageHeader";
import { getDialogAction, getDoctorFormId } from "../../utils/doctor-utils";

const DoctorPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [dialogContentId, setDialogContentId] = useState(0);
  const [dialogTitle, setDialogTitle] = useState("");
  const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [toastOpen, setToastOpen] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastSeverity, setToastSeverity] = useState<"success" | "error">(
    "error",
  );

  const {
    data: doctors = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useGetDoctorsQuery();

  const [createDoctor] = useCreateDoctorMutation();
  const [updateDoctor] = useUpdateDoctorMutation();
  const [deleteDoctor] = useDeleteDoctorMutation();

  const handleToastClose = () => {
    setToastOpen(false);
  };

  const showErrorToast = (error: unknown) => {
    let message = "An error occurred. Please try again.";

    if (error && typeof error === "object") {
      const err = error as Record<string, unknown>;
      const data = err.data as Record<string, unknown> | undefined;
      if (data?.message && typeof data.message === "string") {
        message = data.message;
      } else if (err.message && typeof err.message === "string") {
        message = err.message;
      }
    }

    setToastMessage(message);
    setToastSeverity("error");
    setToastOpen(true);
  };

  const showSuccessToast = (message: string) => {
    setToastMessage(message);
    setToastSeverity("success");
    setToastOpen(true);
  };

  const handleDialogClose = () => {
    setIsDialogOpen(false);
  };

  const handleAdd = () => {
    setDialogTitle("Add Doctor");
    setDialogContentId(1);
    setIsDialogOpen(true);
  };

  const handleView = (doctor: Doctor) => {
    setSelectedDoctor(doctor);
    setDialogTitle("View Doctor");
    setDialogContentId(4);
    setIsDialogOpen(true);
  };

  const handleEdit = (doctor: Doctor) => {
    setSelectedDoctor(doctor);
    setDialogTitle("Edit Doctor");
    setDialogContentId(2);
    setIsDialogOpen(true);
  };

  const handleDelete = (doctor: Doctor) => {
    setSelectedDoctor(doctor);
    setDialogTitle("Delete Doctor");
    setDialogContentId(3);
    setIsDialogOpen(true);
  };

  const handleAddFormSubmit = async (data: CreateDoctorRequest) => {
    setIsSubmitting(true);

    const result = await createDoctor(data);

    if (result.error) {
      showErrorToast(result.error);
    } else {
      showSuccessToast("Doctor added successfully");
      setIsDialogOpen(false);
    }

    setIsSubmitting(false);
  };

  const handleUpdateFormSubmit = async (data: UpdateDoctorRequest) => {
    setIsSubmitting(true);
    const result = await updateDoctor(data);

    if (result.error) {
      showErrorToast(result.error);
    } else {
      showSuccessToast("Doctor updated successfully");
      setIsDialogOpen(false);
    }

    setIsSubmitting(false);
  };

  const handleDeleteConfirm = async () => {
    setIsSubmitting(true);

    if (selectedDoctor?.id) {
      const result = await deleteDoctor(selectedDoctor.id);

      if (result.error) {
        showErrorToast(result.error);
      } else {
        showSuccessToast("Doctor deleted successfully");
        setIsDialogOpen(false);
      }
    }

    setIsSubmitting(false);
  };

  return (
    <Box>
      {/* Page Header */}
      <PageHeader
        title="Doctors"
        subtitle="Manage hospital doctors"
        handleAction={handleAdd}
      />

      {/* Doctor Table */}
      <DoctorTable
        doctors={doctors}
        isLoading={isLoading}
        isError={isError}
        error={error}
        onRefetch={refetch}
        onView={handleView}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      {/* App Dialog for Add, Update, Delete, and View Doctor */}
      <AppDialog
        open={isDialogOpen}
        title={dialogTitle}
        onClose={handleDialogClose}
        formId={getDoctorFormId(dialogContentId)}
        submitText={
          dialogContentId === 3
            ? "Confirm"
            : dialogContentId === 4
              ? "Ok"
              : "Save"
        }
        onSubmit={getDialogAction(
          dialogContentId,
          handleDeleteConfirm,
          handleDialogClose,
        )}
        isSubmitting={isSubmitting}
      >
        {dialogContentId === 1 && (
          <AddForm onSubmit={handleAddFormSubmit} isSubmitting={isSubmitting} />
        )}
        {dialogContentId === 2 && (
          <UpdateForm
            doctor={selectedDoctor}
            onSubmit={handleUpdateFormSubmit}
            isSubmitting={isSubmitting}
          />
        )}
        {dialogContentId === 3 && (
          <Box>Do you want to delete {selectedDoctor?.firstName}?</Box>
        )}
        {dialogContentId === 4 && <ViewDetails doctor={selectedDoctor} />}
      </AppDialog>

      {/* Toast Notification */}
      <Snackbar
        open={toastOpen}
        autoHideDuration={6000}
        onClose={handleToastClose}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert
          onClose={handleToastClose}
          severity={toastSeverity}
          sx={{ width: "100%" }}
        >
          {toastMessage}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default DoctorPage;
