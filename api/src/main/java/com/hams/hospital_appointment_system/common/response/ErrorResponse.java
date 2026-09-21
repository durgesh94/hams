package com.hams.hospital_appointment_system.common.response;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

  private int status;
  private String message;
  private Map<String, String> errors;
  private LocalDateTime timestamp;
}
