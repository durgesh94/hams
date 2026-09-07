export interface ApiErrorResponse {
  status: number;
  message: string;
  errors: unknown;
  timestamp: string;
}

export interface ApiResponse<T> {
  status: number;
  message: string;
  data: T;
  timestamp: string;
}