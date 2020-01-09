package follow.twentyfourking.base;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView author;
    public TextView chapter;
    public TextView date;
    protected BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
