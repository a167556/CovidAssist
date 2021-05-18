package com.example.fyp;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<CovidData> covidData;
    private CovidDataRepo covidDataRepo;
    private MutableLiveData<CovidGlobalData> covidGlobalData;
    private MutableLiveData<List<TopCountriesData>> chartData;
    Context context;
    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        covidDataRepo = CovidDataRepo.getInstance();
        covidData = covidDataRepo.setCovidData(context);
        covidGlobalData = covidDataRepo.setCovidGlobalData(context);
        chartData = covidDataRepo.setTopCountries(context);

    }

    public MutableLiveData<CovidData> getCovidData() {
        return covidData;
    }

    public MutableLiveData<CovidGlobalData> getCovidGlobalData() {
        return covidGlobalData;
    }

    public MutableLiveData<List<TopCountriesData>> getChartData() {
        return chartData;
    }
}