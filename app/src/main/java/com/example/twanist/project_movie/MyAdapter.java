package com.example.twanist.project_movie;

/**
 * Created by twanist on 7/31/17.
 */


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private List<MovieData> arrayList;
    private final Context context;


    public MyAdapter(List<MovieData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        final MovieData movieData = arrayList.get(position);
        final String url = "https://image.tmdb.org/t/p/w640" + movieData.getPoster_path();

        Picasso.with(context).load(url).into(holder.imageView);
        holder.title.setText(movieData.getTitle());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("image", movieData.getPoster_path());
                intent.putExtra("id", movieData.getId());
                intent.putExtra("title", movieData.getTitle());
                intent.putExtra("release_date", movieData.getRelease_date());
                intent.putExtra("vote_ava", movieData.getVote_ava());
                intent.putExtra("overview", movieData.getOverview());
                intent.putExtra("Backdrop_path", movieData.getBackdrop_path());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (arrayList == null) return 0;
        return arrayList.size();
    }

    public void swapAdapter(List<MovieData> movies) {
        arrayList = movies;
        notifyDataSetChanged();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView title;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title_dd);

        }
    }

}
