package dongkyul.pospot.view.common;

import android.support.v7.widget.RecyclerView;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter{
    public final static int LIST_VIEW_TYPE_HEADER = 0;
    public final static int LIST_VIEW_TYPE_CONTENT = 1;
    public final static int LIST_VIEW_TYPE_FOOTER = 2;
    public final static int LIST_VIEW_TYPE_SEARCH = 3;

    public boolean isFooter(int position) {
        return (position + 1 == getItemCount());

    }

    public boolean isHeader(int position) {
        return (position == 0);

    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position))
            return LIST_VIEW_TYPE_HEADER;
        else if (position == 2)
            return LIST_VIEW_TYPE_SEARCH;
        else if (isFooter(position))
            return LIST_VIEW_TYPE_FOOTER;
        else
            return LIST_VIEW_TYPE_CONTENT;

    }
}
