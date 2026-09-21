package com.hams.hospital_appointment_system.common.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

  private int status;
  private String message;
  private T data;
  private PaginationResponse pagination;
  private LocalDateTime timestamp;
}
