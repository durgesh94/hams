import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { describe, expect, it, vi } from "vitest";

import AppLayout from "./AppLayout";

vi.mock("./Header", () => ({
  default: ({ onMenuClick }: { onMenuClick: () => void }) => (
    <header>
      <button type="button" aria-label="open navigation" onClick={onMenuClick}>
        Menu
      </button>
    </header>
  ),
}));

vi.mock("./Sidebar", () => ({
  default: ({
    mobileOpen,
    onClose,
  }: {
    mobileOpen: boolean;
    onClose: () => void;
  }) => (
    <aside data-testid="sidebar" data-open={String(mobileOpen)}>
      <button type="button" onClick={onClose}>
        Close sidebar
      </button>
    </aside>
  ),
}));

describe("AppLayout", () => {
  const renderLayout = () =>
    render(
      <MemoryRouter initialEntries={["/dashboard"]}>
        <Routes>
          <Route element={<AppLayout />}>
            <Route path="/dashboard" element={<div>Dashboard content</div>} />
          </Route>
        </Routes>
      </MemoryRouter>,
    );

  it("should render the header, sidebar and routed content", () => {
    renderLayout();

    expect(screen.getByRole("banner")).toBeInTheDocument();
    expect(screen.getByTestId("sidebar")).toBeInTheDocument();
    expect(screen.getByText("Dashboard content")).toBeInTheDocument();
    expect(screen.getByTestId("sidebar")).toHaveAttribute("data-open", "false");
  });

  it("should open the mobile sidebar when the menu is clicked", async () => {
    const user = userEvent.setup();
    renderLayout();

    await user.click(screen.getByRole("button", { name: "open navigation" }));

    expect(screen.getByTestId("sidebar")).toHaveAttribute("data-open", "true");
  });

  it("should close the mobile sidebar when the sidebar requests close", async () => {
    const user = userEvent.setup();
    renderLayout();

    await user.click(screen.getByRole("button", { name: "open navigation" }));
    await user.click(screen.getByRole("button", { name: "Close sidebar" }));

    expect(screen.getByTestId("sidebar")).toHaveAttribute("data-open", "false");
  });
});
