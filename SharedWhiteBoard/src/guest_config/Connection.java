package guest_config;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Connection {
    private Socket socket;
    public DataInputStream dis;
    public DataOutputStream dos;
    String status;
    boolean kick = false;

    public void launch() {
        try {
            while (true) {
                String request = dis.readUTF();
                String[] Request = request.split(" ", 2);
                if (Request[0].equals("draw")) {
                    try {
                        Guest.listener.update(Request[1]);
                        Guest.canvas.repaint();
                    } catch (Exception e1) {
                    }
                }
                if (Request[0].equals("chat")) {
                    Join.guest.guestChatArea.append(Request[1] + "\n" + "\n");
                }
                if (Request[0].equals("userlist") && Join.guest == null) {
                    try {
                        Guest.userList = Request[1].split(" ");
                    } catch (Exception e) {
                        System.out.println("waiting for user list setup1");
                    }
                }

                if (Request[0].equals("userlist") && Join.guest != null) {
                    Join.guest.list.setListData(Request[1].split(" "));
                    try {
                        Guest.userList = Request[1].split(" ");
                    } catch (Exception e) {
                        System.out.println("waiting for user list setup2");
                    }
                }
                if (Request[0].equals("delete")) {
                    String[] users = Request[1].split(" ", 2);
                    String[] userList = users[1].split(" ");
                    JOptionPane.showMessageDialog(Join.guest.frame, users[0] + " has been kicked out by Host.");
                    Join.guest.list.setListData(userList);
                }
                if (Request[0].equals("kick")) {
                    kick = true;
                    JOptionPane.showMessageDialog(Join.guest.frame, "You have been kicked out by manager.");
                }

                if (Request[0].equals("no")) {
                    status = "no";
                }

                if (Request[0].equals("yes")) {
                    status = "yes";
                }

                if (Request[0].equals("refused")) {
                    status = "refused";
                }

                if (Request[0].equals("out")) {
                    String[] users = Request[1].split(" ",2);
                    JOptionPane.showMessageDialog(Join.guest.frame, "User " + users[0] + " leaves!");
                    String[] userList = users[1].split(" ");
                    Join.guest.list.setListData(userList);
                }

                if (Request[0].equals("new")) {
                    Guest.canvas.removeAll();
                    Guest.canvas.updateUI();
                    Guest.listener.clearRecord();
                    JOptionPane.showMessageDialog(Join.guest.frame, "Host opens a new Whiteboard");
                }
            }

        } catch (IOException e1) {
            try {
                if (!kick) {
                    JOptionPane.showMessageDialog(Join.guest.frame, "Whiteboard is closed");
                }
            } catch (Exception e) {
                System.out.println("Whiteboard is closed.");
            }
            System.exit(0);
        }
    }

    public Connection(Socket socket) {
        resetStatus();
        try {
            this.socket = socket;
            dos = new DataOutputStream(this.socket.getOutputStream());
            dis = new DataInputStream(this.socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Connection failed.");
        }
    }

    public String getCurrentStatus() {
        return status;
    }

    public void resetStatus() {
        status = "wait";
    }
}
