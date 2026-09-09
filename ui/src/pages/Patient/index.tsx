import { useState } from "react";
import { useSelector } from "react-redux";
import { Box } from "@mui/material";

import AppDialog from "../../components/common/AppDialog";
import PageHeader from "../../components/common/PageHeader";
import AddForm from "../../components/patients/AddForm";
import PatientTable from "../../components/patients/PatientTable";
import UpdateForm from "../../components/patients/UpdateForm";
import ViewDetails from "../../components/patients/ViewDetails";
import {
  useCreatePatientMutation,
  useDeletePatientMutation,
  useGetPatientsQuery,
  useUpdatePatientMutation,
} from "../../features/patients/patientApi";
import type {
  CreatePatientRequest,
  Patient,
  UpdatePatientRequest,
} from "../../features/patients/types";
import {
  getDialogAction,
  getDialogButtonLabel,
  getPatientFormId,
} from "../../utils/patient-utils";
import ToastMessage from "../../components/common/ToastMessage";
import { selectIsAdmin } from "../../features/auth/authSelectors";

const PatientPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [dialogContentId, setDialogContentId] = useState(0);
  const [dialogTitle, setDialogTitle] = useState("");
  const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [toastOpen, setToastOpen] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastSeverity, setToastSeverity] = useState<"success" | "error">(
    "error",
  );
  const isAdmin = useSelector(selectIsAdmin);

  const {
    data: patients = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useGetPatientsQuery();

  const [createPatient] = useCreatePatientMutation();
  const [updatePatient] = useUpdatePatientMutation();
  const [deletePatient] = useDeletePatientMutation();

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
    setSelectedPatient(null);
    setDialogTitle("Add Patient");
    setDialogContentId(1);
    setIsDialogOpen(true);
  };

  const handleView = (patient: Patient) => {
    setSelectedPatient(patient);
    setDialogTitle("View Patient");
    setDialogContentId(4);
    setIsDialogOpen(true);
  };

  const handleEdit = (patient: Patient) => {
    setSelectedPatient(patient);
    setDialogTitle("Edit Patient");
    setDialogContentId(2);
    setIsDialogOpen(true);
  };

  const handleDelete = (patient: Patient) => {
    setSelectedPatient(patient);
    setDialogTitle("Delete Patient");
    setDialogContentId(3);
    setIsDialogOpen(true);
  };

  const handleAddFormSubmit = async (data: CreatePatientRequest) => {
    setIsSubmitting(true);

    const result = await createPatient(data);

    if (result.error) {
      showErrorToast(result.error);
    } else {
      showSuccessToast("Patient added successfully");
      setIsDialogOpen(false);
    }

    setIsSubmitting(false);
  };

  const handleUpdateFormSubmit = async (data: UpdatePatientRequest) => {
    setIsSubmitting(true);

    const result = await updatePatient(data);

    if (result.error) {
      showErrorToast(result.error);
    } else {
      showSuccessToast("Patient updated successfully");
      setIsDialogOpen(false);
    }

    setIsSubmitting(false);
  };

  const handleDeleteConfirm = async () => {
    setIsSubmitting(true);

    if (selectedPatient?.id) {
      const result = await deletePatient(selectedPatient.id);

      if (result.error) {
        showErrorToast(result.error);
      } else {
        showSuccessToast("Patient deleted successfully");
        setIsDialogOpen(false);
      }
    }

    setIsSubmitting(false);
  };

  return (
    <Box>
      <PageHeader
        title="Patients"
        subtitle="Manage hospital patients"
        handleAction={handleAdd}
        actionLabel="Add Patient"
        isAdmin={isAdmin}
      />

      <PatientTable
        patients={patients}
        isLoading={isLoading}
        isError={isError}
        error={error ?? null}
        isAdmin={isAdmin}
        onRefetch={refetch}
        onView={handleView}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <AppDialog
        open={isDialogOpen}
        title={dialogTitle}
        onClose={handleDialogClose}
        formId={getPatientFormId(dialogContentId)}
        submitText={getDialogButtonLabel(dialogContentId)}
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
            patient={selectedPatient}
            onSubmit={handleUpdateFormSubmit}
            isSubmitting={isSubmitting}
          />
        )}
        {dialogContentId === 3 && (
          <Box>Do you want to delete {selectedPatient?.firstName}?</Box>
        )}
        {dialogContentId === 4 && <ViewDetails patient={selectedPatient} />}
      </AppDialog>

      <ToastMessage
        toastOpen={toastOpen}
        toastMessage={toastMessage}
        toastSeverity={toastSeverity}
        handleToastClose={handleToastClose}
      />
    </Box>
  );
};

export default PatientPage;
