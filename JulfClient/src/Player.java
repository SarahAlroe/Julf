import java.awt.geom.Point2D;

/**
 * Created by silasa on 8/20/16.
 */
class Player implements GameEventListener {

    public static Player getInstance() {
        return instance;
    }


    private static Player instance = new Player(0d,0d);
    private double orientation;
    private double x;
    private double y;
    private boolean isMovingForward, isMovingBackwards, isMovingLeft, isMovingRight, isRotatingLeft, isRotatingRight;
    private WorldMap map;
    private int verticalOrientation;
    private double headBobPosition;
    private int headBobAmplitude = 10;
    private double headBobSpeed = 0.1;
    private Config config = Config.getInstance();

    public Player(double xpos, double ypos) {
        x = xpos;
        y = ypos;
        orientation = 0;
        GameEventBroadcaster.getInstance().addEventListener(this);
    }

    public Player(double xpos, double ypos, double orient) {
        x = xpos;
        y = ypos;
        orientation = orient;
    }

    public void setMap(WorldMap map) {
        this.map = map;
    }

    public double[] getPos() {
        return new double[]{x, y};
    }

    public Point2D.Double getPoint() {return new Point2D.Double(x,y);}

    public double getOrientation() {
        return orientation;
    }

    public void setPos(double newX, double newY) {
        x = newX;
        y = newY;
    }

    public void toggleMovement(Direction direction, boolean isMoving) {
        switch (direction) {
            case MOVE_FORWARD:
                isMovingForward = isMoving;
                break;
            case MOVE_BACKWARDS:
                isMovingBackwards = isMoving;
                break;
            case MOVE_LEFT:
                isMovingLeft = isMoving;
                break;
            case MOVE_RIGHT:
                isMovingRight = isMoving;
                break;
            case TURN_LEFT:
                isRotatingLeft = isMoving;
                break;
            case TURN_RIGHT:
                isRotatingRight = isMoving;
                break;
        }
    }

    public int getVerticalOrientation() {
        return verticalOrientation;
    }

    @Override
    public void onGameTick() {
        if (config.isHeadBobbing()) {
            moveHead();
        }
        if (isMovingForward) {
            moveForward();
        }
        if (isMovingBackwards) {
            moveBackwards();
        }
        if (isMovingLeft) {
            strafeLeft();
        }
        if (isMovingRight) {
            strafeRight();
        }
        if (isRotatingLeft) {
            turnLeft();
        }
        if (isRotatingRight) {
            turnRight();
        }
    }

    private void moveHead() {
        if (isMovingForward || isMovingBackwards || isMovingRight || isMovingLeft || headBobPosition > 0.1) {
            headBobPosition += headBobSpeed;
            headBobPosition = headBobPosition % 2d;
            verticalOrientation = (int) (headBobAmplitude * Math.sin(headBobPosition * Math.PI));
        }
    }

    public void moveForward() {
        double addX = Math.sin(orientation) * Config.PLAYER_SPEED;
        double addY = Math.cos(orientation) * Config.PLAYER_SPEED;
        move(addX, addY);
    }

    public void moveBackwards() {
        double addX = Math.sin(orientation) * Config.PLAYER_SPEED;
        double addY = Math.cos(orientation) * Config.PLAYER_SPEED;
        move(-addX, -addY);
    }

    public void strafeLeft() {
        double newOrientation = orientation + Math.toRadians(90);
        double addX = Math.sin(newOrientation) * Config.PLAYER_SPEED;
        double addY = Math.cos(newOrientation) * Config.PLAYER_SPEED;
        move(-addX, -addY);
    }

    public void strafeRight() {
        double newOrientation = orientation + Math.toRadians(90);
        double addX = Math.sin(newOrientation) * Config.PLAYER_SPEED;
        double addY = Math.cos(newOrientation) * Config.PLAYER_SPEED;
        move(addX, addY);
    }

    public void turnLeft() {
        rotate(-Config.PLAYER_TURNSPEED);
    }

    public void turnRight() {
        rotate(Config.PLAYER_TURNSPEED);
    }

    void move(double addX, double addY) {
        double resX = x + addX;
        double resY = y + addY;
        if (!map.hasTile((int) resX, (int) y)) {
            x = resX;
        }
        if (!map.hasTile((int) x, (int) resY)) {
            y = resY;
        }
    }

    public void rotate(double angle) {
        orientation = MathUtils.angleClamp(orientation+angle);

    }
}
