import { Alert, Snackbar } from "@mui/material";

const ToastMessage = ({
  toastOpen,
  toastMessage,
  toastSeverity,
  handleToastClose,
}: {
  toastOpen: boolean;
  toastMessage: string;
  toastSeverity: "success" | "error";
  handleToastClose: () => void;
}) => {
  return (
    <Snackbar
      open={toastOpen}
      autoHideDuration={6000}
      onClose={handleToastClose}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
    >
      <Alert
        onClose={handleToastClose}
        severity={toastSeverity}
        sx={{ width: "100%" }}
      >
        {toastMessage}
      </Alert>
    </Snackbar>
  );
};

export default ToastMessage;