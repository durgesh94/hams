import type { FetchBaseQueryError } from "@reduxjs/toolkit/query";
import type { ApiErrorResponse } from "./types";

export const getApiErrorMessage = (
  error: FetchBaseQueryError | unknown,
): string => {
  if (!error) {
    return "Something went wrong.";
  }

  if (typeof error === "object" && error !== null && "status" in error) {
    const apiError = error as FetchBaseQueryError;

    // Backend returned an HTTP response
    if (typeof apiError.data === "object" && apiError.data !== null) {
      const data = apiError.data as Partial<ApiErrorResponse>;

      if (data.message) {
        return data.message;
      }
    }
  }

  return "Something went wrong. Please try again.";
};
