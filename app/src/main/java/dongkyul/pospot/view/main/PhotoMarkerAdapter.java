package dongkyul.pospot.view.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;

public class PhotoMarkerAdapter extends RecyclerView.Adapter<PhotoMarkerAdapter.PhotoViewHolder> {

    private Context mContext;

    List<Bitmap> mPhotoList_img = new ArrayList<Bitmap>();

    public PhotoMarkerAdapter(Context mContext, List<Bitmap> mPhotoList_img) {
        this.mContext = mContext;
        this.mPhotoList_img = mPhotoList_img;
    }

    @Override
    public PhotoMarkerAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_photo_recyclerview_custom_layout,  parent, false);

        return new PhotoMarkerAdapter.PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoMarkerAdapter.PhotoViewHolder holder, int position) {
        holder.mPhoto.setImageBitmap(mPhotoList_img.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotoList_img.size()+1;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }

}
