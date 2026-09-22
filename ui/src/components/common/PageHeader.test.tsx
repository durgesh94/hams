import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";

import PageHeader from "./PageHeader";

describe("PageHeader", () => {
  it("should render the title and subtitle", () => {
    render(
      <PageHeader title="Doctors" subtitle="Manage hospital doctors" isAdmin />,
    );

    expect(
      screen.getByRole("heading", { name: "Doctors" }),
    ).toBeInTheDocument();
    expect(screen.getByText("Manage hospital doctors")).toBeInTheDocument();
  });

  it("should not render a subtitle when it is not provided", () => {
    render(<PageHeader title="Patients" />);

    expect(
      screen.getByRole("heading", { name: "Patients" }),
    ).toBeInTheDocument();
    expect(
      screen.queryByText("Manage hospital patients"),
    ).not.toBeInTheDocument();
  });

  it("should render the action button for an admin and call its handler", async () => {
    const user = userEvent.setup();
    const handleAction = vi.fn();

    render(
      <PageHeader
        title="Doctors"
        actionLabel="Add Doctor"
        handleAction={handleAction}
        isAdmin
      />,
    );

    await user.click(screen.getByRole("button", { name: "Add Doctor" }));

    expect(handleAction).toHaveBeenCalledTimes(1);
  });

  it("should hide the action button for non-admin users", () => {
    render(
      <PageHeader
        title="Doctors"
        actionLabel="Add Doctor"
        handleAction={vi.fn()}
        isAdmin={false}
      />,
    );

    expect(
      screen.queryByRole("button", { name: "Add Doctor" }),
    ).not.toBeInTheDocument();
  });

  it("should render right-side content", () => {
    render(
      <PageHeader
        title="Appointments"
        rightContent={<span>Filter controls</span>}
      />,
    );

    expect(screen.getByText("Filter controls")).toBeInTheDocument();
  });
});
