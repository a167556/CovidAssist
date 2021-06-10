package com.example.fyp;

import java.util.List;

public interface IHospitalLoadListener {
    void onHospitalLoadSuccess(List<Hospital> hospitalList);
    void onHospitalLoadFailed(String message);
}
