package com.hams.hospital_appointment_system.modules.user.mapper;

import com.hams.hospital_appointment_system.module.user.dto.UserRequest;
import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import com.hams.hospital_appointment_system.module.user.entity.Role;
import com.hams.hospital_appointment_system.module.user.entity.User;
import com.hams.hospital_appointment_system.module.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

        @Test
        void toEntity_shouldMapRequestFieldsToEntity() {

                UserRequest request = UserRequest.builder()
                                .username("johndoe")
                                .password("secret")
                                .build();

                User user = UserMapper.toEntity(request);

                assertThat(user.getId()).isNull();
                assertThat(user.getUsername()).isEqualTo("johndoe");
                assertThat(user.getPassword()).isEqualTo("secret");
                assertThat(user.getRole()).isNull();
        }

        @Test
        void toDto_shouldMapEntityFieldsToResponse() {

                Role role = Role.builder()
                                .id(1L)
                                .name("ADMIN")
                                .build();

                User user = User.builder()
                                .id(1L)
                                .username("johndoe")
                                .password("secret")
                                .role(role)
                                .build();

                UserResponse response = UserMapper.toDto(user);

                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getUsername()).isEqualTo("johndoe");
                assertThat(response.getRole()).isEqualTo("ADMIN");
        }
}
