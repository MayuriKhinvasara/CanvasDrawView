package mk.android.com.canvasdrawview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.presenter.CanvasTouch;
import mk.android.com.canvasdrawview.util.Constants;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class CustomView extends View {
    private static final String LOG_TAG = CustomView.class.getSimpleName();
    private final String TAG = CustomView.class.getSimpleName();
    public final int RADIUS = Constants.INSTANCE.getRADIUS();
    private Canvas canvas;
    List<Shape> historyList = new ArrayList<>();
    CanvasTouch canvasTouch;
    private boolean longPressDone;

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
        Log.d(TAG, "  constructor called");
    }

    // defines paint and canvas
    private Paint drawPaint;

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLUE);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        Log.d(TAG, "  onDraw called");
        for (Shape shape : getHistoryList()) {
            if (shape.isVisible()) {
                switch (shape.getType()) {
                    case CIRCLE:
                        drawPaint.setColor(Color.BLUE);
                        canvas.drawCircle(shape.getxCordinate(), shape.getyCordinate(), RADIUS, drawPaint);
                        break;
                    case SQUARE:
                        drawRectangle(shape.getxCordinate(), shape.getyCordinate());
                        break;
                    case TRIANGLE:
                        drawTriangle(shape.getxCordinate(), shape.getyCordinate(), (int) (2 * RADIUS));
                        break;
                }
            }
        }
    }

    private boolean longClickActive = false;
    float initialTouchX = 0;
    float initialTouchY = 0;
    private static final int MIN_CLICK_DURATION = 1000;
    private long startClickTime = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(LOG_TAG, " ACTION_DOWN");

                initialTouchX = event.getX();
                initialTouchY = event.getY();
                longPressDone = false;
                if (!longClickActive) {
                    longClickActive = true;
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(LOG_TAG, " ACTION_UP");
                long currentTime = Calendar.getInstance().getTimeInMillis();
                long clickDuration = currentTime - startClickTime;
                if (clickDuration <= MIN_CLICK_DURATION && !longPressDone) {
                    //normal click
                    if (canvasTouch != null) {
                        canvasTouch.onClickEvent(event);
                    }
                    longClickActive = false;
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(LOG_TAG, " ACTION_MOVE");
                currentTime = Calendar.getInstance().getTimeInMillis();
                clickDuration = currentTime - startClickTime;
                if (clickDuration >= MIN_CLICK_DURATION) {
                    if (canvasTouch != null) {
                        canvasTouch.onLongPressEvent(initialTouchX, initialTouchY);
                    }
                    longClickActive = false;
                    longPressDone = true;
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    return false;
                }
                break;
        }
        return true;
    }

     double squareSideHalf = 1 / Math.sqrt(2);
    //Consider pivot x,y as centroid.

    public void drawRectangle(int x, int y) {
        drawPaint.setColor(Color.RED);
        Rect rectangle = new Rect((int) (x - (squareSideHalf * RADIUS)), (int) (y - (squareSideHalf * RADIUS)), (int) (x + (squareSideHalf * RADIUS)), (int) (y + ((squareSideHalf * RADIUS))));
        canvas.drawRect(rectangle, drawPaint);
    }

    /*
    select three vertices of triangle. Draw 3 lines between them to form a traingle
     */
    public void drawTriangle(int x, int y, int width) {
        drawPaint.setColor(Color.GREEN);
        int halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(x, y - halfWidth); // Top
        path.lineTo(x - halfWidth, y + halfWidth); // Bottom left
        path.lineTo(x + halfWidth, y + halfWidth); // Bottom right
        path.lineTo(x, y - halfWidth); // Back to Top
        path.close();
        canvas.drawPath(path, drawPaint);
    }

    public List<Shape> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<Shape> historyList) {
        this.historyList = historyList;
    }

    public CanvasTouch getCanvasTouch() {
        return canvasTouch;
    }

    public void setCanvasTouch(CanvasTouch canvasTouch) {
        this.canvasTouch = canvasTouch;
    }

}
