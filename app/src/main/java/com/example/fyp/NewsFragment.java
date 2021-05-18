package com.example.fyp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsFragment extends Fragment {
    private Fragment CovidFragment = new CovidFragment();
    private Chip chipStats, chipNews;
    private List<Articles> articlesList;
    private RecyclerView recyclerView;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        chipNews = view.findViewById(R.id.chipNews);
        chipStats = view.findViewById(R.id.chipStats);
        recyclerView = view.findViewById(R.id.rvNews);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipNews.setChecked(false);
                chipStats.setChecked(true);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(((ViewGroup)getView().getParent()).getId(), CovidFragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        chipNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipNews.setChecked(true);
                chipStats.setChecked(false);
            }
        });

        //retrofit builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Instance for interface
        NewsApiCall newsApiCall = retrofit.create(NewsApiCall.class);
        Call<NewsData> call = newsApiCall.getData();

        call.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                if(response.code() != 200){
                    System.out.println("Error getting request from news API");
                    return;
                }

                NewsData newsData = response.body();
                articlesList = new ArrayList<>(Arrays.asList(newsData.getArticles()));
                for (int i=0; i<5; i++){
                    System.out.println(articlesList.get(i).getUrlToImage());
                }
                putDataInRecyclerView(articlesList);
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {

            }
        });

    }

    private void putDataInRecyclerView(List<Articles> articlesList) {
        NewsAdapter adapter = new NewsAdapter(getContext(),articlesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemDecorator decorator = new ItemDecorator(30);
        recyclerView.addItemDecoration(decorator);
        recyclerView.setAdapter(adapter);
    }
}