package mk.android.com.canvasdrawview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import mk.android.com.canvasdrawview.R;
import mk.android.com.canvasdrawview.model.Shape;

/**
 * Created by Mayuri Khinvasara on 02,December,2018
 */
public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private HashMap<Shape.Type, Integer> mDataSet;

    private Context mContext;
    private OnItemClicked onClick;

    public StatsAdapter(HashMap<Shape.Type, Integer> myDataset, StatsActivity context, OnItemClicked onClick) {
        this.mContext = context;
        this.mDataSet = myDataset;
        this.onClick = onClick;
    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton mDeleteButton;
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textViewStats);
            mDeleteButton = (ImageButton) v.findViewById(R.id.deleteShape);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stats_content, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StatsAdapter.ViewHolder holder,  int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Shape.Type type = (Shape.Type) mDataSet.keySet().toArray()[position];
        String stats = " Shape : " + type + "  Count : " + mDataSet.get(type);
        holder.mTextView.setText(stats);
        Log.d("canvas1234", " stats = " + stats + " pos= " + (position - 1));
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    public void setmDataSet(HashMap<Shape.Type, Integer> mDataSet) {
        this.mDataSet = mDataSet;
    }
}
