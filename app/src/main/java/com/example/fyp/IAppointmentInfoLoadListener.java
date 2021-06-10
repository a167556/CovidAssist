package com.example.fyp;

public interface IAppointmentInfoLoadListener {
    void onAppointmentInfoLoadEmpty();
    void onAppointmentInfoLoadSuccess(AppointmentInformation appointmentInformation);
    void onAppointmentInfoLoadFailed(String message);
}
