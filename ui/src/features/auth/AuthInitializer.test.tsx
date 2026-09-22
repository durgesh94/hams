import { render, screen } from "@testing-library/react";
import { Provider } from "react-redux";
import { configureStore } from "@reduxjs/toolkit";
import { beforeEach, describe, expect, it, vi } from "vitest";

import AuthInitializer from "./AuthInitializer";
import authReducer from "./authSlice";
import { useGetCurrentUserQuery } from "./authApi";
import { authStorage } from "./authStorage";
import type { User } from "./types";

vi.mock("./authApi", () => ({
  useGetCurrentUserQuery: vi.fn(),
}));

const mockedUseGetCurrentUserQuery = vi.mocked(useGetCurrentUserQuery);

const mockCurrentUserQuery = (result: {
  data?: User;
  isLoading: boolean;
  isError: boolean;
}) => {
  mockedUseGetCurrentUserQuery.mockReturnValue(
    result as unknown as ReturnType<typeof useGetCurrentUserQuery>,
  );
};

const currentUser = {
  username: "john.doe",
  role: "ADMIN",
};

const createStore = (token: string | null, user: typeof currentUser | null) =>
  configureStore({
    reducer: {
      auth: authReducer,
    },
    preloadedState: {
      auth: {
        token,
        user,
        role: user?.role ?? null,
        isAuthenticated: Boolean(token && user),
      },
    },
  });

const renderInitializer = (
  token: string | null,
  user: typeof currentUser | null,
) => {
  const store = createStore(token, user);

  return {
    store,
    ...render(
      <Provider store={store}>
        <AuthInitializer>
          <div>Protected content</div>
        </AuthInitializer>
      </Provider>,
    ),
  };
};

describe("AuthInitializer", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mockCurrentUserQuery({
      data: undefined,
      isLoading: false,
      isError: false,
    });
    vi.spyOn(authStorage, "getTokenExpirationTime").mockReturnValue(null);
  });

  it("should render children immediately when no token exists", () => {
    renderInitializer(null, null);

    expect(screen.getByText("Protected content")).toBeInTheDocument();
    expect(mockedUseGetCurrentUserQuery).toHaveBeenCalledWith(undefined, {
      skip: true,
    });
  });

  it("should render nothing while loading the current user for a token", () => {
    mockCurrentUserQuery({
      data: undefined,
      isLoading: true,
      isError: false,
    });

    renderInitializer("valid-token", null);

    expect(screen.queryByText("Protected content")).not.toBeInTheDocument();
    expect(mockedUseGetCurrentUserQuery).toHaveBeenCalledWith(undefined, {
      skip: false,
    });
  });

  it("should store the current user when the token is valid", () => {
    mockCurrentUserQuery({
      data: currentUser,
      isLoading: false,
      isError: false,
    });

    const { store } = renderInitializer("valid-token", null);

    expect(store.getState().auth.user).toEqual(currentUser);
    expect(store.getState().auth.role).toBe("ADMIN");
    expect(store.getState().auth.isAuthenticated).toBe(true);
  });

  it("should logout when current user loading fails", () => {
    mockCurrentUserQuery({
      data: undefined,
      isLoading: false,
      isError: true,
    });

    const { store } = renderInitializer("invalid-token", currentUser);

    expect(store.getState().auth.token).toBeNull();
    expect(store.getState().auth.user).toBeNull();
    expect(store.getState().auth.isAuthenticated).toBe(false);
  });

  it("should logout when the token expires", () => {
    vi.useFakeTimers();
    vi.spyOn(Date, "now").mockReturnValue(1_000);
    vi.mocked(authStorage.getTokenExpirationTime).mockReturnValue(2_000);

    const { store } = renderInitializer("expiring-token", currentUser);

    expect(store.getState().auth.isAuthenticated).toBe(true);

    vi.advanceTimersByTime(1_000);

    expect(store.getState().auth.token).toBeNull();
    expect(store.getState().auth.isAuthenticated).toBe(false);

    vi.useRealTimers();
  });
});
