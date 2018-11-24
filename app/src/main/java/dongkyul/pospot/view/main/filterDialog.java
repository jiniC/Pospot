package dongkyul.pospot.view.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import dongkyul.pospot.R;

public class filterDialog extends Dialog implements View.OnClickListener{
    private filterDialogListener listener;
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.filter_capture:
                listener.onCapture();
                break;
        }
    }
    public filterDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.filter);
        ConstraintLayout constraintLayout = findViewById(R.id.filterbackground);
        //constraintLayout.setBackground
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView btnCapture = (ImageView)findViewById(R.id.filter_capture);
        btnCapture.setOnClickListener(this);
    }
    public void setDialogListener(filterDialogListener listener){
        this.listener=listener;
    }
}
