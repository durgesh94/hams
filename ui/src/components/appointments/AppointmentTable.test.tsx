import { fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";

import AppointmentTable from "./AppointmentTable";
import type { Appointment } from "../../features/appointments/types";

const buildAppointment = (
  overrides: Partial<Appointment> = {},
): Appointment => ({
  id: 1,
  patientId: 10,
  patientName: "Jane Doe",
  doctorId: 20,
  doctorName: "John Smith",
  appointmentDate: "2026-10-01",
  appointmentTime: "10:30:00",
  reason: "Routine check-up",
  notes: "Bring previous reports",
  status: "BOOKED",
  createdAt: "2026-09-01T10:00:00",
  updatedAt: "2026-09-01T10:00:00",
  ...overrides,
});

const defaultProps = {
  isLoading: false,
  isError: false,
  error: null,
  isAdmin: false,
  onRefetch: vi.fn(),
  onView: vi.fn(),
  onEdit: vi.fn(),
  onDelete: vi.fn(),
};

describe("AppointmentTable", () => {
  it("should render appointment rows and headers", () => {
    render(
      <AppointmentTable
        {...defaultProps}
        appointments={[buildAppointment()]}
      />,
    );

    expect(
      screen.getByRole("columnheader", { name: "Patient" }),
    ).toBeInTheDocument();
    expect(screen.getByText("Jane Doe")).toBeInTheDocument();
    expect(screen.getByText("John Smith")).toBeInTheDocument();
    expect(screen.getByText("Routine check-up")).toBeInTheDocument();
    expect(screen.getByText("BOOKED")).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: "View appointment 1" }),
    ).toBeInTheDocument();
  });

  it("should show a loading indicator while loading", () => {
    render(<AppointmentTable {...defaultProps} appointments={[]} isLoading />);

    expect(screen.getByRole("progressbar")).toBeInTheDocument();
    expect(screen.queryByText("No appointments found")).not.toBeInTheDocument();
    expect(
      screen.queryByRole("button", { name: /next page/i }),
    ).not.toBeInTheDocument();
  });

  it("should show the API error and retry action", async () => {
    const user = userEvent.setup();
    const onRefetch = vi.fn();

    render(
      <AppointmentTable
        {...defaultProps}
        appointments={[]}
        isError
        error={{ status: 500, data: { message: "Appointments unavailable" } }}
        onRefetch={onRefetch}
      />,
    );

    expect(screen.getByText("Appointments unavailable")).toBeInTheDocument();
    await user.click(screen.getByRole("button", { name: "Retry" }));

    expect(onRefetch).toHaveBeenCalledTimes(1);
    expect(
      screen.queryByRole("button", { name: /next page/i }),
    ).not.toBeInTheDocument();
  });

  it("should show an empty state when there are no appointments", () => {
    render(<AppointmentTable {...defaultProps} appointments={[]} />);

    expect(screen.getByText("No appointments found")).toBeInTheDocument();
  });

  it("should filter appointments by search text", async () => {
    const user = userEvent.setup();

    render(
      <AppointmentTable
        {...defaultProps}
        appointments={[
          buildAppointment(),
          buildAppointment({
            id: 2,
            patientName: "Alice Brown",
            doctorName: "Mary Jones",
            reason: "Dental consultation",
          }),
        ]}
      />,
    );

    await user.type(
      screen.getByPlaceholderText(
        "Search by patient, doctor, reason or status",
      ),
      "Alice",
    );

    expect(screen.getByText("Alice Brown")).toBeInTheDocument();
    expect(screen.queryByText("Jane Doe")).not.toBeInTheDocument();
  });

  it("should sort appointments by date when the Date header is clicked", async () => {
    const user = userEvent.setup();

    render(
      <AppointmentTable
        {...defaultProps}
        appointments={[
          buildAppointment({ id: 1, appointmentDate: "2026-10-01" }),
          buildAppointment({
            id: 2,
            patientName: "Alice Brown",
            appointmentDate: "2026-09-01",
          }),
        ]}
      />,
    );

    const dateHeader = screen.getByRole("button", { name: "Date" });
    const rows = () => screen.getAllByRole("row").slice(1);

    expect(rows()[0]).toHaveTextContent("Alice Brown");

    await user.click(dateHeader);

    expect(rows()[0]).toHaveTextContent("Jane Doe");
  });

  it("should expose edit and delete actions only to admins", async () => {
    const user = userEvent.setup();
    const appointment = buildAppointment();
    const onView = vi.fn();
    const onEdit = vi.fn();
    const onDelete = vi.fn();

    const { rerender } = render(
      <AppointmentTable
        {...defaultProps}
        appointments={[appointment]}
        onView={onView}
        onEdit={onEdit}
        onDelete={onDelete}
      />,
    );

    expect(
      screen.queryByRole("button", { name: "Edit appointment 1" }),
    ).not.toBeInTheDocument();
    expect(
      screen.queryByRole("button", { name: "Delete appointment 1" }),
    ).not.toBeInTheDocument();

    rerender(
      <AppointmentTable
        {...defaultProps}
        appointments={[appointment]}
        isAdmin
        onView={onView}
        onEdit={onEdit}
        onDelete={onDelete}
      />,
    );

    await user.click(
      screen.getByRole("button", { name: "View appointment 1" }),
    );
    await user.click(
      screen.getByRole("button", { name: "Edit appointment 1" }),
    );
    await user.click(
      screen.getByRole("button", { name: "Delete appointment 1" }),
    );

    expect(onView).toHaveBeenCalledWith(appointment);
    expect(onEdit).toHaveBeenCalledWith(appointment);
    expect(onDelete).toHaveBeenCalledWith(appointment);
  });

  it("should paginate appointments", () => {
    const appointments = Array.from({ length: 6 }, (_, index) =>
      buildAppointment({
        id: index + 1,
        patientName: `Patient ${index + 1}`,
        appointmentDate: `2026-10-${String(index + 1).padStart(2, "0")}`,
      }),
    );

    render(<AppointmentTable {...defaultProps} appointments={appointments} />);

    expect(screen.getByText("Patient 1")).toBeInTheDocument();
    expect(screen.queryByText("Patient 6")).not.toBeInTheDocument();
    expect(screen.getByText("1–5 of 6")).toBeInTheDocument();

    fireEvent.click(screen.getByRole("button", { name: "Go to next page" }));

    expect(screen.getByText("Patient 6")).toBeInTheDocument();
    expect(screen.getByText("6–6 of 6")).toBeInTheDocument();
  });
});
