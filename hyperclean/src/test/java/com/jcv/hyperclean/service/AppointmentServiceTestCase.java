package com.jcv.hyperclean.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppointmentServiceTestCase {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentServiceTestCase(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

}