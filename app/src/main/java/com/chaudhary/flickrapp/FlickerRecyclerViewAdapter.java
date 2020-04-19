package com.chaudhary.flickrapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickerRecyclerViewAdapter extends RecyclerView.Adapter<FlickerRecyclerViewAdapter.FlickerImageViewHolder> {
    private static final String TAG = "FlickerRecyclerViewAdap";
    private List<Photos> mPhotosList;
    private Context mContext;

    public FlickerRecyclerViewAdapter(List<Photos> photosList, Context context) {
        mPhotosList = photosList;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull FlickerImageViewHolder flickerImageViewHolder, int i) {

        if ((mPhotosList == null) || (mPhotosList.size()==0)){
            flickerImageViewHolder.thumbnail.setImageResource(R.drawable.baseline_image_black_48dp);
            flickerImageViewHolder.title.setText(mContext.getString(R.string.emptyphoto));
        }else {
            Photos photoItem = mPhotosList.get(i);
            Picasso.get().load(photoItem.getImages())
                    .error(R.drawable.baseline_image_black_48dp)
                    .placeholder(R.drawable.baseline_image_black_48dp)
                    .into(flickerImageViewHolder.thumbnail);

            flickerImageViewHolder.title.setText(photoItem.getTitle());
        }


    }

   @NonNull
    @Override
    public FlickerImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse,viewGroup,false);
        return new FlickerImageViewHolder(view);
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: called");
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.size() : 1);
    }

    void loadNewData(List<Photos> newPhotos){
        mPhotosList = newPhotos;
        notifyDataSetChanged();
    }

    public Photos getPhoto(int position){
        return ((mPhotosList != null) && (mPhotosList.size() != 0) ? mPhotosList.get(position) : null);
    }

    static class FlickerImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickerImageViewHolder";

        ImageView thumbnail = null;
        TextView title = null;

        public FlickerImageViewHolder(View itemView){
            super(itemView);
            this.thumbnail =  itemView.findViewById(R.id.thumbnail);
            this.title =  itemView.findViewById(R.id.title);
        }
    }
}
