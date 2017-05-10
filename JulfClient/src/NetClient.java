import java.awt.geom.Point2D;
import java.io.*;
import java.net.*;
import java.util.UUID;

/**
 * Created by silasa on 4/26/17.
 */

public class NetClient extends Thread{
    protected Julf julf;

    public NetClient(Julf julf){this.julf = julf;}

    public void run(){
        String hostName = "localhost";
        int portNumber = Config.getInstance().getPort();
        long lastPing = 0;

        UUID myId = null;

        try {
            Socket socket = new Socket(hostName, portNumber);
            OutputStream out = new DataOutputStream(socket.getOutputStream());
            InputStream in = socket.getInputStream();

            JulfProtocol.ClientMessage.Builder initMessage = JulfProtocol.ClientMessage.newBuilder();
            initMessage.setRequestInit(JulfProtocol.RequestInit.newBuilder().setPing(JulfProtocol.Ping.newBuilder().setTimeStamp(System.currentTimeMillis())));
            initMessage.build().writeDelimitedTo(out);

            while (true) {
                if (in.available()>0) {
                    JulfProtocol.ClientMessage.Builder newMsg = JulfProtocol.ClientMessage.newBuilder();
                    JulfProtocol.ServerMessage serverMessage = JulfProtocol.ServerMessage.parseDelimitedFrom(in);
                    lastPing = System.currentTimeMillis();
                    newMsg.setPing(JulfProtocol.Ping.newBuilder().setTimeStamp(lastPing));

                    if (serverMessage.hasPing()) {
                        JulfProtocol.Pong.Builder pong = JulfProtocol.Pong.newBuilder();
                        pong.setTimestamp(serverMessage.getPing().getTimeStamp());
                        newMsg.setPong(pong);
                    }

                    if (serverMessage.hasPong()) {
                        //Do something with this
                        int latency = (int)(System.currentTimeMillis()-lastPing);
                    }

                    if (serverMessage.hasInit()){
                        julf.initGameElements(serverMessage.getInit().getMapName());
                    }

                    if (serverMessage.hasResetPlayerMessage()){
                        JulfProtocol.ResetPlayer resetPlayer = serverMessage.getResetPlayerMessage();
                        julf.getPlayer().setPos(resetPlayer.getXPos(),resetPlayer.getYPos());
                        myId = UUID.fromString(resetPlayer.getPlayerId());
                    }

                    for (JulfProtocol.UpdateWorldObject updateWorldObject :
                            serverMessage.getUpdateWorldObjectMessagesList()) {
                        WorldObject object = new WorldObject(new Point2D.Double(updateWorldObject.getXPos(),updateWorldObject.getYPos()),updateWorldObject.getTypeId(),updateWorldObject.getObjectId());
                        //System.out.println(object.getUid()+" - "+object.getYPos());
                        if (WOTypeHandler.getInstance().objectTypeExists(updateWorldObject.getTypeId())) {
                            WorldObjectHandler.getInstance().addWorldObject(object);
                            System.out.println("Adding object");
                        }else {
                            WorldObjectHandler.getInstance().updateWorldObject(object);
                            System.out.println("Updating objecte");
                        }

                    }

                    for (JulfProtocol.DestroyWorldObject destroyWorldObject :
                            serverMessage.getDestroyWorldObjectMessagesList()) {
                        WorldObjectHandler.getInstance().destroyWorldObject(destroyWorldObject.getObjectId());
                    }

                    if (myId!=null) {
                        newMsg.setMove(JulfProtocol.Move.newBuilder()
                                .setPlayerId(myId.toString())
                                .setXPos(julf.getPlayer().getPos()[0])
                                .setYPos(julf.getPlayer().getPos()[1])
                                .setRotation(julf.getPlayer().getOrientation()).build());
                        newMsg.setRequestUpdate(JulfProtocol.RequestUpdate.newBuilder()
                        .setPlayerId(myId.toString()).build());
                    }

                    newMsg.build().writeDelimitedTo(out);

                }


            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}