package com.example.twanist.project_movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoPlayAdapter extends RecyclerView.Adapter<VideoPlayAdapter.MyHolder> {

    List<PlayVideo> playVideoList;
    private static final String YOUTUBE = "https://www.youtube.com/results?search_query=";
    private Context context;

    public VideoPlayAdapter(List<PlayVideo> playVideoList, Context context) {
        this.playVideoList = playVideoList;
        this.context=context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_play, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        final PlayVideo playVideo = playVideoList.get(position);
        holder.txType.setText(playVideo.getType());
        holder.imName.toString().concat(playVideo.getName());
        holder.imName.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse(YOUTUBE+playVideo.getName());
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                if(intent.resolveActivity(context.getPackageManager())!=null){
                    context.startActivity(intent);
                }
            }
        });


    }



    @Override
    public int getItemCount() {
        return playVideoList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        public TextView txType;
        public ImageView imName;

        public MyHolder(View itemView) {
            super(itemView);
            imName = (ImageView) itemView.findViewById(R.id.imgName);
            txType = (TextView) itemView.findViewById(R.id.txt_type);

        }
    }


}
