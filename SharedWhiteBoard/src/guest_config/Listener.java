package guest_config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class Listener implements ActionListener, MouseListener, MouseMotionListener {
    Graphics2D graphics;
    JFrame frame;
    int startX, startY, endX, endY;
    int thickness = 1;
    String type = "Line";
    static Color color = Color.BLACK;
    String rgb = "0 0 0";
    String record;
    ArrayList<String> recordList = new ArrayList<>();

    public Listener() {
    }

    public Listener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Color")) {
            final JFrame jf = new JFrame("Color Panel");
            jf.setSize(300,300);
            jf.setLocationRelativeTo(null);
            jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Color curColor = JColorChooser.showDialog(jf, "Choose color", null);
            if (curColor != null){
                color = curColor;
            }
        } else {
            this.type = e.getActionCommand();
            if (type.equals("Free")) {
                Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);
                frame.setCursor(cur);
            } else if (type.equals("Oval")) {
                Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
                frame.setCursor(cur);
            } else if (type.equals("Text")) {
                Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);
                frame.setCursor(cur);
            } else if (type.equals("Line")) {
                Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
                frame.setCursor(cur);
            }else if (type.equals("Rectangle")) {
                Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
                frame.setCursor(cur);
            }else if (type.equals("Circle")) {
                Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
                frame.setCursor(cur);
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        if (!graphics.getColor().equals(color)) {
            graphics.setColor(color);
        }
        if (type.equals("Free")) {
            rgb = getColor(color);
            graphics.setStroke(new BasicStroke(thickness));
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        if (this.type.equals("Line")) {
            rgb = getColor(color);
            graphics.setStroke(new BasicStroke(thickness));
            graphics.drawLine(startX, startY, endX, endY);
            record = "Line " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY;
            recordList.add(record);
        } else if (this.type.equals("Free")) {
            graphics.drawLine(startX, startY, endX, endY);
            record = "Line " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY;
            recordList.add(record);
        } else if (this.type.equals("Circle")) {
            rgb = getColor(color);
            graphics.setStroke(new BasicStroke(thickness));
            int diameter = Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
            graphics.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
            record = "Circle " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY;
            recordList.add(record);
        } else if (this.type.equals("Oval")) {
            rgb = getColor(color);
            graphics.setStroke(new BasicStroke(thickness));
            int width = Math.abs(endX - startX);
            int height = Math.abs(endY - startY);
            graphics.drawOval(startX - width, startY - height, 2 * width, 2 * height);
            record = "Oval " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY;
            recordList.add(record);
        } else if (this.type.equals("Rectangle")) {
            rgb = getColor(color);
            graphics.setStroke(new BasicStroke(thickness));
            graphics.drawLine(startX, startY, endX, startY);        // Top side
            graphics.drawLine(startX, endY, endX, endY);            // Bottom side
            graphics.drawLine(startX, startY, startX, endY);        // Left side
            graphics.drawLine(endX, startY, endX, endY);
            record = "Rectangle " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY;
            recordList.add(record);
        } else if (this.type.equals("Text")) {
            String text = JOptionPane.showInputDialog("Please enter input text");
            if (text != null) {
                Font f = new Font(null, Font.PLAIN, this.thickness + 10);
                graphics.setFont(f);
                rgb = getColor(color);
                graphics.drawString(text, endX, endY);
                record = "Text " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY + " " + text;
                recordList.add(record);
                startX = endX;
                startY = endY;
            } else {
                return;
            }
        }
        sendDraw();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        if (type.equals("Free")) {
            rgb = getColor(color);
            graphics.setStroke(new BasicStroke(thickness));
            graphics.drawLine(startX, startY, endX, endY);
            record = "Line " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY;
            recordList.add(record);
            startX = endX;
            startY = endY;
        } else {
            return;
        }
        sendDraw();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setGraphics(Graphics g){
        this.graphics = (Graphics2D) g;
    }

    public ArrayList<String> getRecord() {
        return recordList;
    }

    public void update(String line) {
        recordList.add(line);
    }

    public void clearRecord() {
        recordList.clear();
    }

    public String getColor (Color color) {
        return color.getRed() + " " + color.getGreen() + " " + color.getBlue();
    }

    private void sendDraw() {
        try {
            String draw = "draw " + record;
            Join.connection.dos.writeUTF(draw);
            Join.connection.dos.flush();
        } catch (IOException e) {
            System.out.println("Draw error" + "\n");
        }
    }
}
