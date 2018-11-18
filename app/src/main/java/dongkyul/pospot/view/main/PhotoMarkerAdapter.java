package dongkyul.pospot.view.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class PhotoMarkerAdapter extends RecyclerView.Adapter<PhotoMarkerAdapter.PhotoMarkerViewHolder> {

    private Context mContext;

    List<Bitmap> mPhotoList_img = new ArrayList<Bitmap>();
    PhotoMarkerDB clickPhotoMarker;
    Realm realm;

    public PhotoMarkerAdapter(Context mContext, List<Bitmap> mPhotoList_img, PhotoMarkerDB clickPhotoMarker) {
        this.mContext = mContext;
        this.mPhotoList_img = mPhotoList_img;
        this.clickPhotoMarker = clickPhotoMarker;
    }

    @Override
    public PhotoMarkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_photo_recyclerview_custom_layout,  parent, false);
        return new PhotoMarkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoMarkerViewHolder holder, final int position) {
        holder.mPhoto.setImageBitmap(mPhotoList_img.get(position));
        holder.mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to-be
                // 클릭한 사진의 index값을 clickPhotoMarker의 titleIndex값에 넣어주기
//                RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoToAdd").deleteRealmIfMigrationNeeded().build();
//                realm = Realm.getInstance(config);
//                realm.beginTransaction();
//                clickPhotoMarker.setTitleIndex(position);
                Log.e("title ::: ", String.valueOf(clickPhotoMarker.getTitle()));
                Log.e("lat ::: ", String.valueOf(clickPhotoMarker.getLat()));
                Log.e("lon ::: ", String.valueOf(clickPhotoMarker.getLon()));
                Log.e("TitleIndex ::: ", String.valueOf(clickPhotoMarker.getTitleIndex()));
//                realm.commitTransaction();
//                realm.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList_img.size();
    }

    class PhotoMarkerViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;

        public PhotoMarkerViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }

}
