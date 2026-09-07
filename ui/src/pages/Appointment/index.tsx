import { useState } from "react";
import { Box } from "@mui/material";

import AppDialog from "../../components/common/AppDialog";
import PageHeader from "../../components/common/PageHeader";
import ToastMessage from "../../components/common/ToastMessage";
import AddForm from "../../components/appointments/AddForm";
import AppointmentTable from "../../components/appointments/AppointmentTable";
import UpdateForm from "../../components/appointments/UpdateForm";
import ViewDetails from "../../components/appointments/ViewDetails";
import {
  useCreateAppointmentMutation,
  useDeleteAppointmentMutation,
  useGetAppointmentsQuery,
  useUpdateAppointmentMutation,
} from "../../features/appointments/appointmentApi";
import type {
  Appointment,
  CreateAppointmentRequest,
  UpdateAppointmentRequest,
} from "../../features/appointments/types";
import { useGetDoctorsQuery } from "../../features/doctors/doctorApi";
import { useGetPatientsQuery } from "../../features/patients/patientApi";
import {
  getAppointmentFormId,
  getDialogAction,
  getDialogButtonLabel,
} from "../../utils/appointment-utils";

const Appointments = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [dialogContentId, setDialogContentId] = useState(0);
  const [dialogTitle, setDialogTitle] = useState("");
  const [selectedAppointment, setSelectedAppointment] =
    useState<Appointment | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [toastOpen, setToastOpen] = useState(false);
  const [toastMessage, setToastMessage] = useState("");
  const [toastSeverity, setToastSeverity] = useState<"success" | "error">(
    "error",
  );

  const {
    data: appointmentPage,
    isLoading,
    isError,
    error,
    refetch,
  } = useGetAppointmentsQuery();

  const { data: doctors = [] } = useGetDoctorsQuery();
  const { data: patients = [] } = useGetPatientsQuery();

  const [createAppointment] = useCreateAppointmentMutation();
  const [updateAppointment] = useUpdateAppointmentMutation();
  const [deleteAppointment] = useDeleteAppointmentMutation();

  const appointments = appointmentPage?.content ?? [];

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
    setSelectedAppointment(null);
    setDialogTitle("Add Appointment");
    setDialogContentId(1);
    setIsDialogOpen(true);
  };

  const handleView = (appointment: Appointment) => {
    setSelectedAppointment(appointment);
    setDialogTitle("View Appointment");
    setDialogContentId(4);
    setIsDialogOpen(true);
  };

  const handleEdit = (appointment: Appointment) => {
    setSelectedAppointment(appointment);
    setDialogTitle("Edit Appointment");
    setDialogContentId(2);
    setIsDialogOpen(true);
  };

  const handleDelete = (appointment: Appointment) => {
    setSelectedAppointment(appointment);
    setDialogTitle("Delete Appointment");
    setDialogContentId(3);
    setIsDialogOpen(true);
  };

  const handleAddFormSubmit = async (data: CreateAppointmentRequest) => {
    setIsSubmitting(true);

    const result = await createAppointment(data);

    if (result.error) {
      showErrorToast(result.error);
    } else {
      showSuccessToast("Appointment added successfully");
      setIsDialogOpen(false);
    }

    setIsSubmitting(false);
  };

  const handleUpdateFormSubmit = async (data: UpdateAppointmentRequest) => {
    setIsSubmitting(true);

    const result = await updateAppointment(data);

    if (result.error) {
      showErrorToast(result.error);
    } else {
      showSuccessToast("Appointment updated successfully");
      setIsDialogOpen(false);
    }

    setIsSubmitting(false);
  };

  const handleDeleteConfirm = async () => {
    setIsSubmitting(true);

    if (selectedAppointment?.id) {
      const result = await deleteAppointment(selectedAppointment.id);

      if (result.error) {
        showErrorToast(result.error);
      } else {
        showSuccessToast("Appointment deleted successfully");
        setIsDialogOpen(false);
      }
    }

    setIsSubmitting(false);
  };

  return (
    <Box>
      <PageHeader
        title="Appointments"
        subtitle="Manage hospital appointments"
        actionLabel="Add Appointment"
        handleAction={handleAdd}
      />

      <AppointmentTable
        appointments={appointments}
        isLoading={isLoading}
        isError={isError}
        error={error}
        onRefetch={refetch}
        onView={handleView}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />

      <AppDialog
        open={isDialogOpen}
        title={dialogTitle}
        onClose={handleDialogClose}
        formId={getAppointmentFormId(dialogContentId)}
        submitText={getDialogButtonLabel(dialogContentId)}
        onSubmit={getDialogAction(
          dialogContentId,
          handleDeleteConfirm,
          handleDialogClose,
        )}
        isSubmitting={isSubmitting}
      >
        {dialogContentId === 1 && (
          <AddForm
            doctors={doctors}
            patients={patients}
            onSubmit={handleAddFormSubmit}
            isSubmitting={isSubmitting}
          />
        )}
        {dialogContentId === 2 && (
          <UpdateForm
            appointment={selectedAppointment}
            doctors={doctors}
            patients={patients}
            onSubmit={handleUpdateFormSubmit}
            isSubmitting={isSubmitting}
          />
        )}
        {dialogContentId === 3 && (
          <Box>
            Do you want to delete appointment #{selectedAppointment?.id}?
          </Box>
        )}
        {dialogContentId === 4 && (
          <ViewDetails appointment={selectedAppointment} />
        )}
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

export default Appointments;
