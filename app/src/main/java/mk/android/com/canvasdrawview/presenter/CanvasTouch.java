package mk.android.com.canvasdrawview.presenter;

import android.view.MotionEvent;

/**
 * Created by Mayuri Khinvasara on 02,December,2018
 */
public interface CanvasTouch {
     void onClickEvent(MotionEvent event);
     void onLongPressEvent(float initialTouchX, float initialTouchY);
}
