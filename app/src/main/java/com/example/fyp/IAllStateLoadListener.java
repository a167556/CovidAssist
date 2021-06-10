package com.example.fyp;

import java.util.List;

public interface IAllStateLoadListener {
    void onAllSalonLoadSuccess(List<String> areaNameList);
    void onAllSalonLoadFailed(String message);
}
