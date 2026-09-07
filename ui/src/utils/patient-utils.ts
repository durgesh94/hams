export const getPatientFormId = (
  dialogContentId: number,
): string | undefined => {
  switch (dialogContentId) {
    case 1:
      return "add-patient-form";
    case 2:
      return "update-patient-form";
    default:
      return undefined;
  }
};

export const getDialogAction = (
  dialogContentId: number,
  handleDeleteConfirm: () => void,
  handleDialogClose: () => void,
): (() => void) | undefined => {
  switch (dialogContentId) {
    case 3:
      return handleDeleteConfirm;
    case 4:
      return handleDialogClose;
    default:
      return undefined;
  }
};

export const getDialogButtonLabel = (dialogContentId: number): string => {
  switch (dialogContentId) {
    case 3:
      return "Confirm";
    case 4:
      return "Ok";
    default:
      return "Save";
  }
};
