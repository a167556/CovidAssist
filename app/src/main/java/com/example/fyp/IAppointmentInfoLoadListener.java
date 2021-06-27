package com.example.fyp;

public interface IAppointmentInfoLoadListener {
    void onAppointmentInfoLoadEmpty();
    void onAppointmentInfoLoadSuccess(AppointmentInformation appointmentInformation, String documentId);
    void onAppointmentInfoLoadFailed(String message);
}
