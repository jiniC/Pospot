package dongkyul.pospot.view.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;

public class PhotoMainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<Bitmap> mPhotoList_img;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    public Button btnHome;
    public Button btnViewFilter;
    public Button btnAddPhoto;

    private int id_view;
    private String absolutePath;

    private DB_Manager db_manager;

    PhotoMyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_main);

        View.OnClickListener pickFromCameraListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to-be
                // 버튼클릭에 연동 말고 이미지 추가되면 바로 뷰 업데이트 실행
                doTakeAlbumAction();
                mRecyclerView.setAdapter(myAdapter);
            }
        };

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PhotoMainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotoList_img = new ArrayList<Bitmap>();
        myAdapter = new PhotoMyAdapter(PhotoMainActivity.this, mPhotoList_img, pickFromCameraListener);
        mRecyclerView.setAdapter(myAdapter);
    }

    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;
        switch(requestCode) {
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
                //Log.d("PICK_FROM_ALBUM??", mImageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE);
                break;
            }
            case CROP_FROM_iMAGE:
            {
                if(resultCode!=RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pospot/"+System.currentTimeMillis()+".jpg";
                if(extras!=null) {
                    Bitmap photo = extras.getParcelable("data");
                    // to-be 이미지 뷰에 추가되게
                    mPhotoList_img.add(photo);
                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;
                }
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()) {
                    f.delete();
                }
            }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pospot";
        File directory_Pospot = new File(dirPath);
        if(!directory_Pospot.exists()) {
            directory_Pospot.mkdir();
        }
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}