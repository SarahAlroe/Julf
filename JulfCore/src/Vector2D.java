import java.awt.geom.Dimension2D;

/**
 * Created by silasa on 8/21/16.
 */
public class Vector2D extends Dimension2D {
    public double width;
    public double height;

    public Vector2D(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
