import { fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import ConfirmDialog from "./ConfirmDialog";

describe("ConfirmDialog", () => {
  it("should render title, message and default button texts", () => {
    render(
      <ConfirmDialog
        open
        title="Delete Doctor"
        message="Are you sure you want to delete this doctor?"
        onClose={vi.fn()}
        onConfirm={vi.fn()}
        disableRestoreFocus
      />,
    );

    expect(
      screen.getByRole("heading", { name: "Delete Doctor" }),
    ).toBeInTheDocument();

    expect(
      screen.getByText("Are you sure you want to delete this doctor?"),
    ).toBeInTheDocument();

    expect(screen.getByRole("button", { name: "Cancel" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Confirm" })).toBeInTheDocument();
  });

  it("should call onClose when Cancel is clicked", async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    render(
      <ConfirmDialog
        open
        title="Delete Doctor"
        message="Are you sure?"
        onClose={onClose}
        onConfirm={vi.fn()}
        disableRestoreFocus
      />,
    );

    await user.click(screen.getByRole("button", { name: "Cancel" }));

    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it("should call onConfirm when Confirm is clicked", async () => {
    const user = userEvent.setup();
    const onConfirm = vi.fn();

    render(
      <ConfirmDialog
        open
        title="Delete Doctor"
        message="Are you sure?"
        onClose={vi.fn()}
        onConfirm={onConfirm}
        disableRestoreFocus
      />,
    );

    await user.click(screen.getByRole("button", { name: "Confirm" }));

    expect(onConfirm).toHaveBeenCalledTimes(1);
  });

  it("should render custom confirm and cancel button text", () => {
    render(
      <ConfirmDialog
        open
        title="Delete Appointment"
        message="Are you sure?"
        onClose={vi.fn()}
        onConfirm={vi.fn()}
        confirmText="Delete"
        cancelText="Keep"
        disableRestoreFocus
      />,
    );

    expect(screen.getByRole("button", { name: "Keep" })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "Delete" })).toBeInTheDocument();

    expect(
      screen.queryByRole("button", { name: "Cancel" }),
    ).not.toBeInTheDocument();

    expect(
      screen.queryByRole("button", { name: "Confirm" }),
    ).not.toBeInTheDocument();
  });

  it("should disable both buttons and show loading text when loading", () => {
    const onClose = vi.fn();
    const onConfirm = vi.fn();

    render(
      <ConfirmDialog
        open
        title="Delete Doctor"
        message="Deleting doctor..."
        onClose={onClose}
        onConfirm={onConfirm}
        loading
        disableRestoreFocus
      />,
    );

    const cancelButton = screen.getByRole("button", { name: "Cancel" });
    const confirmButton = screen.getByRole("button", {
      name: "In Progress...",
    });

    expect(cancelButton).toBeDisabled();
    expect(confirmButton).toBeDisabled();
  });

  it("should not call callbacks when buttons are disabled", () => {
    const onClose = vi.fn();
    const onConfirm = vi.fn();

    render(
      <ConfirmDialog
        open
        title="Delete Doctor"
        message="Deleting doctor..."
        onClose={onClose}
        onConfirm={onConfirm}
        loading
        disableRestoreFocus
      />,
    );

    fireEvent.click(screen.getByRole("button", { name: "Cancel" }));
    fireEvent.click(screen.getByRole("button", { name: "In Progress..." }));

    expect(onClose).not.toHaveBeenCalled();
    expect(onConfirm).not.toHaveBeenCalled();
  });

  it("should not render when open is false", () => {
    render(
      <ConfirmDialog
        open={false}
        title="Delete Doctor"
        message="Are you sure?"
        onClose={vi.fn()}
        onConfirm={vi.fn()}
        disableRestoreFocus
      />,
    );

    expect(
      screen.queryByRole("heading", { name: "Delete Doctor" }),
    ).not.toBeInTheDocument();

    expect(screen.queryByText("Are you sure?")).not.toBeInTheDocument();
  });
});
