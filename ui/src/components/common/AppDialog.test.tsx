import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";

import AppDialog from "./AppDialog";

describe("AppDialog", () => {
  it("should render dialog with title, children and default submit text", () => {
    render(
      <AppDialog open title="Add Doctor" onClose={vi.fn()} disableRestoreFocus>
        <div>Doctor form</div>
      </AppDialog>,
    );

    expect(screen.getByRole("dialog")).toBeInTheDocument();

    expect(
      screen.getByRole("heading", {
        name: "Add Doctor",
      }),
    ).toBeInTheDocument();

    expect(screen.getByText("Doctor form")).toBeInTheDocument();

    expect(
      screen.getByRole("button", {
        name: "Save",
      }),
    ).toBeInTheDocument();

    expect(
      screen.getByRole("button", {
        name: "Cancel",
      }),
    ).toBeEnabled();
  });

  it("should call onClose when Cancel is clicked", async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    render(
      <AppDialog open title="Add Patient" onClose={onClose} disableRestoreFocus>
        <div>Patient form</div>
      </AppDialog>,
    );

    const cancelButton = screen.getByRole("button", {
      name: "Cancel",
    });

    await user.click(cancelButton);

    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it("should call onSubmit when submit button is clicked", async () => {
    const user = userEvent.setup();
    const onSubmit = vi.fn();

    render(
      <AppDialog
        open
        title="Add Doctor"
        onClose={vi.fn()}
        onSubmit={onSubmit}
        submitText="Create"
        disableRestoreFocus
      >
        <div>Doctor form</div>
      </AppDialog>,
    );

    const submitButton = screen.getByRole("button", {
      name: "Create",
    });

    expect(submitButton).toHaveAttribute("type", "button");

    await user.click(submitButton);

    expect(onSubmit).toHaveBeenCalledTimes(1);
  });

  it("should render submit button with form attributes when formId is provided", () => {
    render(
      <AppDialog
        open
        title="Add Appointment"
        onClose={vi.fn()}
        onSubmit={vi.fn()}
        submitText="Create Appointment"
        formId="appointment-form"
        disableRestoreFocus
      >
        <form id="appointment-form">
          <input aria-label="Doctor" />
        </form>
      </AppDialog>,
    );

    const submitButton = screen.getByRole("button", {
      name: "Create Appointment",
    });

    expect(submitButton).toHaveAttribute("form", "appointment-form");

    expect(submitButton).toHaveAttribute("type", "submit");
  });

  it("should show submitting state and disable buttons", () => {
    render(
      <AppDialog
        open
        title="Saving Doctor"
        onClose={vi.fn()}
        onSubmit={vi.fn()}
        isSubmitting
        submitText="Create"
        disableRestoreFocus
      >
        <div>Doctor form</div>
      </AppDialog>,
    );

    const cancelButton = screen.getByRole("button", {
      name: "Cancel",
    });

    const submitButton = screen.getByRole("button", {
      name: "In progress...",
    });

    expect(cancelButton).toBeDisabled();
    expect(submitButton).toBeDisabled();

    expect(submitButton).toHaveAttribute("type", "button");
  });

  it("should not render dialog when open is false", () => {
    render(
      <AppDialog
        open={false}
        title="Hidden Dialog"
        onClose={vi.fn()}
        disableRestoreFocus
      >
        <div>Hidden content</div>
      </AppDialog>,
    );

    expect(screen.queryByRole("dialog")).not.toBeInTheDocument();
  });
});
