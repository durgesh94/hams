package com.hams.hospital_appointment_system.modules.user.service;

import com.hams.hospital_appointment_system.common.exception.ResourceNotFoundException;
import com.hams.hospital_appointment_system.module.user.dto.UserResponse;
import com.hams.hospital_appointment_system.module.user.entity.Role;
import com.hams.hospital_appointment_system.module.user.entity.User;
import com.hams.hospital_appointment_system.module.user.repository.UserRepository;
import com.hams.hospital_appointment_system.module.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UserServiceImpl userService;

        @Test
        void getUserById_shouldReturnUser_whenUserExists() {

                Role role = Role.builder().id(1L).name("ADMIN").build();
                User user = User.builder()
                                .id(1L)
                                .username("johndoe")
                                .password("secret")
                                .role(role)
                                .build();

                when(userRepository.findById(1L)).thenReturn(Optional.of(user));

                UserResponse response = userService.getUserById(1L);

                assertThat(response.getId()).isEqualTo(1L);
                assertThat(response.getUsername()).isEqualTo("johndoe");
                assertThat(response.getRole()).isEqualTo("ADMIN");
                verify(userRepository).findById(1L);
        }

        @Test
        void getUserById_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {

                when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUserById(999L))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessageContaining("User not found with id 999");
        }

        @Test
        void getUserByUsername_shouldReturnUser_whenUserExists() {

                Role role = Role.builder().id(1L).name("PATIENT").build();
                User user = User.builder()
                                .id(2L)
                                .username("janedoe")
                                .password("secret")
                                .role(role)
                                .build();

                when(userRepository.findByUsername("janedoe")).thenReturn(Optional.of(user));

                UserResponse response = userService.getUserByUsername("janedoe");

                assertThat(response.getId()).isEqualTo(2L);
                assertThat(response.getUsername()).isEqualTo("janedoe");
                assertThat(response.getRole()).isEqualTo("PATIENT");
                verify(userRepository).findByUsername("janedoe");
        }

        @Test
        void getUserByUsername_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {

                when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

                assertThatThrownBy(() -> userService.getUserByUsername("unknown"))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessageContaining("User not found with username unknown");
        }

        @Test
        void getAllUsers_shouldReturnAllUsers() {

                Role role = Role.builder().id(1L).name("ADMIN").build();
                User user1 = User.builder().id(1L).username("john").password("pass1").role(role).build();
                User user2 = User.builder().id(2L).username("jane").password("pass2").role(role).build();

                when(userRepository.findAll()).thenReturn(List.of(user1, user2));

                List<UserResponse> responses = userService.getAllUsers();

                assertThat(responses).hasSize(2);
                assertThat(responses)
                                .extracting(UserResponse::getUsername)
                                .containsExactly("john", "jane");
                verify(userRepository).findAll();
        }

        @Test
        void getAllUsers_shouldReturnEmptyList_whenNoUsersExist() {

                when(userRepository.findAll()).thenReturn(List.of());

                List<UserResponse> responses = userService.getAllUsers();

                assertThat(responses).isEmpty();
        }
}
