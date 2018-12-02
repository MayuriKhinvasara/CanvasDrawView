package mk.android.com.canvasdrawview.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mk.android.com.canvasdrawview.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatsActivityFragment extends Fragment {

    public StatsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }
}
