export const getDoctorFormId = (
  dialogContentId: number,
): string | undefined => {
  switch (dialogContentId) {
    case 1:
      return "add-doctor-form";
    case 2:
      return "update-doctor-form";
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
