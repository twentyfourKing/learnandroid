package follow.twentyfourking.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    protected View mHeader;
    protected View mFooter;
    protected RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        ifGridLayoutManager();
    }
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }
    public boolean removeHeaderView() {
        if (hasHeaderView()) {
            mHeader = null;
            notifyItemRemoved(0);
            return true;
        }
        return false;
    }

    public boolean removeFooterView() {
        if (hasFooterView()) {
            int footerPosition = getItemCount() - 1;
            mFooter = null;
            notifyItemRemoved(footerPosition);
            return true;
        }
        return false;
    }

    public void addHeaderView(View header) {
        if (hasHeaderView())
            throw new IllegalStateException("You have already added a header view.");
        mHeader = header;
        setLayoutParams(mHeader);
        ifGridLayoutManager();
        notifyItemInserted(0);
    }

    public void addFooterView(View footer) {
        if (hasFooterView())
            throw new IllegalStateException("You have already added a footer view.");
        mFooter = footer;
        setLayoutParams(mFooter);
        ifGridLayoutManager();
        notifyItemInserted(getItemCount() - 2);
    }

    public boolean hasHeaderView() {
        return getHeaderView() != null;
    }

    public boolean hasFooterView() {
        return getFooterView() != null;
    }

    public boolean isHeaderView(int position) {
        return hasHeaderView() && position == 0;
    }

    public boolean isFooterView(int position) {
        return hasFooterView() && position == getItemCount() - 1;
    }

    private void ifGridLayoutManager() {
        if (hasHeaderView() || hasFooterView()) {
            final RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                        ((GridLayoutManager) layoutManager).getSpanSizeLookup();
                ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeaderView(position) || isFooterView(position)) ?
                                ((GridLayoutManager) layoutManager).getSpanCount() :
                                originalSpanSizeLookup.getSpanSize(position);
                    }
                });
            }
        }
    }

    private void setLayoutParams(View view) {
        if (hasHeaderView() || hasFooterView()) {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager.canScrollVertically()) {
                view.setLayoutParams(new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT));
            } else {
                view.setLayoutParams(new RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.WRAP_CONTENT,
                        RecyclerView.LayoutParams.MATCH_PARENT));
            }
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return hasLayoutManager() ? mRecyclerView.getLayoutManager() : null;
    }

    public View getHeaderView() {
        return mHeader;
    }

    public View getFooterView() {
        return mFooter;
    }

    public boolean hasLayoutManager() {
        return mRecyclerView != null && mRecyclerView.getLayoutManager() != null;
    }

}

