import { useEffect, type ReactNode } from "react";

import { useAppDispatch, useAppSelector } from "../../app/hooks";

import { selectToken, selectUser } from "./authSelectors";

import { logout, setCredentials } from "./authSlice";
import { authStorage } from "./authStorage";

import { useGetCurrentUserQuery } from "./authApi";

interface AuthInitializerProps {
  children: ReactNode;
}

const AuthInitializer = ({ children }: AuthInitializerProps) => {
  const dispatch = useAppDispatch();

  const token = useAppSelector(selectToken);
  const user = useAppSelector(selectUser);

  const {
    data: currentUser,
    isLoading,
    isError,
  } = useGetCurrentUserQuery(undefined, {
    skip: !token,
  });

  useEffect(() => {
    if (currentUser && token) {
      dispatch(
        setCredentials({
          token,
          user: currentUser,
        }),
      );
    }
  }, [currentUser, token, dispatch]);

  useEffect(() => {
    if (isError && token) {
      dispatch(logout());
    }
  }, [isError, token, dispatch]);

  useEffect(() => {
    if (!token) {
      return undefined;
    }

    const expirationTime = authStorage.getTokenExpirationTime(token);

    if (!expirationTime) {
      return undefined;
    }

    const timeUntilExpiration = expirationTime - Date.now();

    if (timeUntilExpiration <= 0) {
      dispatch(logout());
      return undefined;
    }

    const timeoutId = window.setTimeout(() => {
      dispatch(logout());
    }, timeUntilExpiration);

    return () => {
      window.clearTimeout(timeoutId);
    };
  }, [token, dispatch]);

  if (token && !user && isLoading) {
    return null;
  }

  return children;
};

export default AuthInitializer;
