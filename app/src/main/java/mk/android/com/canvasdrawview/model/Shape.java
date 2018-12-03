package mk.android.com.canvasdrawview.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class Shape implements Parcelable{
    public static final int ACTION_CREATE = -1;
    private int x;
    private int y;
    private int width;
    private Type type;
    private boolean visible = true;
    private int actionNumber = 0;
    private int lastTranformIndex = ACTION_CREATE;

    public Shape(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public enum Type {
        CIRCLE(0), SQUARE(1), TRIANGLE(2);
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

    public int getLastTranformIndex() {
        return lastTranformIndex;
    }

    public void setLastTranformIndex(int lastTranformIndex) {
        this.lastTranformIndex = lastTranformIndex;
    }
    protected Shape(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        width = in.readInt();
        visible = in.readByte() != 0;
        actionNumber = in.readInt();
        lastTranformIndex = in.readInt();
    }

    public static final Creator<Shape> CREATOR = new Creator<Shape>() {
        @Override
        public Shape createFromParcel(Parcel in) {
            return new Shape(in);
        }

        @Override
        public Shape[] newArray(int size) {
            return new Shape[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(width);
        dest.writeByte((byte) (visible ? 1 : 0));
        dest.writeInt(actionNumber);
        dest.writeInt(lastTranformIndex);
    }

}
