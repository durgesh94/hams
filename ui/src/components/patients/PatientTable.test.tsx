import { fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";

import PatientTable from "./PatientTable";
import type { Patient } from "../../features/patients/types";

const buildPatient = (overrides: Partial<Patient> = {}): Patient => ({
  id: 1,
  firstName: "Jane",
  lastName: "Doe",
  dateOfBirth: "1990-05-20",
  gender: "FEMALE",
  email: "jane.doe@test.com",
  phone: "9876543210",
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

describe("PatientTable", () => {
  it("should render patient rows and details", () => {
    render(<PatientTable {...defaultProps} patients={[buildPatient()]} />);

    expect(
      screen.getByRole("columnheader", { name: "Patient Name" }),
    ).toBeInTheDocument();
    expect(screen.getByText("Jane Doe")).toBeInTheDocument();
    expect(screen.getByText("FEMALE")).toBeInTheDocument();
    expect(screen.getByText("1990-05-20")).toBeInTheDocument();
    expect(screen.getByText("jane.doe@test.com")).toBeInTheDocument();
    expect(
      screen.getByRole("button", { name: "View Jane" }),
    ).toBeInTheDocument();
  });

  it("should show a loading indicator while loading", () => {
    render(<PatientTable {...defaultProps} patients={[]} isLoading />);

    expect(screen.getByRole("progressbar")).toBeInTheDocument();
    expect(screen.queryByText("No patients found")).not.toBeInTheDocument();
    expect(
      screen.queryByRole("button", { name: /next page/i }),
    ).not.toBeInTheDocument();
  });

  it("should show the API error and retry action", async () => {
    const user = userEvent.setup();
    const onRefetch = vi.fn();

    render(
      <PatientTable
        {...defaultProps}
        patients={[]}
        isError
        error={{ status: 500, data: { message: "Patients unavailable" } }}
        onRefetch={onRefetch}
      />,
    );

    expect(screen.getByText("Patients unavailable")).toBeInTheDocument();
    await user.click(screen.getByRole("button", { name: "Retry" }));

    expect(onRefetch).toHaveBeenCalledTimes(1);
    expect(
      screen.queryByRole("button", { name: /next page/i }),
    ).not.toBeInTheDocument();
  });

  it("should show an empty state when there are no patients", () => {
    render(<PatientTable {...defaultProps} patients={[]} />);

    expect(screen.getByText("No patients found")).toBeInTheDocument();
  });

  it("should filter patients by name, email or phone", async () => {
    const user = userEvent.setup();

    render(
      <PatientTable
        {...defaultProps}
        patients={[
          buildPatient(),
          buildPatient({
            id: 2,
            firstName: "Alice",
            lastName: "Brown",
            email: "alice.brown@test.com",
            phone: "9876543211",
          }),
        ]}
      />,
    );

    await user.type(
      screen.getByPlaceholderText("Search by name, phone or email"),
      "alice.brown",
    );

    expect(screen.getByText("Alice Brown")).toBeInTheDocument();
    expect(screen.queryByText("Jane Doe")).not.toBeInTheDocument();
  });

  it("should expose edit and delete actions only to admins", async () => {
    const user = userEvent.setup();
    const patient = buildPatient();
    const onView = vi.fn();
    const onEdit = vi.fn();
    const onDelete = vi.fn();

    const { rerender } = render(
      <PatientTable
        {...defaultProps}
        patients={[patient]}
        onView={onView}
        onEdit={onEdit}
        onDelete={onDelete}
      />,
    );

    expect(
      screen.queryByRole("button", { name: "Edit Jane" }),
    ).not.toBeInTheDocument();
    expect(
      screen.queryByRole("button", { name: "Delete Jane" }),
    ).not.toBeInTheDocument();

    rerender(
      <PatientTable
        {...defaultProps}
        patients={[patient]}
        isAdmin
        onView={onView}
        onEdit={onEdit}
        onDelete={onDelete}
      />,
    );

    await user.click(screen.getByRole("button", { name: "View Jane" }));
    await user.click(screen.getByRole("button", { name: "Edit Jane" }));
    await user.click(screen.getByRole("button", { name: "Delete Jane" }));

    expect(onView).toHaveBeenCalledWith(patient);
    expect(onEdit).toHaveBeenCalledWith(patient);
    expect(onDelete).toHaveBeenCalledWith(patient);
  });

  it("should paginate patients", () => {
    const patients = Array.from({ length: 6 }, (_, index) =>
      buildPatient({
        id: index + 1,
        firstName: `Patient${index + 1}`,
        lastName: "Test",
      }),
    );

    render(<PatientTable {...defaultProps} patients={patients} />);

    expect(screen.getByText("Patient1 Test")).toBeInTheDocument();
    expect(screen.queryByText("Patient6 Test")).not.toBeInTheDocument();
    expect(screen.getByText("1–5 of 6")).toBeInTheDocument();

    fireEvent.click(screen.getByRole("button", { name: "Go to next page" }));

    expect(screen.getByText("Patient6 Test")).toBeInTheDocument();
    expect(screen.getByText("6–6 of 6")).toBeInTheDocument();
  });
});
