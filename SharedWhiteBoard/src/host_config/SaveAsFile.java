package host_config;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class SaveAsFile {
    JFrame frame;
    private JTextField textField;
    private Host whiteBoard;
    private String[] format_list = {".txt",".jpg",".png"};

    public SaveAsFile(Host whiteBoard) {
        this.whiteBoard = whiteBoard;
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 341, 199);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Input file name");
        lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
        lblNewLabel.setBounds(30, 65, 107, 20);
        frame.getContentPane().add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(127, 63, 111, 21);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[] {".jpg",".png"}));
        comboBox.setBounds(248, 62, 58, 23);
        frame.getContentPane().add(comboBox);

        JButton btn_save = new JButton("Save");
        btn_save.setFont(new Font("Calibri", Font.PLAIN, 13));
        btn_save.addActionListener(e -> {
            String name = textField.getText();
            String format = comboBox.getSelectedItem().toString();
            if (check_duplicate_file(name)) {
                saveFile(name);
                saveImg(name, format);
                frame.dispose();
            }
        });
        btn_save.setBounds(109, 112, 79, 23);
        frame.getContentPane().add(btn_save);

        JLabel lblNewLabel_1 = new JLabel("Save As");
        lblNewLabel_1.setFont(new Font("Calibri", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(130, 25, 58, 15);
        frame.getContentPane().add(lblNewLabel_1);
    }

    public void saveImg(String file, String format) {
        BufferedImage Img = new BufferedImage(whiteBoard.width, whiteBoard.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = Img.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,whiteBoard.width,whiteBoard.height);
        ArrayList<String> recordList = whiteBoard.DrawListener.getRecord();
        whiteBoard.canvas.draw(g, recordList);

        try {
            String homeDirectory = System.getProperty("user.home");
            String filePath = homeDirectory + "/Desktop/" + file + format;
            if (format.equals(".jpg")) {
                ImageIO.write(Img, "JPEG", new File(filePath));
            } else if (format.equals(".png")) {
                ImageIO.write(Img, "PNG", new File(filePath));
            }
            System.out.println(filePath + "is saved to desktop");
        } catch (IOException e1) {
            System.out.println("Failed to write and save Image");
        }
    }

    public void saveFile(String file) {
        try {
            String homeDirectory = System.getProperty("user.home");
            String desktopPath = homeDirectory + "/Desktop/";
            String filePath = desktopPath + file + ".txt";
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(filePath));
            ArrayList<String> recordList = whiteBoard.DrawListener.getRecord();
            for (String record : recordList) {
                outputStream.println(record);
            }
            outputStream.flush();
            outputStream.close();
            System.out.println(filePath + " is saved to desktop");
        } catch (FileNotFoundException e) {
            System.out.println("Failed to write and save file");
        }
    }

    public boolean check_duplicate_file(String file) {
        String homeDirectory = System.getProperty("user.home");
        String desktopPath = homeDirectory + "/Desktop/";
        for (String format : format_list) {
            String filePath = desktopPath + file + format;
            File outputFile = new File(filePath);
            if (outputFile.exists()) {
                System.out.println("File name is already in use.");
                JOptionPane.showMessageDialog(frame, "File name is already in use.");
                return false;
            }
        }
        return true;
    }

}
