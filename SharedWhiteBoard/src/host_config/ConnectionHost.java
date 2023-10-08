package host_config;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ConnectionHost {
    public static synchronized int checkIn(String name) {
        return Create.WhiteBoard.showRequest(name);
    }

    public static synchronized void addUser(String[] clients) {

        Create.WhiteBoard.list.setListData(clients);
    }

    public static void clientOut(String clients) {
        String[] k = clients.split(" ", 2);
        JOptionPane.showMessageDialog(Create.WhiteBoard.frame, "User " + k[0] + " leaves!");
        String[] client = k[1].split(" ");
        Create.WhiteBoard.list.setListData(client);
    }

    public static synchronized void canvasRepaint(String drawRecord) {
        Host.DrawListener.update(drawRecord);
        Host.canvas.repaint();
    }

    public static void broadcast(String message) {
        try {
            for (int i = 0; i < Server.connections.size(); i++) {
                Connection st = Server.connections.get(i);
                st.dos.writeUTF(message);
                st.dos.flush();
            }
        } catch (IOException e) {
            System.out.println("Host error" + "\n");
            System.exit(1);
        }
    }

    public static void broadcastBatch(ArrayList<String> recordList) {
        try {
            String[] recordArray = recordList.toArray(new String[recordList.size()]);
            for (String message : recordArray) {
                for (int i = 0; i < Server.connections.size(); i++) {
                    Connection st = Server.connections.get(i);
                    st.dos.writeUTF("draw " + message);
                    st.dos.flush();
                }
            }

        } catch (IOException e1) {
            System.out.println("Host error" + "\n");
            System.exit(1);
        }

    }
}
