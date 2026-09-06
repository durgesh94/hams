import { useEffect, type ReactNode } from "react";

import { useAppDispatch, useAppSelector } from "../../app/hooks";

import { selectToken, selectUser } from "./authSelectors";

import { logout, setCredentials } from "./authSlice";

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

  if (token && !user && isLoading) {
    return null;
  }

  return children;
};

export default AuthInitializer;
