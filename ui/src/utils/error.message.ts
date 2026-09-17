export const getApiErrorMessage = (error: unknown): string => {
  const defaultMessage = "An error occurred. Please try again.";

  if (!error || typeof error !== "object") {
    return defaultMessage;
  }

  const err = error as Record<string, unknown>;
  const data = err.data as Record<string, unknown> | undefined;

  if (typeof data?.message === "string") {
    return data.message;
  }

  if (typeof err.message === "string") {
    return err.message;
  }

  return defaultMessage;
};
