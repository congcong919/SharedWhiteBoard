package guest_config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Join {
    static String address;
    static int port;
    static String username;
    public static Connection connection;
    public static Guest guest;
    public static Socket socket;
    private static JFrame frame;
    private JTextField textField;

    public static void main(String[] args){
        if (args.length == 3){
            try {
                address = args[0];
                port = Integer.parseInt(args[1]);
                username = args[2];
            } catch (Exception e){
                System.out.println("Command error" + "\n");
                System.exit(1);
            }
        } else {
            System.out.println("Please give correct format of command");
            System.exit(1);
        }

        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            System.out.println("No Whiteboard opened" + "\n");
            System.out.println("Check IP address and port" + "\n");
            System.exit(1);
        }
        connection = new Connection(socket);
        EventQueue.invokeLater(() -> {
            try {
                new Join();
            } catch (Exception e){
                System.out.println("Initial setup error" + "\n");
                System.exit(1);
            }
        });
        connection.launch();
    }

    public Join() {
        initialize();
    }

    public void initialize() {
        Listener listener = new Listener();
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblNewLabel = new JLabel("Please enter your name");
        lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
        lblNewLabel.setBounds(115, 26, 200, 44);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(104, 86, 231, 46);
        frame.getContentPane().add(textField);
        textField.setText(username);

        JButton Join_button = new JButton("Join");
        Join_button.setBounds(160, 174, 117, 44);
        Join_button.setFont(new Font("Calibri", Font.PLAIN, 20));
        Join_button.addActionListener(listener);
        Join_button.addActionListener(e -> {
            if (e.getActionCommand().equals("Join")) {
                try {
                    username = textField.getText();
                    connection.dos.writeUTF("request " + username);
                    int time = 0;
                    while (connection.getCurrentStatus().equals("wait") && time < 10000) {
                        TimeUnit.MILLISECONDS.sleep(100);
                        time += 100;
                    }

                    String allow = connection.getCurrentStatus();
                    switch (allow) {
                        case "no" -> {
                            JOptionPane.showMessageDialog(frame, "Username exists!");
                            connection.resetStatus();
                        }
                        case "refused" -> {
                            JOptionPane.showMessageDialog(frame, "Refused by Host!");
                            frame.dispose();
                            try {
                                connection.dos.writeUTF("over");
                                connection.dos.flush();
                                socket.close();
                                System.exit(1);
                            } catch (Exception e1) {
                                System.exit(1);
                            }
                        }
                        case "yes" -> {
                            frame.dispose();
                            try {
                                if (guest == null) {
                                    guest = new Guest(connection, username);
                                    try {
                                        connection.dos.writeUTF("joined");
                                    } catch (IOException e1) {
                                        System.out.println("Initial set up for user failed" + "\n");
                                        System.exit(1);
                                    }
                                }
                            } catch (Exception e1) {
                                System.out.println("Initial set up for user failed" + "\n");
                                System.exit(1);
                            }
                        }
                        default -> {
                            JOptionPane.showMessageDialog(frame, "Timeout");
                            socket.close();
                            System.exit(0);
                            frame.dispose();
                        }
                    }
                } catch (Exception e2) {
                    System.out.println("Failed to join" + "\n");
                    System.out.println("Try again later" + "\n");
                    System.exit(1);
                }
            }
        });
        frame.getContentPane().add(Join_button);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
