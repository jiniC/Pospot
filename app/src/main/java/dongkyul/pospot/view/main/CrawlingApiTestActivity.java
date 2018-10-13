package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class CrawlingApiTestActivity extends BaseActivity {

    String searchItem;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_insta_image);
        editText = (EditText)findViewById(R.id.filterSearch);
        // 클릭 마커 이름
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String searchItem = extras.getString("markerName");
        editText.setText(searchItem);
        Button filterSearchButton = (Button)findViewById(R.id.filterSearchButton);
        filterSearchButton.setOnClickListener(this);
        Button orderTagButton = (Button)findViewById(R.id.order_tag);
        orderTagButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.filterSearchButton:
                Intent intent = new Intent(CrawlingApiTestActivity.this,FilterListActivity.class);
                intent.putExtra("tag",editText.getText().toString());
                startActivity(intent);
                break;
            case R.id.order_tag:
                Intent i = new Intent(CrawlingApiTestActivity.this,TagRankActivity.class);
                ArrayList<String> tagList = new ArrayList<>();
                tagList.add("경복궁");
                tagList.add("인사동");
                tagList.add("창경궁");
                tagList.add("익선동");
                tagList.add("한강");
                tagList.add("광화문");
                i.putStringArrayListExtra("tagList",tagList);
                startActivity(i);
                break;
        }
    }
}
