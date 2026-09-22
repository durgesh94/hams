import { fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";

import DoctorTable from "./DoctorTable";
import type { Doctor } from "../../features/doctors/types";

const buildDoctor = (overrides: Partial<Doctor> = {}): Doctor => ({
  id: 1,
  firstName: "John",
  lastName: "Smith",
  specialization: "Cardiology",
  qualification: "MBBS, MD",
  experienceYears: 10,
  gender: "MALE",
  phone: "9876543210",
  email: "john.smith@test.com",
  status: "ACTIVE",
  appointmentCount: 4,
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

describe("DoctorTable", () => {
  it("should render doctor rows and appointment count", () => {
    render(<DoctorTable {...defaultProps} doctors={[buildDoctor()]} />);

    expect(
      screen.getByRole("columnheader", { name: "Doctor Name" }),
    ).toBeInTheDocument();
    expect(screen.getByText("John Smith")).toBeInTheDocument();
    expect(screen.getByText("Cardiology")).toBeInTheDocument();
    expect(screen.getByText("ACTIVE")).toBeInTheDocument();
    expect(screen.getByText("4")).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: "View John" }),
    ).toBeInTheDocument();
  });

  it("should show a loading indicator while loading", () => {
    render(<DoctorTable {...defaultProps} doctors={[]} isLoading />);

    expect(screen.getByRole("progressbar")).toBeInTheDocument();
    expect(screen.queryByText("No doctors found")).not.toBeInTheDocument();
    expect(
      screen.queryByRole("button", { name: /next page/i }),
    ).not.toBeInTheDocument();
  });

  it("should show the API error and retry action", async () => {
    const user = userEvent.setup();
    const onRefetch = vi.fn();

    render(
      <DoctorTable
        {...defaultProps}
        doctors={[]}
        isError
        error={{ status: 500, data: { message: "Doctors unavailable" } }}
        onRefetch={onRefetch}
      />,
    );

    expect(screen.getByText("Doctors unavailable")).toBeInTheDocument();
    await user.click(screen.getByRole("button", { name: "Retry" }));

    expect(onRefetch).toHaveBeenCalledTimes(1);
    expect(
      screen.queryByRole("button", { name: /next page/i }),
    ).not.toBeInTheDocument();
  });

  it("should show an empty state when there are no doctors", () => {
    render(<DoctorTable {...defaultProps} doctors={[]} />);

    expect(screen.getByText("No doctors found")).toBeInTheDocument();
  });

  it("should filter doctors by name, specialization, qualification or phone", async () => {
    const user = userEvent.setup();

    render(
      <DoctorTable
        {...defaultProps}
        doctors={[
          buildDoctor(),
          buildDoctor({
            id: 2,
            firstName: "Mary",
            lastName: "Jones",
            specialization: "Neurology",
            qualification: "MBBS, DM",
            phone: "9876543211",
          }),
        ]}
      />,
    );

    await user.type(
      screen.getByPlaceholderText(
        "Search by name, specialization, qualification or phone",
      ),
      "Neurology",
    );

    expect(screen.getByText("Mary Jones")).toBeInTheDocument();
    expect(screen.queryByText("John Smith")).not.toBeInTheDocument();
  });

  it("should expose edit and delete actions only to admins", async () => {
    const user = userEvent.setup();
    const doctor = buildDoctor();
    const onView = vi.fn();
    const onEdit = vi.fn();
    const onDelete = vi.fn();

    const { rerender } = render(
      <DoctorTable
        {...defaultProps}
        doctors={[doctor]}
        onView={onView}
        onEdit={onEdit}
        onDelete={onDelete}
      />,
    );

    expect(
      screen.queryByRole("button", { name: "Edit John" }),
    ).not.toBeInTheDocument();
    expect(
      screen.queryByRole("button", { name: "Delete John" }),
    ).not.toBeInTheDocument();

    rerender(
      <DoctorTable
        {...defaultProps}
        doctors={[doctor]}
        isAdmin
        onView={onView}
        onEdit={onEdit}
        onDelete={onDelete}
      />,
    );

    await user.click(screen.getByRole("button", { name: "View John" }));
    await user.click(screen.getByRole("button", { name: "Edit John" }));
    await user.click(screen.getByRole("button", { name: "Delete John" }));

    expect(onView).toHaveBeenCalledWith(doctor);
    expect(onEdit).toHaveBeenCalledWith(doctor);
    expect(onDelete).toHaveBeenCalledWith(doctor);
  });

  it("should paginate doctors", () => {
    const doctors = Array.from({ length: 6 }, (_, index) =>
      buildDoctor({
        id: index + 1,
        firstName: `Doctor${index + 1}`,
        lastName: "Test",
      }),
    );

    render(<DoctorTable {...defaultProps} doctors={doctors} />);

    expect(screen.getByText("Doctor1 Test")).toBeInTheDocument();
    expect(screen.queryByText("Doctor6 Test")).not.toBeInTheDocument();
    expect(screen.getByText("1–5 of 6")).toBeInTheDocument();

    fireEvent.click(screen.getByRole("button", { name: "Go to next page" }));

    expect(screen.getByText("Doctor6 Test")).toBeInTheDocument();
    expect(screen.getByText("6–6 of 6")).toBeInTheDocument();
  });
});
