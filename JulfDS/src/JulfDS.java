import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by silasa on 3/27/17.
 */
public class JulfDS {
    WorldMap worldMap;
    public static void main(String[] args) {
        String mapName = args[0];
        Config.getInstance().setMapName(mapName);
        JulfDS julfDS = new JulfDS(mapName);
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(Config.getInstance().getPort());
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            //Start new thread for a client
            System.out.println("New client");
            new HostThread(socket).start();
        }
    }

    private void loadMap(String mapName) {
        worldMap = new WorldMap(mapName);
        WOTypeHandler.getInstance().loadMapWorldObjectTypes(mapName);
        WorldObjectHandler.getInstance().addObjectsFromMap(mapName);
    }
    public JulfDS(String mapName){
        loadMap(mapName);

        //Start server
    }
}
