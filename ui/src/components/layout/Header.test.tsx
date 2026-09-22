import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { Provider } from "react-redux";
import { MemoryRouter, useLocation } from "react-router-dom";
import { configureStore } from "@reduxjs/toolkit";
import { describe, expect, it, vi } from "vitest";

import Header from "./Header";
import authReducer from "../../features/auth/authSlice";

const user = {
  username: "admin.user",
  role: "ADMIN",
};

const createStore = () =>
  configureStore({
    reducer: {
      auth: authReducer,
    },
    preloadedState: {
      auth: {
        token: "test-token",
        user,
        role: user.role,
        isAuthenticated: true,
      },
    },
  });

const LocationDisplay = () => {
  const location = useLocation();
  return <span data-testid="location">{location.pathname}</span>;
};

const renderHeader = (onMenuClick = vi.fn()) => {
  const store = createStore();

  return {
    store,
    ...render(
      <Provider store={store}>
        <MemoryRouter>
          <Header onMenuClick={onMenuClick} />
          <LocationDisplay />
        </MemoryRouter>
      </Provider>,
    ),
  };
};

describe("Header", () => {
  it("should render the application title and authenticated user details", () => {
    renderHeader();

    expect(
      screen.getByText("Hospital Appointment Management System"),
    ).toBeInTheDocument();
    expect(screen.getByText("admin.user")).toBeInTheDocument();
    expect(screen.getByText("ADMIN")).toBeInTheDocument();
    expect(screen.getByText("a")).toBeInTheDocument();
  });

  it("should call onMenuClick when the navigation menu button is clicked", async () => {
    const userEventSetup = userEvent.setup();
    const onMenuClick = vi.fn();
    renderHeader(onMenuClick);

    await userEventSetup.click(
      screen.getByRole("button", { name: "open navigation" }),
    );

    expect(onMenuClick).toHaveBeenCalledTimes(1);
  });

  it("should open the logout confirmation dialog", async () => {
    const userEventSetup = userEvent.setup();
    renderHeader();

    await userEventSetup.click(screen.getByRole("button", { name: "logout" }));

    expect(screen.getByRole("dialog")).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: "Confirmation" }),
    ).toBeInTheDocument();
    expect(
      screen.getByText("Are you sure you want to logout?"),
    ).toBeInTheDocument();
  });

  it("should close the logout confirmation without logging out", async () => {
    const userEventSetup = userEvent.setup();
    const { store } = renderHeader();

    await userEventSetup.click(screen.getByRole("button", { name: "logout" }));
    await userEventSetup.click(screen.getByRole("button", { name: "Cancel" }));

    await waitFor(() => {
      expect(screen.queryByRole("dialog")).not.toBeInTheDocument();
    });
    expect(store.getState().auth.isAuthenticated).toBe(true);
  });

  it("should logout and navigate to login when confirmed", async () => {
    const userEventSetup = userEvent.setup();
    const { store } = renderHeader();

    await userEventSetup.click(screen.getByRole("button", { name: "logout" }));
    await userEventSetup.click(screen.getByRole("button", { name: "Confirm" }));

    expect(store.getState().auth.isAuthenticated).toBe(false);
    expect(store.getState().auth.user).toBeNull();
    expect(screen.getByTestId("location")).toHaveTextContent("/login");
  });
});
