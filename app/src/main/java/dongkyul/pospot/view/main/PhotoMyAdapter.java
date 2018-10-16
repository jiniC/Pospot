package dongkyul.pospot.view.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;

public class PhotoMyAdapter extends RecyclerView.Adapter<PhotoMyAdapter.PhotoViewHolder> {

    private Context mContext;

    List<Bitmap> mPhotoList_img = new ArrayList<Bitmap>();
    View.OnClickListener pickPhotoListener;

    public PhotoMyAdapter(Context mContext, List<Bitmap> mPhotoList_img, View.OnClickListener pickPhotoListener) {
        this.mContext = mContext;
        this.mPhotoList_img = mPhotoList_img;
        this.pickPhotoListener = pickPhotoListener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType == R.layout.activity_photo_add_button){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_photo_add_button, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_photo_recyclerview_custom_layout,  parent, false);
        }

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        if(position == mPhotoList_img.size()) {
            // Log.e("seojin test !!", String.valueOf(mPhotoist_img.size()));
            holder.addBtn.setOnClickListener(pickPhotoListener);
        }

        else {
            holder.mPhoto.setImageBitmap(mPhotoList_img.get(position));
            holder.mPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(mContext, PhotoDetailActivity.class);
                    mIntent.putExtra("Image", mPhotoList_img.get(holder.getAdapterPosition()));
                    mContext.startActivity(mIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPhotoList_img.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mPhotoList_img.size()) ? R.layout.activity_photo_add_button : R.layout.activity_photo_recyclerview_custom_layout;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;
        Button addBtn;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.ivPhoto);
            addBtn = itemView.findViewById(R.id.btnAddPhoto);
        }
    }
}

