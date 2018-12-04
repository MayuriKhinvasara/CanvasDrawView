package mk.android.com.canvasdrawview.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import mk.android.com.canvasdrawview.R;
import mk.android.com.canvasdrawview.adapter.RecyclerViewEmptyObserver;
import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.presenter.StatsPresenter;
import mk.android.com.canvasdrawview.util.ToastHelper;

public class StatsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StatsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView statsEmptyView;
    StatsPresenter statsPresenter;
    private HashMap<Shape.Type, Integer> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.stats_recycler_view);
        statsEmptyView = (TextView) findViewById(R.id.emptyView);
        statsPresenter = new StatsPresenter();
        setupStatsListView();
    }

    /*
    Setup the list view to show statistics per shape
     */
    @SuppressWarnings("unchecked")
    private void setupStatsListView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myDataset = (HashMap<Shape.Type, Integer>) statsPresenter.getCountByGroup();
        statsEmptyView.setVisibility(View.GONE);
        mAdapter = new StatsAdapter(myDataset, this, onClickDelete);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerViewEmptyObserver(mRecyclerView, statsEmptyView));
    }

    StatsAdapter.OnItemClicked onClickDelete = new StatsAdapter.OnItemClicked() {
        @Override
        public void onItemClick(int position) {
            Shape.Type type = (Shape.Type) myDataset.keySet().toArray()[position];
            Log.d("canvas1234", "   on item click : type " + type);
            statsPresenter.deleteAllByShape(type);
            ToastHelper.INSTANCE.makeText(StatsActivity.this, " All "+type+"s deleted. ");
            refreshData();
        }
    };

    /*
    Reload data in list, if coming from delete or any other action
     */
    private void refreshData() {
        myDataset = (HashMap<Shape.Type, Integer>) statsPresenter.getCountByGroup();
        mAdapter.setmDataSet(myDataset);
        mAdapter.notifyDataSetChanged();
    }

}
