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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.presenter.CanvasTouch;
import mk.android.com.canvasdrawview.presenter.ShapesPresenter;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class CustomView extends View {
    private final String TAG = "canvas1234";
    public final int RADIUS = ShapesPresenter.RADIUS;
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

    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
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
                        canvas.drawCircle(shape.getX(), shape.getY(), RADIUS, drawPaint);
                        break;

                    case SQUARE:
                        drawRectangle(shape.getX(), shape.getY());
                        break;
                    case TRIANGLE:
                        drawTriangle(shape.getX(), shape.getY(), (int) (2 * RADIUS));
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
                Log.d("canvas12345", " ACTION_DOWN");

                initialTouchX = event.getX();
                initialTouchY = event.getY();
                longPressDone = false;
                if (!longClickActive) {
                    longClickActive = true;
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("canvas12345", " ACTION_UP");
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
                Log.d("canvas12345", " ACTION_MOVE");
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

    public void drawRectangle(int x, int y) {
        drawPaint.setColor(Color.RED);
        Rect rectangle = new Rect((int) (x - ((0.8) * RADIUS)), (int) (y - ((0.6) * RADIUS)), (int) (x + ((0.8) * RADIUS)), (int) (y + ((0.6 * RADIUS))));
        canvas.drawRect(rectangle, drawPaint);
    }

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
