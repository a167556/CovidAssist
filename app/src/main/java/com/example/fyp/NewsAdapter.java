package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private List<Articles> articlesList;

    public NewsAdapter(Context context, List<Articles> articlesList) {
        this.context = context;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.article_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {

        Articles a = articlesList.get(position);
//        Source source = articlesList.get(position).getSource();
        holder.newsTitle.setText(articlesList.get(position).getTitle());
//        holder.newsDescription.setText(articlesList.get(position).getDescription());
        String url = a.getUrl();
        String date = articlesList.get(position).getPublishedAt();
        String YY = date.substring(0,4);
        String DD = date.substring(8,10);
        String MM= date.substring(5,7);


        holder.newsPublishedAt.setText(DD + "-"+ MM + "-"+YY);

//        holder.newsPublishedAt.setText(DD + "-"+ MM + "-"+YY);
        holder.newsSource.setText(a.getSource().getName());
        //holder.newsUrl.setText(articlesList.get(position).getUrl());

        Glide.with(context)
                .load(articlesList.get(position).getUrlToImage())
                .into(holder.newsImage);

        holder.cardvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsDetail.class);
                intent.putExtra("title",a.getTitle());
                intent.putExtra("source",a.getSource().getName());
                intent.putExtra("time",DD + "-"+ MM + "-"+YY);
                intent.putExtra("imageUrl",a.getUrlToImage());
                intent.putExtra("desc",a.getDescription());
                intent.putExtra("url",a.getUrl());
                context.startActivity(intent);
            }
        });

//        holder.newsUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri url = Uri.parse(articlesList.get(position).getUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, url);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle, newsPublishedAt, newsDescription, newsSource, newsUrl;
        CardView cardvNews;
        ImageView newsImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newTitle);
            newsPublishedAt = itemView.findViewById(R.id.newsPublishedAt);
//            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsSource = itemView.findViewById(R.id.sourceName);
            newsImage = itemView.findViewById(R.id.newsImage);
            cardvNews = itemView.findViewById(R.id.cvNews);

//            newsUrl = itemView.findViewById(R.id.urlToNews);
        }
    }
    public String dateTime (String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);

        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }
    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}