package mk.android.com.canvasdrawview.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView

/**
 * Created by Mayuri Khinvasara on 03,December,2018
 */
class RecyclerViewEmptyObserver
/**
 * Constructor to set an Empty View for the RV
 */
(private val recyclerView: RecyclerView, private val emptyView: TextView?) : RecyclerView.AdapterDataObserver() {


    init {
        checkIfEmpty()
    }

    /**
     * Check if Layout is empty and show the appropriate view
     */
    private fun checkIfEmpty() {

        if (emptyView != null && recyclerView.adapter != null) {

            val emptyViewVisible = recyclerView.adapter!!.itemCount == 0
            Log.d(LOG_TAG, " Enabling empty view for list : No data found ")
            emptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }


    /**
     * Abstract method implementations
     */
    override fun onChanged() {
        checkIfEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

    companion object {

        private val LOG_TAG = RecyclerViewEmptyObserver::class.java.simpleName
    }
}
