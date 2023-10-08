package guest_config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Guest {
    static Listener listener;
    public JFrame frame;
    public String guestUserName;
    public Connection connection;
    public static String[] userList;
    int width;
    int height;
    static Painter canvas;
    public static JList list = new JList();
    public static JTextArea guestChatArea;


    public Guest(Connection connection, String guestUserName) {
        this.connection = connection;
        this.guestUserName = guestUserName;
        initialize(guestUserName);
    }

    private void initialize(String guestUserName) {
        frame = new JFrame();
        frame.setTitle("Distributed Whiteboard (UserName): " + guestUserName);
        frame.setBounds(100, 100, 1355, 644);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listener = new Listener(frame);
        frame.getContentPane().setLayout(null);

        JPanel toolPanel = new JPanel();
        toolPanel.setBounds(206, 10, 988, 50);

        JButton LineButton = new JButton("Line");
        LineButton.setBounds(10, 14, 97, 23);
        LineButton.setActionCommand("Line");
        LineButton.addActionListener(listener);
        toolPanel.add(LineButton);

        JButton CircleButton = new JButton("Circle");
        CircleButton.setBounds(136, 14, 97, 23);
        CircleButton.setActionCommand("Circle");
        CircleButton.addActionListener(listener);
        toolPanel.add(CircleButton);

        JButton RectangleButton = new JButton("Rectangle");
        RectangleButton.setBounds(263, 14, 97, 23);
        RectangleButton.setActionCommand("Rectangle");
        RectangleButton.addActionListener(listener);
        toolPanel.add(RectangleButton);

        JButton OvalButton = new JButton("Oval");
        OvalButton.setBounds(389, 14, 97, 23);
        OvalButton.setActionCommand("Oval");
        OvalButton.addActionListener(listener);
        toolPanel.add(OvalButton);

        JButton TextButton = new JButton("A");
        TextButton.setActionCommand("Text");
        TextButton.addActionListener(listener);
        TextButton.setBounds(514, 14, 97, 23);
        toolPanel.add(TextButton);

        JButton FreeButton = new JButton("Free");
        FreeButton.setBounds(638, 14, 97, 23);
        FreeButton.setActionCommand("Free");
        FreeButton.addActionListener(listener);
        toolPanel.add(FreeButton);

        JButton ColorButton = new JButton("Color");
        ColorButton.setBounds(761, 14, 97, 23);
        ColorButton.setActionCommand("Color");
        ColorButton.setPreferredSize(new Dimension(27, 27));
        ColorButton.addActionListener(listener);
        toolPanel.add(ColorButton);
        toolPanel.setLayout(null);
        frame.getContentPane().add(toolPanel);

        canvas = new Painter();
        canvas.setBorder(null);
        canvas.setBounds(216, 66, 866, 531);
        width = canvas.getWidth();
        height = canvas.getHeight();
        canvas.setBackground(Color.WHITE);
        canvas.getList(listener.getRecord());
        frame.getContentPane().add(canvas);
        canvas.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        canvas.addMouseListener(listener);
        canvas.addMouseMotionListener(listener);
        listener.setGraphics(canvas.getGraphics());

        frame.getContentPane().add(list);
        list.setListData(userList);
        JScrollPane scrollList = new JScrollPane(list);
        scrollList.setBounds(28, 90, 153, 292);
        scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollList);

        guestChatArea = new JTextArea();
        guestChatArea.setBounds(1088, 90, 220, 350);
        guestChatArea.setEnabled(false);
        frame.getContentPane().add(guestChatArea);

        JTextField textField = new JTextField();
        textField.setBounds(1088, 463, 220, 50);
        frame.getContentPane().add(textField);

        JButton SendButton = new JButton("Send");
        SendButton.setBounds(1155, 523, 107, 39);
        frame.getContentPane().add(SendButton);
        SendButton.addActionListener(e -> {
            String msg = guestUserName + ": " + textField.getText();
            try {
                connection.dos.writeUTF("chat " + msg);
                connection.dos.flush();
            } catch (IOException e1) {
                System.out.println("Guest error" + "\n");
                System.exit(1);
            }
            textField.setText("");
        });
    }
}
