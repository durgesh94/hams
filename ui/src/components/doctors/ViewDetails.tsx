interface ViewDetailsProps {
  doctor: {
    firstName: string;
    lastName: string;
    specialization: string;
    qualification: string;
    phone: string;
    appointmentCount: number;
    email: string;
  } | null;
}

const ViewDetails = ({ doctor }: ViewDetailsProps) => {
  if (!doctor) {
    return <div>No details available</div>;
  }

  return (
    <div>
      <p>
        Name: {doctor.firstName} {doctor.lastName}
      </p>
      <p>Specialization: {doctor.specialization}</p>
      <p>Qualification: {doctor.qualification}</p>
      <p>Phone: {doctor.phone}</p>
      <p>Email: {doctor.email}</p>
      <p>Appointment Count: {doctor.appointmentCount}</p>
    </div>
  );
};

export default ViewDetails;
