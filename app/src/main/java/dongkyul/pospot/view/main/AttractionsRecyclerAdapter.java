package dongkyul.pospot.view.main;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dongkyul.pospot.R;

public class AttractionsRecyclerAdapter extends RecyclerView.Adapter<AttractionsRecyclerAdapter.ViewHolder> {

    private Context mContext;

    List<String> tagNames;
    List<Integer> tagNums;



    public AttractionsRecyclerAdapter(List<String> tagNames, List<Integer> tagNums){
            this.tagNames = tagNames;
            this.tagNums = tagNums;
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.attractions_recycler_adapter,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tagName.setText(tagNames.get(position));
        holder.tagNum.setText("#"+Integer.toString(tagNums.get(position)));
    }

    @Override
    public int getItemCount() {
        return this.tagNums.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tagName;
        TextView tagNum;
        ConstraintLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_name);
            tagNum = itemView.findViewById(R.id.tag_num);
            itemLayout = (ConstraintLayout)itemView.findViewById(R.id.itemLayout);
        }
    }
}

