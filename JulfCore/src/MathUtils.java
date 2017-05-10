/**
 * Created by silasa on 8/30/16.
 */
public class MathUtils {
    public static double angleClamp(double inAngle){
        boolean isClamped = false;
        while (!isClamped) {
            if (inAngle < 0) {
                inAngle += Math.PI * 2;
            } else if (inAngle > Math.PI * 2) {
                inAngle -= Math.PI * 2;
            }
            else {
                isClamped = true;
            }
        }
        return inAngle;
    }
}
