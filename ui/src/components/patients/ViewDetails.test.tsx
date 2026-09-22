import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import ViewDetails from "./ViewDetails";
import type { Patient } from "../../features/patients/types";

const patient: Patient = {
  id: 42,
  firstName: "Jane",
  lastName: "Doe",
  dateOfBirth: "1990-05-20",
  gender: "FEMALE",
  email: "jane.doe@test.com",
  phone: "9876543210",
};

describe("PatientViewDetails", () => {
  it("should show a fallback when no patient is selected", () => {
    render(<ViewDetails patient={null} />);

    expect(screen.getByText("No details available")).toBeInTheDocument();
    expect(screen.queryByText("Basic Information")).not.toBeInTheDocument();
  });

  it("should render patient identity and contact details", () => {
    render(<ViewDetails patient={patient} />);

    expect(screen.getByText("JD")).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: "Jane Doe" }),
    ).toBeInTheDocument();
    expect(screen.getByText("Patient ID: 42")).toBeInTheDocument();
    expect(screen.getByText("Basic Information")).toBeInTheDocument();
    expect(screen.getByText("Contact Information")).toBeInTheDocument();
    expect(screen.getByText("Female")).toBeInTheDocument();
    expect(screen.getByText("1990-05-20")).toBeInTheDocument();
    expect(screen.getByText("9876543210")).toBeInTheDocument();
    expect(screen.getByText("jane.doe@test.com")).toBeInTheDocument();
  });

  it("should format uppercase gender values for display", () => {
    render(<ViewDetails patient={{ ...patient, gender: "MALE" }} />);

    expect(screen.getByText("Male")).toBeInTheDocument();
    expect(screen.queryByText("MALE")).not.toBeInTheDocument();
  });
});
