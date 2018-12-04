package mk.android.com.canvasdrawview.presenter

import android.view.MotionEvent

/**
 * Created by Mayuri Khinvasara on 02,December,2018
 */
/*
     Interface to detect canvas touch and canvas long press events
 */
interface CanvasTouch {
    fun onClickEvent(event: MotionEvent)
    fun onLongPressEvent(initialTouchX: Float, initialTouchY: Float)
}
