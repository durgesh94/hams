import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import ViewDetails from "./ViewDetails";
import type { Appointment } from "../../features/appointments/types";

const appointment: Appointment = {
  id: 42,
  patientId: 10,
  patientName: "Jane Doe",
  doctorId: 20,
  doctorName: "John Smith",
  appointmentDate: "2026-10-01",
  appointmentTime: "10:30:00",
  reason: "Routine check-up",
  notes: "Bring previous reports",
  status: "CONFIRMED",
  createdAt: "2026-09-01T10:00:00",
  updatedAt: "2026-09-01T10:00:00",
};

describe("AppointmentViewDetails", () => {
  it("should show a fallback when no appointment is selected", () => {
    render(<ViewDetails appointment={null} />);

    expect(screen.getByText("No details available")).toBeInTheDocument();
    expect(
      screen.queryByText("Appointment Information"),
    ).not.toBeInTheDocument();
  });

  it("should render appointment identity and details", () => {
    render(<ViewDetails appointment={appointment} />);

    expect(
      screen.getByRole("heading", { name: "Appointment #42" }),
    ).toBeInTheDocument();
    expect(screen.getByText("01 Oct 2026 at 10:30 am")).toBeInTheDocument();
    expect(screen.getByText("CONFIRMED")).toBeInTheDocument();
    expect(screen.getByText("Appointment Information")).toBeInTheDocument();
    expect(screen.getByText("Jane Doe")).toBeInTheDocument();
    expect(screen.getByText("John Smith")).toBeInTheDocument();
    expect(screen.getByText("Visit Details")).toBeInTheDocument();
    expect(screen.getByText("Routine check-up")).toBeInTheDocument();
    expect(screen.getByText("Bring previous reports")).toBeInTheDocument();
  });

  it("should preserve invalid date and time values", () => {
    render(
      <ViewDetails
        appointment={{
          ...appointment,
          appointmentDate: "not-a-date",
          appointmentTime: "not-a-time",
        }}
      />,
    );

    expect(screen.getByText("not-a-date", { exact: true })).toBeInTheDocument();
    expect(screen.getByText("not-a-time", { exact: true })).toBeInTheDocument();
    expect(screen.getByText("not-a-date at not-a-time")).toBeInTheDocument();
  });
});
