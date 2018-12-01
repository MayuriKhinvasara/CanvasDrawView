package mk.android.com.canvasdrawview.model;

/**
 * Created by Mayuri Khinvasara on 01,December,2018
 */
public class Shape {
    int x;
    int y;
    int width;
    Type type;

    public Shape(int x, int y , int width) {
        this.x = x;
        this.y = y;
        this.width =width;
    }

    public enum Type
    {
        CIRCLE(0),RECTANGLE(1),TRIANGLE(2);

        public final int value;

        Type(int i) {
            this.value = i;

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
}
