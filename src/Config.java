/**
 * Created by silasa on 8/20/16.
 */
class Config {
    public static final double PLAYER_TURNSPEED = 0.03;
    public static final double PLAYER_SPEED = 0.1;
    private static Config instance = new Config();
    private int fov = 75;
    private double maxLength = 30.0;
    private double incLength = 0.01;
    private int emptyColor = -16777216;
    private boolean headBobbing = false;

    public static Config getInstance() {
        return instance;
    }

    public boolean isHeadBobbing() {
        return headBobbing;
    }

    public void setHeadBobbing(boolean headBobbing) {
        this.headBobbing = headBobbing;
    }

    public int getFov() {
        return fov;
    }

    public void setFov(int fov) {
        this.fov = fov;
    }

    public double getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(double maxLength) {
        this.maxLength = maxLength;
    }

    public double getIncLength() {
        return incLength;
    }

    public void setIncLength(double incLength) {
        this.incLength = incLength;
    }

    public int getEmptyColor() {
        return emptyColor;
    }

    public void setEmptyColor(int emptyColor) {
        this.emptyColor = emptyColor;
    }
}
