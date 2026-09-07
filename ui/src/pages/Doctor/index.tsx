import { useState } from "react";
import { Box } from "@mui/material";
import { useGetDoctorsQuery } from "../../features/doctors/doctorApi";
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

const DoctorPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [dialogContentId, setDialogContentId] = useState(0);
  const [dialogTitle, setDialogTitle] = useState("");
  const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    data: doctors = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useGetDoctorsQuery();

  const handleDialogClose = () => {
    setIsDialogOpen(false);
  };

  const handleAdd = () => {
    setDialogTitle("Add Doctor");
    setDialogContentId(1);
    setIsDialogOpen(true);
  };

  const handleView = (doctor: Doctor) => {
    console.log("View doctor:", doctor);
    setSelectedDoctor(doctor);
    setDialogTitle("View Doctor");
    setDialogContentId(4);
    setIsDialogOpen(true);
  };

  const handleEdit = (doctor: Doctor) => {
    console.log("Edit doctor:", doctor);
    setSelectedDoctor(doctor);
    setDialogTitle("Edit Doctor");
    setDialogContentId(2);
    setIsDialogOpen(true);
  };

  const handleDelete = (doctor: Doctor) => {
    console.log("Delete doctor:", doctor);
    setSelectedDoctor(doctor);
    setDialogTitle("Delete Doctor");
    setDialogContentId(3);
    setIsDialogOpen(true);
  };

  const handleAddFormSubmit = (data: CreateDoctorRequest) => {
    setIsSubmitting(true);
    console.log("Adding doctor:", data);
    // TODO: Call create doctor API
    setTimeout(() => {
      setIsSubmitting(false);
      setIsDialogOpen(false);
    }, 1000);
  };

  const handleUpdateFormSubmit = (data: UpdateDoctorRequest) => {
    setIsSubmitting(true);
    console.log("Updating doctor:", data);
    // TODO: Call update doctor API
    setTimeout(() => {
      setIsSubmitting(false);
      setIsDialogOpen(false);
    }, 1000);
  };

  const handleDeleteConfirm = () => {
    setIsSubmitting(true);
    console.log("Deleting doctor:", selectedDoctor?.id);
    // TODO: Call delete doctor API
    setTimeout(() => {
      setIsSubmitting(false);
      setIsDialogOpen(false);
    }, 1000);
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
        formId={
          dialogContentId === 1
            ? "add-doctor-form"
            : dialogContentId === 2
              ? "update-doctor-form"
              : undefined
        }
        submitText={
          dialogContentId === 3
            ? "Confirm"
            : dialogContentId === 4
              ? "Ok"
              : "Save"
        }
        onSubmit={
          dialogContentId === 3
            ? handleDeleteConfirm
            : dialogContentId === 4
              ? handleDialogClose
              : undefined
        }
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
    </Box>
  );
};

export default DoctorPage;
