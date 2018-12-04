package mk.android.com.canvasdrawview.interactor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import mk.android.com.canvasdrawview.model.Shape;
import mk.android.com.canvasdrawview.util.Constants;
import mk.android.com.canvasdrawview.view.CustomView;

/**
 * Created by Mayuri Khinvasara on 04,December,2018
 */

/**
 * Handles business logic of creation , transformation  and deletion of shape
 */
public class ShapesInteractor {
    private static final ShapesInteractor ourInstance = new ShapesInteractor();
    private static final String LOG_TAG = "";
    private Context mContext;

    public static ShapesInteractor getInstance() {
        return ourInstance;
    }

    private ShapesInteractor() {
    }
    private CustomView canvas;
    private int maxX;
    private int maxY;

    /*
    Choose linkedlist (default doubly linkedlist in java ) as the data structure
     since we can add, transform, delete shapes very quickly in the same list without using extra memory
     */
    private static LinkedList<Shape> historyList = new LinkedList<>();
    private int actionSequence = 0;

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

    public void changeShapeOnTouch(float x, float y, int changeStatus) {
        int touchX = Math.round(x);
        int touchY = Math.round(y);
        //   Toast.makeText(this.getContext(), " Touch at " + touchX + " y= " + touchY, Toast.LENGTH_SHORT).show();
        int oldX, oldY;

        LinkedList<Shape> list = getHistoryList();
        Shape newShape = null;
        //Traverse from end so that we find the last performed action or shape first.
        for (int i = list.size() - 1; i >= 0; i--) {
            Shape oldShape = list.get(i);
            if (oldShape.isVisible()) {
                oldX = oldShape.getxCordinate();
                oldY = oldShape.getyCordinate();

                //Find an existing shape where the user has clicked on the canvas
                if (Constants.INSTANCE.getRADIUS() >= calculateDistanceBetweenPoints(oldX, oldY, touchX, touchY)) {
                    if (changeStatus == Constants.INSTANCE.getACTION_TRANSFORM())
                        addTransformShape(oldShape, i, oldX, oldY);
                    else if (changeStatus == Constants.INSTANCE.getACTION_DELETE())
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

        //transform object , rotate into available objects
        int newShapeType = (oldShape.getType().getValue() + 1) % Constants.INSTANCE.getTOTAL_SHAPES();
        Shape.Type newshapeType = mk.android.com.canvasdrawview.model.Shape.Type.values()[newShapeType];
        Log.d(LOG_TAG, " newshape =  " + newshapeType);

        Shape newShape = createShape(newshapeType, newX, newY);
        newShape.setLastTranformationIndex(index);
        upDateCanvas(newShape);
    }

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    /*
    Generate random x,y from 0,0 to screen max width and height
     */
    private int[] generateRandomXAndY() {
        int x, y;
        Random rn = new Random();
        int diff = maxX - Constants.INSTANCE.getRADIUS();
        x = rn.nextInt(diff + 1);
        x += Constants.INSTANCE.getRADIUS();

        rn = new Random();
        diff = maxY - Constants.INSTANCE.getRADIUS();
        y = rn.nextInt(diff + 1);
        y += Constants.INSTANCE.getRADIUS();
        Log.d(LOG_TAG, " Random x= " + x + "y" + y);
        return new int[]{x, y};
    }

    public void addShapeRandom(Shape.Type type) {
        int[] randomCordinates = generateRandomXAndY();
        Shape shape = createShape(type, randomCordinates[0], randomCordinates[1]);
        upDateCanvas(shape);
    }

    @NonNull
    private Shape createShape(Shape.Type type, int x, int y) {

        Shape shape = new Shape(x, y, Constants.INSTANCE.getRADIUS());
        shape.setType(type);
        return shape;
    }

    public void undo() {

        if (getHistoryList().size() > 0) {
            actionSequence--;
            Shape toDeleteShape = getHistoryList().getLast();
            if (toDeleteShape.getLastTranformationIndex() != Constants.INSTANCE.getACTION_CREATE()) {
                int lastVisibleIndex = toDeleteShape.getLastTranformationIndex();
                if(lastVisibleIndex < getHistoryList().size()) {
                    Shape lastVisibleShape = getHistoryList().get(lastVisibleIndex);
                    if (lastVisibleShape != null) {
                        lastVisibleShape.setVisibility(true);
                        getHistoryList().set(lastVisibleIndex, lastVisibleShape);
                    }
                }
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

    private LinkedList<Shape> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(LinkedList<Shape> historyList) {
        this.historyList = historyList;
    }

    /*
   Remove all items of a shape
    */
    public void deleteAllByShape(Shape.Type shapeType) {

        Iterator<Shape> itr = getHistoryList().iterator();
        while (itr.hasNext()) {
            Shape shape = itr.next();
            if (shape.getType() == shapeType) {
                itr.remove();
            }
        }
    }
    /*
    Get all items in list , grouped by shape
     */
    public HashMap<Shape.Type, Integer> getCountByGroup() {
        HashMap<Shape.Type, Integer> shapeTypeCountMap = new HashMap<>();
        for (Shape shape : getHistoryList()) {
            if (shape.isVisible()) {
                Shape.Type shapeType = shape.getType();
                int existingCnt = 0;
                if (shapeTypeCountMap.containsKey(shape.getType()))
                    existingCnt = shapeTypeCountMap.get(shape.getType());
                existingCnt++;
                shapeTypeCountMap.put(shapeType, existingCnt);
            }
        }
        return shapeTypeCountMap;
    }

    public CustomView getCanvas() {
        return canvas;
    }

    public void setCanvas(CustomView canvas) {
        this.canvas = canvas;
    }

    public Context getmContext() {
        return mContext;
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


    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

}
