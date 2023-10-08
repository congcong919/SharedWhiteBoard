package host_config;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class OpenFile {
    JFrame frame;
    private JTextField textField;
    private Host whiteBoard;

    public OpenFile(Host whiteBoard) {
        this.whiteBoard = whiteBoard;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 341, 199);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Open");
        lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        lblNewLabel.setBounds(39, 66, 58, 15);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(96, 63, 111, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Input file name ");
        lblNewLabel_1.setFont(new Font("Calibri", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(104, 23, 103, 31);
        frame.getContentPane().add(lblNewLabel_1);

        JButton btn_Open = new JButton("Open");
        btn_Open.setFont(new Font("Calibri", Font.PLAIN, 13));
        btn_Open.setBounds(109, 112, 79, 23);
        btn_Open.addActionListener(e -> {
            String name = textField.getText();
            String file = name;
            openFile(file);
            frame.dispose();
        });
        frame.getContentPane().add(btn_Open);
    }

    public void openFile(String file) {
        String homeDirectory = System.getProperty("user.home");
        String desktopPath = homeDirectory + "/Desktop/";
        String filePath = desktopPath + file + ".txt";
        Scanner inputStream;
        try {
            inputStream = new Scanner(new FileInputStream(filePath));
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(frame, "file does not exists");
            System.out.println("Problem opening files");
            return;
        }

        whiteBoard.DrawListener.clearRecord();
        while (inputStream.hasNextLine()) {
            String line = inputStream.nextLine();
            whiteBoard.DrawListener.update(line);
        }
        ConnectionHost.broadcast("new");
        ArrayList<String> rl = whiteBoard.DrawListener.getRecord();
        ConnectionHost.broadcastBatch(rl);
        whiteBoard.canvas.repaint();
        inputStream.close();
    }
}
