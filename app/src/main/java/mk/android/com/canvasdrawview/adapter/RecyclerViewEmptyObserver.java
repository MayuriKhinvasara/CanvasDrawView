package mk.android.com.canvasdrawview.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mayuri Khinvasara on 03,December,2018
 */
public class RecyclerViewEmptyObserver extends RecyclerView.AdapterDataObserver {

    private static final String LOG_TAG = RecyclerViewEmptyObserver.class.getSimpleName();
    private TextView emptyView;
    private RecyclerView recyclerView;


    /**
     * Constructor to set an Empty View for the RV
     */
    public RecyclerViewEmptyObserver(RecyclerView rv, TextView ev) {
        this.recyclerView = rv;
        this.emptyView    = ev;
        checkIfEmpty();
    }

    /**
     * Check if Layout is empty and show the appropriate view
     */
    private void checkIfEmpty() {

        if (emptyView != null && recyclerView.getAdapter() != null) {

            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            Log.d(LOG_TAG, " Enabling empty view for list : No data found " );
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }


    /**
     Abstract method implementations
     */
    @Override
    public void onChanged() {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        checkIfEmpty();
    }
}
