package mk.android.com.canvasdrawview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import mk.android.com.canvasdrawview.R;
import mk.android.com.canvasdrawview.model.Shape;

/**
 * Created by Mayuri Khinvasara on 02,December,2018
 */
public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {
    private final HashMap<Shape.Type, Integer> mDataSet ;

    private Context mContext;
    public StatsAdapter(HashMap<Shape.Type, Integer> myDataset) {
       // this.mContext = context;
        this.mDataSet = myDataset;
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.textViewStats);
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
    public void onBindViewHolder(@NonNull final StatsAdapter.ViewHolder holder, final int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String stats = " Shape : "+ Shape.Type.values()[position] + "  count : "+ mDataSet.get(Shape.Type.values()[position]);
        holder.mTextView.setText(stats);
        Log.d("canvas1234", " stats = "+ stats+ " pos= "+position);
        }

    @Override
    public int getItemCount() {
        if(mDataSet == null)
            return  0;
        Log.d("canvas1234", "getItemCount :"+mDataSet.size());
        return mDataSet.size();
    }
}
