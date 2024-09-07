package com.vasiu_catalina.beauty_salon.service;

import java.util.Set;

import com.vasiu_catalina.beauty_salon.entity.AppointmentService;

public interface AppointmentServiceService {

	Set<AppointmentService> getAllAppointmentServices();

	AppointmentService getAppointmentService(Long appointmentId, Long serviceId);

	AppointmentService createAppointmentService(Long appointmentId, Long serviceId,
			AppointmentService appointmentService);

	AppointmentService updateAppointmentService(Long appointmentId, Long serviceId,
			AppointmentService appointmentService);

	void deleteAppointmentService(Long appointmentId, Long serviceId);

	Set<AppointmentService> getAppointmentServicesByAppointment(Long appointmentId);

	Set<AppointmentService> getAppointmentServicesByService(Long serviceid);
}
