import { DialogContentId } from "./dialog.constants";

import type { DialogConfig, DialogEntity } from "./dialog.types";

export const getDialogConfig = (
  dialogContentId: DialogContentId,
  entity: DialogEntity,
): DialogConfig => {
  switch (dialogContentId) {
    case DialogContentId.ADD:
      return {
        formId: `add-${entity}-form`,
        buttonLabel: "Save",
      };

    case DialogContentId.UPDATE:
      return {
        formId: `update-${entity}-form`,
        buttonLabel: "Save",
      };

    case DialogContentId.DELETE:
      return {
        buttonLabel: "Confirm",
        action: "delete",
      };

    case DialogContentId.CLOSE:
      return {
        buttonLabel: "Ok",
        action: "close",
      };

    default:
      return {
        buttonLabel: "Save",
      };
  }
};
