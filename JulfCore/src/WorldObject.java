import java.awt.geom.Point2D;
import java.util.UUID;

import org.json.*;

/**
 * Created by silasa on 8/30/16.
 */
public class WorldObject implements Comparable<WorldObject>{
    private Point2D.Double objectPosition;
    private int typeID;
    private UUID uid;
    private double objectRotation;

    public WorldObject(Point2D.Double objectPosition, int typeID){
        this.objectPosition=objectPosition;
        this.typeID = typeID;
        this.uid = UUID.randomUUID();
    }

    public WorldObject(Point2D.Double objectPosition, int typeID, String uuid){
        this.objectPosition=objectPosition;
        this.typeID = typeID;
        this.uid = UUID.fromString(uuid);
        System.out.println(this.objectPosition.toString()+this.typeID+this.uid);
    }

    public WorldObject(String jSONAnno){
        this(jSONAnno,UUID.randomUUID().toString());
    }

    public WorldObject(String jSONAnno, String uuid){
        JSONObject data = new JSONObject(jSONAnno);
        this.objectPosition=new Point2D.Double(data.getDouble("xPos"),data.getDouble("yPos"));
        this.typeID = data.getInt("typeID");
        this.uid = UUID.fromString(uuid);
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

    public UUID getUid() {
        return uid;
    }

    public double getXPos(){
        return objectPosition.getX();
    }

    public double getYPos(){
        return objectPosition.getY();
    }

    public double getRotation(){
        return objectRotation;
    }

    @Override
    public int compareTo(WorldObject o) {
        Player player = Player.getInstance();
        return (int) (o.getDistanceFrom(player.getPoint())-getDistanceFrom(player.getPoint()));
    }

    public void setPosition(double xPos, double yPos, double rotation) {
        this.objectPosition = new Point2D.Double(xPos,yPos);
        this.objectRotation = rotation;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }
}
