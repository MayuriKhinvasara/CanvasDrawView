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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.presenter.ShapesPresenter;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class CustomView extends View {
    private static final String TAG = "canvas123";
    public static final int RADIUS = ShapesPresenter.RADIUS;
    private Canvas canvas;
    private int x = 50;
    private int y = 50;
    List<Shape> historyList = new ArrayList<>();

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

    // Let's draw three circles
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        Log.d(TAG, "  onDraw called");
        for (Shape shape : getHistoryList()) {
            switch (shape.getType()) {
                case CIRCLE:
                    drawPaint.setColor(Color.BLUE);
                    canvas.drawCircle(shape.getX(), shape.getY(), RADIUS, drawPaint);
                    break;

                case RECTANGLE:
                    drawRectangle(shape.getX(), shape.getY());
                    break;
                case TRIANGLE:
                    drawTriangle(shape.getX(), shape.getY(), (int) (2 * RADIUS));
                    break;
            }
        }
    }

    // Append new circle each time user presses on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, " onTouchEvent" + event);
        int touchX = Math.round(event.getX());
        int touchY = Math.round(event.getY());
        Toast.makeText(this.getContext(), " Touch at " + touchX + " y= " + touchY, Toast.LENGTH_SHORT).show();
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
}
