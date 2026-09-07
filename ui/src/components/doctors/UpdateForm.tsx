import type { UpdateDoctorRequest } from "../../features/doctors/types";

interface UpdateFormProps {
  doctor: {
    firstName: string;
    lastName: string;
    specialization: string;
    qualification: string;
    phone: string;
    appointmentCount: number;
    email: string;
  } | null;
  onSubmit: (data: UpdateDoctorRequest) => void;
  isSubmitting?: boolean;
}

const UpdateForm = ({ doctor, onSubmit, isSubmitting }: UpdateFormProps) => {
  if (!doctor) {
    return <div>No doctor selected</div>;
  }

  return (
    <div>
      Update Form for {doctor.firstName} {doctor.lastName}
    </div>
  );
};

export default UpdateForm;
