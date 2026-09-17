export type DialogEntity = "doctor" | "patient" | "appointment";

export interface DialogConfig {
  formId?: string;
  buttonLabel: string;
  action?: "delete" | "close";
}
