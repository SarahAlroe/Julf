import java.awt.geom.Point2D;
import org.json.*;

/**
 * Created by silasa on 8/30/16.
 */
public class WorldObject implements Comparable<WorldObject>{
    private Point2D.Double objectPosition;
    private int typeID;
    public WorldObject(Point2D.Double objectPosition, int typeID){
        this.objectPosition=objectPosition;
        this.typeID = typeID;
    }

    public WorldObject(String jSONAnno){
        JSONObject data = new JSONObject(jSONAnno);
        this.objectPosition=new Point2D.Double(data.getDouble("xPos"),data.getDouble("yPos"));
        this.typeID = data.getInt("typeID");
    }

    public int getTypeID() {
        return typeID;
    }
    public double getDistanceFrom(Point2D.Double otherPoint){
        return objectPosition.distance(otherPoint);
    }
    public double getAngleFrom(Point2D.Double otherPoint){
        Point2D refPoint = new Point2D.Double(objectPosition.getX()-otherPoint.getX(), objectPosition.getY()-otherPoint.getY());
        return (MathUtils.angleClamp(-1*Math.atan2(refPoint.getY(),refPoint.getX())+Math.PI/2));
    }
    public boolean isVisibleFrom(Point2D.Double otherPoint, double angle, double fovRadians){
        if (getDistanceFrom(otherPoint)>Config.getInstance().getMaxLength()){
            return false;
        }
        double itemAngle= getAngleFrom(otherPoint);
        double minAngle = MathUtils.angleClamp(angle-(fovRadians/4));
        double maxAngle = MathUtils.angleClamp(angle+(fovRadians));
        if (minAngle<maxAngle){
            return ((itemAngle>minAngle) && (itemAngle<maxAngle));
        } else{
            return ((itemAngle>minAngle) || (itemAngle<maxAngle));
        }
    }

    @Override
    public int compareTo(WorldObject o) {
        Player player = Player.getInstance();
        return (int) (o.getDistanceFrom(player.getPoint())-getDistanceFrom(player.getPoint()));
    }
}
