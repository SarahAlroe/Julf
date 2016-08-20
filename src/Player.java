/**
 * Created by silasa on 8/20/16.
 */
class Player {
    public static final double PLAYER_TURNSPEED = 0.075;
    private double orientation;
    private double x;
    private double y;
    public static final double PLAYER_SPEED = 0.2;

    public Player(double xpos, double ypos) {
        x = xpos;
        y = ypos;
        orientation = 0;
    }

    public Player(double xpos, double ypos, double orient) {
        x = xpos;
        y = ypos;
        orientation = orient;
    }

    public double[] getPos() {
        return new double[]{x, y};
    }

    public double getOrientation() {
        return orientation;
    }

    public void setPos(double newX, double newY) {
        x = newX;
        y = newY;
    }

    void move(double addX, double addY) {
        x += addX;
        y += addY;
    }

    public void rotate(double angle) {
        orientation += angle;

    }

    public void moveForward() {
        double addX = Math.sin(orientation)* PLAYER_SPEED;
        double addY = Math.cos(orientation)* PLAYER_SPEED;
        move(addX,addY);
    }
    public void moveBackwards() {
        double addX = Math.sin(orientation)* PLAYER_SPEED;
        double addY = Math.cos(orientation)* PLAYER_SPEED;
        move(-addX,-addY);
    }

    public void turnLeft() {
        rotate(-PLAYER_TURNSPEED);
    }
    public void turnRight() {
        rotate(PLAYER_TURNSPEED);
    }

    public void strafeLeft() {
        double newOrientation = orientation+Math.toRadians(90);
        double addX = Math.sin(newOrientation)* PLAYER_SPEED;
        double addY = Math.cos(newOrientation)* PLAYER_SPEED;
        move(-addX,-addY);
    }
    public void strafeRight() {
        double newOrientation = orientation+Math.toRadians(90);
        double addX = Math.sin(newOrientation)* PLAYER_SPEED;
        double addY = Math.cos(newOrientation)* PLAYER_SPEED;
        move(addX,addY);
    }
}
