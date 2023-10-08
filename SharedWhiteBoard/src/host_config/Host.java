package host_config;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Host {
    static Listener DrawListener;
    public JFrame frame;
    int width;
    int height;
    static Painter canvas;
    public JList list;
    public static JTextArea chatArea;

    public Host(String Name) {
        initialize(Name);
    }

    public int showRequest(String name) {
        return JOptionPane.showConfirmDialog(null, name + " wants to share your white board", "Confirm", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initialize(String Name) {
        frame = new JFrame();
        frame.setTitle("Distributed Whiteboard (Host): " + Name);
        frame.setBounds(100, 100, 1355, 644);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawListener = new Listener(frame);
        frame.getContentPane().setLayout(null);
        JComboBox menu = new JComboBox();
        menu.setBounds(60, 24, 76, 23);
        menu.setModel(new DefaultComboBoxModel(new String[]{"New", "Save", "Save as", "Open", "Close"}));
        menu.addActionListener(e -> {
            if (menu.getSelectedItem().toString().equals("New")) {
                canvas.removeAll();
                canvas.updateUI();
                DrawListener.clearRecord();
                ConnectionHost.broadcast("new");
            } else if (menu.getSelectedItem().toString().equals("Close")) {
                System.exit(1);
            } else if (menu.getSelectedItem().toString().equals("Open")) {
                OpenFile open = new OpenFile(Create.WhiteBoard);
                open.frame.setVisible(true);
            } else if (menu.getSelectedItem().toString().equals("Save")) {
                SaveFile save = new SaveFile(Create.WhiteBoard);
                save.frame.setVisible(true);
            } else if (menu.getSelectedItem().toString().equals("Save as")) {
                SaveAsFile saveAs = new SaveAsFile(Create.WhiteBoard);
                saveAs.frame.setVisible(true);
            }
        });
        frame.getContentPane().add(menu);

        JPanel toolPanel = new JPanel();
        toolPanel.setBounds(206, 10, 988, 50);
        toolPanel.setLayout(null);

        JButton LineButton = new JButton("Line");
        LineButton.setBounds(10, 14, 97, 23);
        LineButton.setActionCommand("Line");
        LineButton.addActionListener(DrawListener);
        toolPanel.add(LineButton);

        JButton CircleButton = new JButton("Circle");
        CircleButton.setBounds(136, 14, 97, 23);
        CircleButton.setActionCommand("Circle");
        CircleButton.addActionListener(DrawListener);
        toolPanel.add(CircleButton);

        JButton RectangleButton = new JButton("Rectangle");
        RectangleButton.setBounds(263, 14, 97, 23);
        RectangleButton.setActionCommand("Rectangle");
        RectangleButton.addActionListener(DrawListener);
        toolPanel.add(RectangleButton);

        JButton OvalButton = new JButton("Oval");
        OvalButton.setBounds(389, 14, 97, 23);
        OvalButton.setActionCommand("Oval");
        OvalButton.addActionListener(DrawListener);
        toolPanel.add(OvalButton);

        JButton TextButton = new JButton("A");
        TextButton.setActionCommand("Text");
        TextButton.addActionListener(DrawListener);
        TextButton.setBounds(514, 14, 97, 23);
        toolPanel.add(TextButton);

        JButton FreeButton = new JButton("Free");
        FreeButton.setBounds(638, 14, 97, 23);
        FreeButton.setActionCommand("Free");
        FreeButton.addActionListener(DrawListener);
        toolPanel.add(FreeButton);

        JButton ColorButton = new JButton("Color");
        ColorButton.setBounds(761, 14, 97, 23);
        ColorButton.setActionCommand("Color");
        ColorButton.setPreferredSize(new Dimension(27, 27));
        ColorButton.addActionListener(DrawListener);
        toolPanel.add(ColorButton);
        frame.getContentPane().add(toolPanel);

        canvas = new Painter();
        canvas.setBorder(null);
        canvas.setBounds(216, 66, 866, 531);
        width = canvas.getWidth();
        height = canvas.getHeight();
        canvas.setBackground(Color.WHITE);
        canvas.getList(DrawListener.getRecord());
        frame.getContentPane().add(canvas);
        canvas.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        canvas.addMouseListener(DrawListener);
        canvas.addMouseMotionListener(DrawListener);
        DrawListener.setGraphics(canvas.getGraphics());


        list = new JList<Object>();
        frame.getContentPane().add(list);
        String[] nameList = {Name};
        list.setListData(nameList);
        JScrollPane scrollList = new JScrollPane(list);
        scrollList.setBounds(28, 90, 153, 292);
        scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollList);

        JButton KickButton = new JButton("Kick");
        KickButton.addActionListener(e -> {
            String user = list.getSelectedValue().toString();
            if (Name.equals(user)) {
                return;
            }

            for (int i = 0; i < Server.connections.size(); i++) {
                Connection connection = Server.connections.get(i);
                if (user.equals(connection.name)) {
                    connection.kick = true;
                    try {
                        connection.dos.writeUTF("kick " + connection.name);
                        connection.dos.flush();
                    } catch (IOException e2) {
                        System.out.println("Host error" + "\n");
                        System.exit(1);
                    }
                    try {
                        connection.socket.close();
                    } catch (IOException ex) {
                        System.out.println("Host error" + "\n");
                        System.exit(1);
                    }
                    Server.connections.remove(i);
                    Server.usernames.remove(user);
                    JOptionPane.showMessageDialog(frame, user + " is kicked out!");
                }
            }

            for (String username : Server.usernames) {
                user += " " + username;
            }
            String[] k1 = user.split(" ", 2);
            String[] kkk = k1[1].split(" ");
            list.setListData(kkk);
            ConnectionHost.broadcast("delete " + user);
        });

        KickButton.setBounds(49, 407, 107, 44);
        frame.getContentPane().add(KickButton);

        chatArea = new JTextArea();
        chatArea.setBounds(1088, 90, 220, 350);
        chatArea.setEnabled(false);
        frame.getContentPane().add(chatArea);

        JTextField textField = new JTextField();
        textField.setBounds(1088, 463, 220, 50);
        frame.getContentPane().add(textField);

        JButton SendButton = new JButton("Send");
        SendButton.setBounds(1155, 523, 107, 39);
        frame.getContentPane().add(SendButton);
        SendButton.addActionListener(e -> {
            String msg = Name + ": " + textField.getText();
            chatArea.append(msg + "\n" + "\n");
            ConnectionHost.broadcast("chat " + msg);
            textField.setText("");
        });
    }
}
