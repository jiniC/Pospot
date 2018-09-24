package dongkyul.pospot.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dongkyul.pospot.R;
import dongkyul.pospot.view.common.BaseActivity;

public class MainActivity extends BaseActivity {

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.filterSearch);
        Button filterSearchButton = (Button)findViewById(R.id.filterSearchButton);
        filterSearchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.filterSearchButton:
                Intent intent = new Intent(MainActivity.this,FilterListActivity.class);
                intent.putExtra("tag",editText.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
