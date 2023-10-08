package guest_config;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Painter extends JPanel {
    private ArrayList<String> recordList = new ArrayList<>();

    public void getList(ArrayList<String> recordList) {
        this.recordList = recordList;
    }

    public void paint(Graphics gr) {
        super.paint(gr);
        draw((Graphics2D) gr, this.recordList);
    }

    public void draw(Graphics2D g, ArrayList<String> recordList) {
        try {
            String[] recordArray = recordList.toArray(new String[recordList.size()]);
            for (String line : recordArray) {
                String[] record = line.split(" ",10);
                int startX, startY, endX, endY, t, red, green, blue;
                Color color;
                if (record[1].equals("!")) {
                    continue;
                }
                if ("Circle".equals(record[0])) {
                    t = Integer.parseInt(record[1]);
                    g.setStroke(new BasicStroke());
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g.setColor(color);
                    startX = Integer.parseInt(record[5]);
                    startY = Integer.parseInt(record[6]);
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    int diameter = Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
                    g.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
                } else if ("Rectangle".equals(record[0])) {
                    t = Integer.parseInt(record[1]);
                    g.setStroke(new BasicStroke());
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g.setColor(color);
                    startX = Integer.parseInt(record[5]);
                    startY = Integer.parseInt(record[6]);
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    g.drawLine(startX, startY, endX, startY);
                    g.drawLine(startX, endY, endX, endY);
                    g.drawLine(startX, startY, startX, endY);
                    g.drawLine(endX, startY, endX, endY);
                } else if ("Text".equals(record[0])) {
                    t = Integer.parseInt(record[1]);
                    Font f = new Font(null, Font.PLAIN, t + 10);
                    g.setFont(f);
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g.setColor(color);
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    String text = record[9];
                    g.drawString(text, endX, endY);
                } else if ("Line".equals(record[0])) {
                    t = Integer.parseInt(record[1]);
                    g.setStroke(new BasicStroke());
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g.setColor(color);
                    startX = Integer.parseInt(record[5]);
                    startY = Integer.parseInt(record[6]);
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    g.drawLine(startX, startY, endX, endY);
                } else if ("Oval".equals(record[0])) {
                    t = Integer.parseInt(record[1]);
                    g.setStroke(new BasicStroke());
                    red = Integer.parseInt(record[2]);
                    green = Integer.parseInt(record[3]);
                    blue = Integer.parseInt(record[4]);
                    color = new Color(red, green, blue);
                    g.setColor(color);
                    startX = Integer.parseInt(record[5]);
                    startY = Integer.parseInt(record[6]);
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    int width = Math.abs(endX - startX);
                    int height = Math.abs(endY - startY);
                    g.drawOval(startX - width, startY - height, 2 * width, 2 * height);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to paint" + "\n");
            System.exit(1);
        }
    }
}
