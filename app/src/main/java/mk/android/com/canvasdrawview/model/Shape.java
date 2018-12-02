package mk.android.com.canvasdrawview.model;

import java.util.ArrayList;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class Shape {
    int x;
    int y;
    int width;
    Type type;
    ArrayList<Shape> transformationList = new ArrayList<>();
    boolean visible = true;
    int actionNumber = 0;

    public Shape(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public enum Type {
        CIRCLE(0), RECTANGLE(1), TRIANGLE(2);
        public final int value;

        Type(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ArrayList<Shape> getTransformationList() {
        return transformationList;
    }

    public void setTransformationList(ArrayList<Shape> transformationList) {
        this.transformationList = transformationList;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisibility(boolean visibility) {
        this.visible = visibility;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int actionNumber) {
        this.actionNumber = actionNumber;
    }
}
