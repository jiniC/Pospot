package dongkyul.pospot.view.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class PhotoMainActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    List<Bitmap> mPhotoList_img;
    RealmList<byte[]> mPhotoList_byte;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    public Button btnHome;
    public Button btnMarkerCreate;
    EditText textTitle;

    private int id_view;
    private String absolutePath;

    private DB_Manager db_manager;

    PhotoMyAdapter myAdapter;

    Realm realm;
    double pointLat;
    double pointLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_main);

        View.OnClickListener pickFromCameraListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakeAlbumAction();
            }
        };

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(PhotoMainActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotoList_img = new ArrayList<Bitmap>();
        mPhotoList_byte = new RealmList<>();
        myAdapter = new PhotoMyAdapter(PhotoMainActivity.this, mPhotoList_img, pickFromCameraListener);
        mRecyclerView.setAdapter(myAdapter);

        btnHome = (Button) findViewById(R.id.btnHome);

        btnMarkerCreate = (Button) findViewById(R.id.btnMarkerCreate);
        textTitle = (EditText)findViewById(R.id.textTitle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        pointLat = extras.getDouble("pointLat");
        pointLon = extras.getDouble("pointLon");

        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoMainActivity.this, MainMapActivity.class);
                startActivity(intent);
            }
        });

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        RealmConfiguration config = new RealmConfiguration.Builder().name("PhotoMarkerDB").deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);

        btnMarkerCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoMainActivity.this,PhotoRealmDBCheck.class);
                realm.beginTransaction();
                PhotoMarkerDB obj = realm.createObject(PhotoMarkerDB.class);
                obj.setTitle(textTitle.getText().toString());
                obj.setLat(pointLat);
                obj.setLon(pointLon);
               //obj.setPhotoList(mPhotoList_byte);
                realm.commitTransaction();
                realm.close();
                finish();
                startActivity(intent);
            }
        });
    }

    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
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
                    mPhotoList_img.add(photo);
                    storeCropImage(photo, filePath);
                    absolutePath = filePath;

//                    int size = photo.getRowBytes() * photo.getHeight();
//                    ByteBuffer b = ByteBuffer.allocate(size);
//                    photo.copyPixelsFromBuffer(b);
//                    byte[] bytes = new byte[size];
//                    mPhotoList_byte.add(bytes);
//                    try {
//                        b.get(bytes, 0, bytes.length);
//                    } catch (BufferUnderflowException e) {
//                    }

                    break;
                }
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()) {
                    f.delete();
                }
            }
        }
        myAdapter.notifyDataSetChanged();
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