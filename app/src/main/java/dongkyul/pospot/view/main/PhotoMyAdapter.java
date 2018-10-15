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

public class PhotoMyAdapter extends RecyclerView.Adapter<PhotoMyAdapter.PhotoViewHolder> {

    private Context mContext;
    private int[] mPhotoList;
    View.OnClickListener pickPhotoListener;

    public PhotoMyAdapter(Context mContext, int[] mPhotoList, View.OnClickListener pickPhotoListener) {
        this.mContext = mContext;
        this.mPhotoList = mPhotoList;
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

        if(position == mPhotoList.length) {
           holder.addBtn.setOnClickListener(pickPhotoListener);
        }
        else {
            holder.mPhoto.setImageResource(mPhotoList[position]);
            holder.mPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent(mContext, PhotoDetailActivity.class);
                    mIntent.putExtra("Image", mPhotoList[holder.getAdapterPosition()]);
                    mContext.startActivity(mIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPhotoList.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mPhotoList.length) ? R.layout.activity_photo_add_button : R.layout.activity_photo_recyclerview_custom_layout;
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

