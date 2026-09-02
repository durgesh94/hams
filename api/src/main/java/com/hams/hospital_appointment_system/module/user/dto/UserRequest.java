package com.hams.hospital_appointment_system.module.user.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String username;
    private String password;
}
