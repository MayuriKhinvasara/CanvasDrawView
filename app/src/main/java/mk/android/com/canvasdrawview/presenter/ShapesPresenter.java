package mk.android.com.canvasdrawview.presenter;

import android.util.Log;

import java.util.LinkedList;
import java.util.Random;

import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.view.CustomView;

/**
 * Created by Mayuri Khinvasara on 02,December,2018
 */
public class ShapesPresenter {
    private static final String LOG_TAG = "canvas123";
    private final CustomView canvas;
    public static final int RADIUS = 60;
    int x;
    int y;
    private int maxX;
    private int maxY;
    private LinkedList<Shape> historyList = new LinkedList<>();

    public ShapesPresenter(CustomView canvas) {
        this.canvas = canvas;
    }

    private void generateRandomXAndY() {

        Random rn = new Random();
        int diff = maxX - RADIUS;
        x = rn.nextInt(diff + 1);
        x += RADIUS;

        rn = new Random();
        diff = maxY - RADIUS;
        y = rn.nextInt(diff + 1);
        y += RADIUS;

        Log.d(LOG_TAG, " Random x= "+x + "y"+y);
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void addShape( Shape.Type type) {
        generateRandomXAndY();
        Shape shape = new Shape(x, y, 60);
        shape.setType(type);
        historyList.add(shape);
        canvas.setHistoryList(historyList);
        canvas.invalidate();
    }

    public void undo() {

        if (historyList.size() > 0) {
            historyList.removeLast();
            canvas.invalidate();
        }
    }
}
