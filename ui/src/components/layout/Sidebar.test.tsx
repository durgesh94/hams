import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { MemoryRouter } from "react-router-dom";

import Sidebar from "./Sidebar";

describe("Sidebar", () => {
  it("should render the brand and all navigation links", () => {
    render(
      <MemoryRouter initialEntries={["/dashboard"]}>
        <Sidebar mobileOpen={false} onClose={vi.fn()} />
      </MemoryRouter>,
    );

    expect(screen.getAllByText("HAMS")).toHaveLength(1);

    expect(screen.getAllByRole("link", { name: /Dashboard/ })).toHaveLength(1);
    expect(screen.getAllByRole("link", { name: /Doctors/ })).toHaveLength(1);
    expect(screen.getAllByRole("link", { name: /Patients/ })).toHaveLength(1);
    expect(screen.getAllByRole("link", { name: /Appointments/ })).toHaveLength(
      1,
    );
  });

  it("should point navigation links to the expected routes", () => {
    render(
      <MemoryRouter>
        <Sidebar mobileOpen={false} onClose={vi.fn()} />
      </MemoryRouter>,
    );

    expect(
      screen.getAllByRole("link", { name: /Dashboard/ })[0],
    ).toHaveAttribute("href", "/dashboard");
    expect(screen.getAllByRole("link", { name: /Doctors/ })[0]).toHaveAttribute(
      "href",
      "/doctors",
    );
    expect(
      screen.getAllByRole("link", { name: /Patients/ })[0],
    ).toHaveAttribute("href", "/patients");
    expect(
      screen.getAllByRole("link", { name: /Appointments/ })[0],
    ).toHaveAttribute("href", "/appointments");
  });

  it("should mark the current route as active", () => {
    render(
      <MemoryRouter initialEntries={["/patients"]}>
        <Sidebar mobileOpen={false} onClose={vi.fn()} />
      </MemoryRouter>,
    );

    const patientLinks = screen.getAllByRole("link", { name: /Patients/ });

    expect(patientLinks[0]).toHaveClass("active");
    expect(screen.getAllByRole("link", { name: /Doctors/ })[0]).not.toHaveClass(
      "active",
    );
  });

  it("should call onClose when a navigation link is clicked", async () => {
    const user = userEvent.setup();
    const onClose = vi.fn();

    render(
      <MemoryRouter>
        <Sidebar mobileOpen onClose={onClose} />
      </MemoryRouter>,
    );

    await user.click(screen.getByRole("link", { name: /Appointments/ }));

    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it("should not mount the mobile drawer when mobileOpen is false", () => {
    render(
      <MemoryRouter>
        <Sidebar mobileOpen={false} onClose={vi.fn()} />
      </MemoryRouter>,
    );

    expect(screen.queryByRole("presentation")).not.toBeInTheDocument();
  });
});
