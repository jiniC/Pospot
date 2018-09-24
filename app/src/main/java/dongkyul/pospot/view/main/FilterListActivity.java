package dongkyul.pospot.view.main;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.utils.Crawler;
import dongkyul.pospot.view.common.BaseActivity;

public class FilterListActivity extends BaseActivity {

    public ProgressDialog mProgressDialog;
    List<ImageView> imageViews;
    WebView webView;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);
        init();
    }

    @Override
    public void init() {
        super.init();
        tag = getIntent().getStringExtra("tag");
        TextView tagtitle = (TextView)findViewById(R.id.tag_title);
        tagtitle.setText("#"+tag);
        setImages(tag);
    }

    private void setImages(String tag){
        imageViews = new ArrayList<>();
        imageViews.add((ImageView)findViewById(R.id.img1));
        imageViews.add((ImageView)findViewById(R.id.img2));
        imageViews.add((ImageView)findViewById(R.id.img3));
        imageViews.add((ImageView)findViewById(R.id.img4));
        imageViews.add((ImageView)findViewById(R.id.img5));
        imageViews.add((ImageView)findViewById(R.id.img6));
        imageViews.add((ImageView)findViewById(R.id.img7));
        imageViews.add((ImageView)findViewById(R.id.img8));
        webView = new WebView(getApplicationContext());
        Crawler c = new Crawler(webView);
        InstaImageCallBack imageCallback = new InstaImageCallBack() {
            @Override
            public void loadImages(final List<Bitmap> images) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i=0;
                        for(ImageView view:imageViews) {
                            view.setImageBitmap(images.get(i));
                            i++;
                        }
                        hideProgressDialog();
                    }
                });
            }
        };
        showProgressDialog();
        c.getImages(tag,imageCallback);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("받아오는 중");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        if(webView!=null) {
            webView.destroy();
            webView=null;
        }
        super.onDestroy();
    }
}
