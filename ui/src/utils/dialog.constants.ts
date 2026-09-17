export const DialogContentId = {
  ADD: 1,
  UPDATE: 2,
  DELETE: 3,
  CLOSE: 4,
} as const;

export type DialogContentId =
  (typeof DialogContentId)[keyof typeof DialogContentId];