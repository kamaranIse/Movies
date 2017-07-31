package com.example.twanist.project_movie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Myholder> {

    List<ReviewMovies> reviewMoviesList;

    public ReviewAdapter(List<ReviewMovies> reviewMoviesList) {
        this.reviewMoviesList = reviewMoviesList;
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_layout, parent, false);
        Myholder myholder = new Myholder(view);
        return myholder;

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        ReviewMovies reviewMovies = reviewMoviesList.get(position);
        holder.txt_author.setText(reviewMovies.getAuthor());
        holder.txt_content.setText(reviewMovies.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewMoviesList.size();
    }

    public static class Myholder extends RecyclerView.ViewHolder {
        TextView txt_content, txt_author;

        public Myholder(View itemView) {
            super(itemView);
            txt_author = (TextView) itemView.findViewById(R.id.author);
            txt_content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
