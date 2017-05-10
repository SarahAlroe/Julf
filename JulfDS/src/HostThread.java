import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by silasa on 4/5/17.
 */
public class HostThread extends Thread {
    protected Socket socket;

    public HostThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        UUID clientId = null;
        InputStream inp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                JulfProtocol.ClientMessage clientMessage = JulfProtocol.ClientMessage.parseDelimitedFrom(inp);
                JulfProtocol.ServerMessage.Builder messageBuilder = JulfProtocol.ServerMessage.newBuilder();

                //Send ping
                messageBuilder.setPing(JulfProtocol.Ping.newBuilder().setTimeStamp(System.currentTimeMillis()));

                if (clientMessage.hasPing()) {
                    JulfProtocol.Pong.Builder pong = JulfProtocol.Pong.newBuilder();
                    pong.setTimestamp(clientMessage.getPing().getTimeStamp());
                    messageBuilder.setPong(pong);
                }

                if (clientMessage.hasPong()) {
                    //System.out.println("Pong: " + new Timestamp(clientMessage.getPong().getTimestamp()).toString());
                }

                if (clientMessage.hasMove()) {
                    JulfProtocol.Move move = clientMessage.getMove();
                    UpdatePool.getInstance().addMoveUpdate(move.getPlayerId(),move.getXPos(),move.getYPos(),move.getRotation());
                }

                if (clientMessage.hasRequestInit()) {
                    System.out.println("Plz init");
                    System.out.println(clientMessage.getRequestInit().getPing().getTimeStamp());
                    //Init level
                    messageBuilder.setInit(JulfProtocol.Init.newBuilder().setMapName(Config.getInstance().getMapName()));
                    //Load objects
                    ArrayList<WorldObject> worldObjects = WorldObjectHandler.getInstance().getWorldObjects();
                    for (WorldObject worldObject :
                            worldObjects) {
                        JulfProtocol.UpdateWorldObject.Builder updateWorldObjectMessage = JulfProtocol.UpdateWorldObject.newBuilder()
                                .setObjectId(worldObject.getUid().toString())
                                .setTypeId(worldObject.getTypeID())
                                .setXPos(worldObject.getXPos())
                                .setYPos(worldObject.getYPos())
                                .setRotation(worldObject.getRotation());
                        messageBuilder.addUpdateWorldObjectMessages(updateWorldObjectMessage);
                    }
                    //Create user object
                    WorldObject user = new WorldObject(new Point2D.Double(6, 6), Color.decode("#00ffff").getRGB());
                    UpdatePool.getInstance().addCreateUpdate(user);
                    clientId=user.getUid();
                    //Subscribe
                    UpdatePool.getInstance().subscribe(user.getUid());
                    //Reset user
                    JulfProtocol.ResetPlayer.Builder resetPlayer = JulfProtocol.ResetPlayer.newBuilder()
                            .setPlayerId(user.getUid().toString())
                            .setXPos(user.getXPos())
                            .setYPos(user.getYPos())
                            .setRotation(0);
                    messageBuilder.setResetPlayerMessage(resetPlayer);
                }
                //Add updates from updatepool if requested
                if (clientMessage.hasRequestUpdate()) {
                    UUID playerUuid = UUID.fromString(clientMessage.getRequestUpdate().getPlayerId());
                    ArrayList<JulfProtocol.ServerMessage> updates = UpdatePool.getInstance().getUpdates(playerUuid);
                    for (JulfProtocol.ServerMessage serverUpdate : updates
                            ) {
                        messageBuilder.mergeFrom(serverUpdate);
                    }
                }
                messageBuilder.build().writeDelimitedTo(out);

            } catch (IOException e) {
                if (clientId!=null) {
                    UpdatePool.getInstance().unSubscribe(clientId);
                    UpdatePool.getInstance().addDestroyUpdate(clientId);
                }
                e.printStackTrace();
                return;
            }
        }
    }
}
