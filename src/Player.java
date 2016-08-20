/**
 * Created by silasa on 8/20/16.
 */
class Player implements GameEventListener{
    private double orientation;
    private double x;
    private double y;
    private boolean isMovingForward, isMovingBackwards, isMovingLeft, isMovingRight, isRotatingLeft, isRotatingRight;
    private WorldMap map;

    public Player(double xpos, double ypos) {
        x = xpos;
        y = ypos;
        orientation = 0;
        GameEventBroadcaster.getInstance().addEventListener(this);
    }

    public void setMap(WorldMap map) {
        this.map = map;
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
        double resX = x+addX;
        double resY = y+addY;
        if(!map.hasTile((int) resX, (int)resY)){
            x=resX;
            y=resY;
        }
    }

    public void rotate(double angle) {
        orientation += angle;

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

    public void turnLeft() {
        rotate(-Config.PLAYER_TURNSPEED);
    }

    public void turnRight() {
        rotate(Config.PLAYER_TURNSPEED);
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

    public void toggleMovement(Direction direction, boolean isMoving){
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

    @Override
    public void onGameTick() {
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
}
