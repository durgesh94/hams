export const Qualifications = {
  MBBS: "MBBS",
  MD: "MD",
  DO: "DO",
  PhD: "PhD",
  MBChB: "MBChB",
  BDS: "BDS",
  MBChD: "MBChD",
  DM: "DM",
} as const;

export type Qualification = (typeof Qualifications)[keyof typeof Qualifications];