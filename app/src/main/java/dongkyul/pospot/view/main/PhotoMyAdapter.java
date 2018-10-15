package dongkyul.pospot.view.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import dongkyul.pospot.R;

public class PhotoMyAdapter extends RecyclerView.Adapter<PhotoMyAdapter.PlaceViewHolder> {

    private Context mContext;
    private int[] mPlaceList;
    View.OnClickListener pickPhotoListener;

    public PhotoMyAdapter(Context mContext, int[] mPlaceList, View.OnClickListener pickPhotoListener) {
        this.mContext = mContext;
        this.mPlaceList = mPlaceList;
        this.pickPhotoListener = pickPhotoListener;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType == R.layout.activity_photo_add_button){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_photo_add_button, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_photo_recyclerview_custom_layout,  parent, false);
        }

        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, int position) {

        if(position == mPlaceList.length) {
           holder.addBtn.setOnClickListener(pickPhotoListener);
        }
        else {
            holder.mPlace.setImageResource(mPlaceList[position]);
            holder.mPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(mContext, PhotoDetailActivity.class);
                    mIntent.putExtra("Image", mPlaceList[holder.getAdapterPosition()]);
                    mContext.startActivity(mIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPlaceList.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mPlaceList.length) ? R.layout.activity_photo_add_button : R.layout.activity_photo_recyclerview_custom_layout;
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        ImageView mPlace;
        Button addBtn;

        public PlaceViewHolder(View itemView) {
            super(itemView);

            mPlace = itemView.findViewById(R.id.ivPlace);
            addBtn = itemView.findViewById(R.id.btnAddPhoto);
        }
    }
}

