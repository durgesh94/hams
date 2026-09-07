import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import type { ReactNode } from "react";

interface AppDialogProps {
  open: boolean;
  title: string;
  children: ReactNode;
  onClose: () => void;
  onSubmit?: () => void;
  submitText?: string;
  isSubmitting?: boolean;
  formId?: string;
}

const AppDialog = ({
  open,
  title,
  children,
  onClose,
  onSubmit,
  submitText = "Save",
  isSubmitting = false,
  formId,
}: AppDialogProps) => {
  return (
    <Dialog
      open={open}
      onClose={isSubmitting ? undefined : onClose}
      fullWidth
      maxWidth="sm"
    >
      <DialogTitle>{title}</DialogTitle>

      <DialogContent dividers>{children}</DialogContent>

      <DialogActions>
        <Button onClick={onClose} disabled={isSubmitting}>
          Cancel
        </Button>

        <Button
          variant="contained"
          onClick={onSubmit}
          disabled={isSubmitting}
          form={formId}
          type={formId ? "submit" : "button"}
        >
          {isSubmitting ? "In progress..." : submitText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default AppDialog;
