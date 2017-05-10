import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by silasa on 4/26/17.
 */
public class UpdatePool {
    private static UpdatePool ourInstance = new UpdatePool();
    private HashMap<UUID, ArrayList> pool = new HashMap<>();

    private UpdatePool() {
    }

    public static UpdatePool getInstance() {
        return ourInstance;
    }

    public void subscribe(UUID uuid) {
        pool.put(uuid, new ArrayList<JulfProtocol.ServerMessage>());
        System.out.println(pool);
    }

    public void unSubscribe(UUID uuid) {
        pool.remove(uuid);
    }

    public ArrayList<JulfProtocol.ServerMessage> getUpdates(UUID uuid) {
        //Return updates and clear them from the pool entry
        ArrayList<JulfProtocol.ServerMessage> updates = (ArrayList) pool.get(uuid).clone();
        pool.replace(uuid, new ArrayList<JulfProtocol.ServerMessage>());
        //System.out.println(updates);
        return updates;
    }

    public void addObjectUpdate(JulfProtocol.UpdateWorldObject worldObjectUpdate) {
        JulfProtocol.ServerMessage.Builder serverMessageBuilder = JulfProtocol.ServerMessage.newBuilder();
        serverMessageBuilder.addUpdateWorldObjectMessages(worldObjectUpdate);
        //System.out.println(worldObjectUpdate.getObjectId()+worldObjectUpdate.getXPos());
        //For each pool entry
        for (HashMap.Entry<UUID, ArrayList> entry :
                pool.entrySet()) {
            //Add the update if the id is not the same as the subscriber
            if (!entry.getKey().equals(UUID.fromString(worldObjectUpdate.getObjectId()))) {
                entry.getValue().add(serverMessageBuilder.build());
            }
        }
    }

    public void addObjectUpdate(JulfProtocol.DestroyWorldObject worldObjectUpdate) {
        JulfProtocol.ServerMessage.Builder serverMessageBuilder = JulfProtocol.ServerMessage.newBuilder();
        serverMessageBuilder.addDestroyWorldObjectMessages(worldObjectUpdate);
        //For each pool entry
        for (HashMap.Entry<UUID, ArrayList> entry :
                pool.entrySet()) {
                entry.getValue().add(serverMessageBuilder.build());
        }
    }

    public void addMoveUpdate(String playerId, double x, double y, double rotation){
        WorldObject playerObject = WorldObjectHandler.getInstance().getWorldObjectById(playerId);
        playerObject.setPosition(x,y,rotation);
        JulfProtocol.UpdateWorldObject.Builder uWO = JulfProtocol.UpdateWorldObject.newBuilder()
                .setObjectId(playerId).setXPos(x)
                .setYPos(y).setRotation(rotation);
        addObjectUpdate(uWO.build());
    }

    public void addCreateUpdate(WorldObject newObject){
        WorldObjectHandler.getInstance().addWorldObject(newObject);
        JulfProtocol.UpdateWorldObject.Builder uWO = JulfProtocol.UpdateWorldObject.newBuilder()
                .setObjectId(newObject.getUid().toString())
                .setTypeId(newObject.getTypeID())
                .setXPos(newObject.getXPos())
                .setYPos(newObject.getYPos())
                .setRotation(newObject.getRotation());
        addObjectUpdate(uWO.build());
    }

    public void addDestroyUpdate(UUID objectId){
        JulfProtocol.DestroyWorldObject.Builder dWO = JulfProtocol.DestroyWorldObject.newBuilder()
                .setObjectId(objectId.toString());
        addObjectUpdate(dWO.build());
        WorldObjectHandler.getInstance().destroyWorldObject(objectId.toString());
    }

}
