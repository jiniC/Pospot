package dongkyul.pospot.view.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import dongkyul.pospot.R;
import dongkyul.pospot.utils.Crawler;
import dongkyul.pospot.view.common.BaseActivity;

public class MainActivity extends BaseActivity {
    List<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_main);
        setImages();
    }
    private void setImages(){
        imageViews = new ArrayList<>();
        imageViews.add((ImageView)findViewById(R.id.img1));
        imageViews.add((ImageView)findViewById(R.id.img2));
        imageViews.add((ImageView)findViewById(R.id.img3));
        imageViews.add((ImageView)findViewById(R.id.img4));
        imageViews.add((ImageView)findViewById(R.id.img5));
        imageViews.add((ImageView)findViewById(R.id.img6));
        Crawler c = new Crawler(this);
        InstaImageCallBack imageCallback = new InstaImageCallBack() {
            @Override
            public void loadImages(List<Bitmap> images) {
                int i=0;
                for(ImageView view:imageViews) {
                    view.setImageBitmap(images.get(i));
                    i++;
                }
            }
        };
        c.getImages("경복궁",imageCallback);
    }
}
