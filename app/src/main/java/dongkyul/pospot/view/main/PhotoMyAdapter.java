package dongkyul.pospot.view.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
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

            // 사진 롱클릭 이미지 확대
            holder.mPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

                    alertDialog.setTitle("대표 사진 설정");
                    alertDialog.setMessage("선택한 사진을 대표이미지로 \n 설정하시겠습니까?");

                    alertDialog.setPositiveButton("네",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    // 선택한 사진 대표이미지로 설정 -> 원래 액티비티로 돌아옴 -> 다이얼로그 창 닫음
//                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    mContext.startActivity(intent);
                                }
                            });
                    alertDialog.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                    return true;
                }
            });

            // 사진 클릭 이미지 확대
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

