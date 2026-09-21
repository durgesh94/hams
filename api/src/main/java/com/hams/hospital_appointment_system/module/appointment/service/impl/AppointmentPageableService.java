package com.hams.hospital_appointment_system.module.appointment.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AppointmentPageableService {
  public Pageable create(Pageable pageable) {

    Sort.Direction direction =
        pageable.getSort().getOrderFor("appointmentDate") != null
            ? pageable.getSort().getOrderFor("appointmentDate").getDirection()
            : Sort.Direction.ASC;

    Sort sort =
        Sort.by(
            new Sort.Order(direction, "appointmentDate"),
            new Sort.Order(direction, "appointmentTime"));

    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }
}
