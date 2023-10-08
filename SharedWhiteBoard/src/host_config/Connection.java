package host_config;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Connection extends Thread {
    public String name;
    public Socket socket;
    public DataInputStream dis;
    public DataOutputStream dos;
    public boolean kick = false;


    public Connection(Socket client) {
        this.socket = client;
    }


    public void run() {
        try {
            InputStream ins = socket.getInputStream();
            OutputStream ous = socket.getOutputStream();
            dis = new DataInputStream(ins);
            dos = new DataOutputStream(ous);
            String str1;
            label:
            while ((str1 = dis.readUTF()) != null) {
                String[] request = str1.split(" ", 2);
                switch (request[0]) {
                    case "joined":
                        ArrayList<String> rl = Create.WhiteBoard.DrawListener.getRecord();
                        try {
                            ConnectionHost.broadcastBatch(rl);
                        } catch (Exception e1) {
                            System.out.println("Host error");
                        }
                        String str = "userlist";
                        for (String userName : Server.usernames) {
                            str += " " + userName;
                        }
                        String[] k = str.split(" ", 2);
                        String[] clients = k[1].split(" ");
                        ConnectionHost.addUser(clients);
                        ConnectionHost.broadcast(str);
                        break;
                    case "request":
                        String client_name = request[1];
                        name = client_name;
                        if (Server.usernames.contains(client_name)) {
                            dos.writeUTF("no");
                            dos.flush();
                        } else {
                            int ans = ConnectionHost.checkIn(request[1]);
                            if (ans == JOptionPane.YES_OPTION) {
                                if (Server.usernames.contains(client_name)) {
                                    try {
                                        dos.writeUTF("no");
                                        dos.flush();
                                        Server.connections.remove(this);
                                        socket.close();
                                        break;
                                    } catch (Exception e1) {
                                        Server.connections.remove(this);
                                    }
                                } else {
                                    Server.usernames.add(client_name);
                                    dos.writeUTF("yes");
                                    dos.flush();
                                }
                            } else if (ans == JOptionPane.CANCEL_OPTION || ans == JOptionPane.CLOSED_OPTION || ans == JOptionPane.NO_OPTION) {
                                dos.writeUTF("refused");
                                dos.flush();
                                Server.connections.remove(this);
                            }
                        }
                        break;
                    case ("draw"):
                        ConnectionHost.broadcast(str1);
                        ConnectionHost.canvasRepaint(request[1]);
                        break;
                    case ("over"):
                        socket.close();
                        break label;
                    case ("chat"):
                        ConnectionHost.broadcast(str1);
                        Host.chatArea.append(request[1] + "\n" + "\n");
                        break;
                }
                if (request[0].equals("new")) {
                    Host.canvas.removeAll();
                    Host.canvas.updateUI();
                    Host.DrawListener.clearRecord();
                }
            }
        } catch (SocketException e) {
            System.out.println("User " + this.name + " Connection interruption.");
            if (!this.kick) {
                client_leave();
            }

        } catch (Exception E) {
            System.out.println("User " + this.name + " Connection interruption");
        }
    }

    public void client_leave() {
        Server.connections.remove(this);
        Server.usernames.remove(name);
        String str = "out " + name;
        for (String userName : Server.usernames) {
            str += " " + userName;
        }
        ConnectionHost.broadcast(str);
        String str1 = str.split(" ", 2)[1];
        ConnectionHost.clientOut(str1);
    }

}
