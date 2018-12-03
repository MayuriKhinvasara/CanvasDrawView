package mk.android.com.canvasdrawview.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.model.Shape.Type;
import mk.android.com.canvasdrawview.view.CustomView;

/**
 * Created by Mayuri Khinvasara on 02,December,2018
 */
public class ShapesPresenter {
    private static final String LOG_TAG = "canvas123";
    public static final int TOTAL_SHAPES = 3;
    private static final int ACTION_DELETE = 14;
    private final CustomView canvas;
    public static final int RADIUS = 60;
    private final Context mContext;
    private int maxX;
    private int maxY;
    private static LinkedList<Shape> historyList = new LinkedList<>();
    private int actionSequence = 0;
    private int ACTION_TRANSFORM = 12;

    public ShapesPresenter(CustomView canvas, Context mContext) {
        this.canvas = canvas;
        this.mContext = mContext;
        canvas.setCanvasTouch(onTouchListener);
    }

    /**
     * Respons to click and long press events on canvas
     */
    private CanvasTouch onTouchListener = new CanvasTouch() {
        @Override
        public void onClickEvent(MotionEvent event) {
            Log.d(LOG_TAG, " onClickEvent done ");
            changeShapeOnTouch(event.getX(), event.getY(), ACTION_TRANSFORM);
        }

        @Override
        public void onLongPressEvent(float initialTouchX, float initialTouchY) {
            Log.d(LOG_TAG, " onLongPressEvent done ");
            changeShapeOnTouch(initialTouchX, initialTouchY, ACTION_DELETE);
        }

    };

    /**
     *
     * @param oldShape
     * @param index
     * @param initialTouchX
     * @param initialTouchY
     */
    private void askForDeleteShape(final Shape oldShape, final int index, float initialTouchX, float initialTouchY) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete ?")
                .setTitle("Delete Shape");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteShape(oldShape, index);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteShape(Shape oldShape, int i) {
        oldShape.setVisibility(false);
        oldShape.setActionNumber(actionSequence++);
        getHistoryList().set(i, oldShape);
        Log.d(LOG_TAG, "askForDeleteShape =  " + oldShape.getType());
        canvas.setHistoryList(getHistoryList());
        canvas.invalidate();
    }

    private void changeShapeOnTouch(float x, float y, int changeStatus) {
        int touchX = Math.round(x);
        int touchY = Math.round(y);
        //   Toast.makeText(this.getContext(), " Touch at " + touchX + " y= " + touchY, Toast.LENGTH_SHORT).show();
        int oldX, oldY;
        double Dis = Integer.MAX_VALUE;

        LinkedList<Shape> list = getHistoryList();
        Shape newShape = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            Shape oldShape = list.get(i);
            if (oldShape.isVisible()) {
                oldX = oldShape.getX();
                oldY = oldShape.getY();

                if (RADIUS >= calculateDistanceBetweenPoints(oldX, oldY, touchX, touchY)) {
                    if (changeStatus == ACTION_TRANSFORM)
                        addTransformShape(oldShape, i, oldX, oldY);
                    else if (changeStatus == ACTION_DELETE)
                        askForDeleteShape(oldShape, i, oldX, oldY);
                    break;
                }
            }
        }
    }

    private void addTransformShape(Shape oldShape, int index, int newX, int newY) {
        Log.d(LOG_TAG, " oldShape =  " + oldShape.getType());
        oldShape.setVisibility(false);
        getHistoryList().set(index, oldShape);
        Log.d(LOG_TAG, " HIDE oldShape =  " + oldShape.getType());
        //transform object
        int newShapeType = (oldShape.getType().value + 1) % TOTAL_SHAPES;
        Type newshapeType = Type.values()[newShapeType];
        Log.d(LOG_TAG, " newshape =  " + newshapeType);

        Shape newShape = createShape(newshapeType, newX, newY);
        newShape.setLastTranformIndex(index);
        upDateCanvas(newShape);
    }

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    private int[] generateRandomXAndY() {
        int x, y;
        Random rn = new Random();
        int diff = maxX - RADIUS;
        x = rn.nextInt(diff + 1);
        x += RADIUS;

        rn = new Random();
        diff = maxY - RADIUS;
        y = rn.nextInt(diff + 1);
        y += RADIUS;
        Log.d(LOG_TAG, " Random x= " + x + "y" + y);
        return new int[]{x, y};
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

    public void addShapeRandom(Type type) {
        int[] randomCordinates = generateRandomXAndY();
        Shape shape = createShape(type, randomCordinates[0], randomCordinates[1]);
        upDateCanvas(shape);
    }

    @NonNull
    private Shape createShape(Type type, int x, int y) {

        Shape shape = new Shape(x, y, RADIUS);
        shape.setType(type);
        return shape;
    }

    public void undo() {

        if (getHistoryList().size() > 0) {
            actionSequence--;
            Shape toDeleteShape = getHistoryList().getLast();
            if (toDeleteShape.getLastTranformIndex() != Shape.ACTION_CREATE) {
                int lastVisibleIndex = toDeleteShape.getLastTranformIndex();
                Shape lastVisibleShape = getHistoryList().get(lastVisibleIndex);
                lastVisibleShape.setVisibility(true);
                getHistoryList().set(lastVisibleIndex, lastVisibleShape);
            }
            getHistoryList().removeLast();
            canvas.setHistoryList(getHistoryList());
            canvas.invalidate();
        }
    }


    private void upDateCanvas(Shape shape) {
        Log.d(LOG_TAG, " upDateCanvas " + shape.getType() + " actiontype = " + actionSequence);
        shape.setActionNumber(actionSequence++);
        getHistoryList().add(shape);
        canvas.setHistoryList(getHistoryList());
        canvas.invalidate();
    }

    public LinkedList<Shape> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(LinkedList<Shape> historyList) {
        this.historyList = historyList;
    }

    public HashMap<Type, Integer> getCountByGroup() {
        HashMap<Type, Integer> shapeTypeCountMap = new HashMap<>();
        for (Shape shape : getHistoryList()) {
            if (shape.isVisible()) {
                Type shapeType = shape.getType();
                int existingCnt = 0;
                if (shapeTypeCountMap.containsKey(shape.getType()))
                    existingCnt = shapeTypeCountMap.get(shape.getType());
                existingCnt++;
                shapeTypeCountMap.put(shapeType, existingCnt);
            }
        }
        return shapeTypeCountMap;
    }
}
