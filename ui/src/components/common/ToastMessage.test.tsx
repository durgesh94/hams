import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import ToastMessage from "./ToastMessage";

describe("ToastMessage", () => {
	it("should render the message when open", () => {
		render(
			<ToastMessage
				toastOpen
				toastMessage="Doctor added successfully"
				toastSeverity="success"
				handleToastClose={vi.fn()}
			/>,
		);

		expect(screen.getByRole("alert")).toBeInTheDocument();
		expect(screen.getByText("Doctor added successfully")).toBeInTheDocument();
	});

	it("should render the configured severity", () => {
		render(
			<ToastMessage
				toastOpen
				toastMessage="Unable to delete doctor"
				toastSeverity="error"
				handleToastClose={vi.fn()}
			/>,
		);

		expect(screen.getByRole("alert")).toHaveClass("MuiAlert-colorError");
	});

	it("should call handleToastClose when the alert close button is clicked", () => {
		const handleToastClose = vi.fn();

		render(
			<ToastMessage
				toastOpen
				toastMessage="Appointment saved"
				toastSeverity="success"
				handleToastClose={handleToastClose}
			/>,
		);

		fireEvent.click(screen.getByRole("button", { name: "Close" }));

		expect(handleToastClose).toHaveBeenCalledTimes(1);
	});

	it("should not render the message when closed", () => {
		render(
			<ToastMessage
				toastOpen={false}
				toastMessage="Hidden notification"
				toastSeverity="success"
				handleToastClose={vi.fn()}
			/>,
		);

		expect(screen.queryByRole("alert")).not.toBeInTheDocument();
		expect(screen.queryByText("Hidden notification")).not.toBeInTheDocument();
	});
});
