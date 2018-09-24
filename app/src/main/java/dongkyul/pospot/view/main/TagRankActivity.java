package dongkyul.pospot.view.main;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.Map;

import dongkyul.pospot.R;
import dongkyul.pospot.utils.Crawler;
import dongkyul.pospot.view.common.BaseActivity;

public class TagRankActivity extends BaseActivity {
    ArrayList<String> tagList;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked_tag);
        init();
    }

    @Override
    public void init() {
        super.init();
        tagList = getIntent().getStringArrayListExtra("tagList");
        webView = new WebView(getApplicationContext());
        Crawler c = new Crawler(webView);
        TagNumCallBack tagNumCallBack = new TagNumCallBack() {
            @Override
            public void loadTagNum(Map<String, Integer> tags) {
                Log.e("s","loadTagNumCallback");
            }
        };
        c.getNumTags(tagList,tagNumCallBack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
