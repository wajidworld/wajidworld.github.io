package de.lucavon.snake;

import javax.swing.*;
import java.awt.*;

public class GraphicsPanel extends JPanel {

    public static int fieldPixelSize = 16;
    private boolean alive = true;

    public GraphicsPanel() {
        setPreferredSize(new Dimension(SnakeGame.getInstance().getFieldWidth() * fieldPixelSize, SnakeGame.getInstance().getFieldHeight() * fieldPixelSize));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!alive)
            return;
        byte[][] field = SnakeGame.getInstance().getField();
        for(int i = 0; i < SnakeGame.getInstance().getFieldWidth(); i++)
            for(int j = 0; j < SnakeGame.getInstance().getFieldHeight(); j++) {
                byte b = field[i][j];
                switch(b) {
                    case SnakeGame.FIELD_SNAKE:
                        g.setColor(Color.WHITE);
                        break;
                    case SnakeGame.FIELD_APPLE:
                        g.setColor(Color.GREEN);
                        break;
                }
                if(b != SnakeGame.FIELD_NONE)
                    g.fillRect(i * fieldPixelSize, j * fieldPixelSize, fieldPixelSize, fieldPixelSize);
            }
    }

    public void dead() {
        setBackground(Color.RED);
        alive = false;
    }
}
