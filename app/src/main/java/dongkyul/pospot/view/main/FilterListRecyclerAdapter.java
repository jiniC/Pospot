package dongkyul.pospot.view.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;

public class FilterListRecyclerAdapter extends RecyclerView.Adapter<FilterListRecyclerAdapter.FilterListViewHolder> {

    private Context mContext;
    private ProgressDialog mProgress;

    List<Bitmap> mPhotoList_img = new ArrayList<Bitmap>();

    public FilterListRecyclerAdapter(Context mContext, List<Bitmap> mPhotoList_img, ProgressDialog mProgressDialog) {
        this.mContext = mContext;
        this.mPhotoList_img = mPhotoList_img;
        this.mProgress=mProgressDialog;
    }

    @Override
    public FilterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_view_recycler_adapter,  parent, false);
        return new FilterListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterListViewHolder holder, final int position) {
        holder.mPhoto.setImageBitmap(mPhotoList_img.get(position));
        if(mPhotoList_img.size()<3){
            MarkerClickedActivity.endLoading();
        }
        else if(position==2) {
            MarkerClickedActivity.endLoading();
        }
        holder.mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,FilteredCameraActivity.class);
                intent.putExtra("filterIndex",position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList_img.size();
    }

    class FilterListViewHolder extends RecyclerView.ViewHolder {

        ImageView mPhoto;

        public FilterListViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.photoToFilter);
        }
    }

}
