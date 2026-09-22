import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import ViewDetails from "./ViewDetails";

const doctor = {
  firstName: "John",
  lastName: "Smith",
  gender: "MALE",
  specialization: "Cardiology",
  qualification: "MBBS, MD",
  phone: "9876543210",
  appointmentCount: 18,
  email: "john.smith@test.com",
  status: "ACTIVE",
};

describe("DoctorViewDetails", () => {
  it("should show a fallback when no doctor is selected", () => {
    render(<ViewDetails doctor={null} />);

    expect(screen.getByText("No details available")).toBeInTheDocument();
    expect(
      screen.queryByText("Professional Information"),
    ).not.toBeInTheDocument();
  });

  it("should render doctor identity, professional and contact details", () => {
    render(<ViewDetails doctor={doctor} />);

    expect(screen.getByText("JS")).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: "Dr. John Smith" }),
    ).toBeInTheDocument();
    expect(screen.getByText("Cardiology")).toBeInTheDocument();
    expect(screen.getByText("Active")).toBeInTheDocument();
    expect(screen.getByText("Professional Information")).toBeInTheDocument();
    expect(screen.getByText("Male")).toBeInTheDocument();
    expect(screen.getByText("MBBS, MD")).toBeInTheDocument();
    expect(screen.getByText("Contact Information")).toBeInTheDocument();
    expect(screen.getByText("9876543210")).toBeInTheDocument();
    expect(screen.getByText("john.smith@test.com")).toBeInTheDocument();
  });

  it("should render appointment statistics and format status values", () => {
    render(<ViewDetails doctor={{ ...doctor, status: "ON_LEAVE" }} />);

    expect(screen.getByText("On_leave")).toBeInTheDocument();
    expect(screen.getByText("18")).toBeInTheDocument();
    expect(screen.getByText("Total Appointments")).toBeInTheDocument();
  });
});
