package de.lucavon.snake;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class SnakeGame extends JFrame {

    private static SnakeGame instance;
    public static final byte FIELD_NONE = 0;
    public static final byte FIELD_SNAKE = 1;
    public static final byte FIELD_APPLE = 2;

    private GraphicsPanel panel;
    private int fieldWidth = 25, fieldHeight = 25;
    private byte[][] field = new byte[fieldWidth][fieldHeight];
    private ArrayList<Location> tail = new ArrayList<>();
    private Location currentSnakeLocation, appleLocation;
    private int snakeLength = 3, snakeSpeed = 7;
    private byte snakeDirection = 2;
    private java.util.Timer gameTimer;

    public SnakeGame() {
        instance = this;
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.panel = new GraphicsPanel();
        add(panel);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case 37:
                        setDirection((byte)0);
                        break;
                    case 38:
                        setDirection((byte)1);
                        break;
                    case 39:
                        setDirection((byte)2);
                        break;
                    case 40:
                        setDirection((byte)3);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        pack();
        setVisible(true);
        currentSnakeLocation = new Location(fieldWidth / 2, fieldHeight / 2);
        placeApple();

        gameTimer = new java.util.Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameTick();
            }
        }, 0, 1000/snakeSpeed);
    }

    private void setDirection(byte direction) {
        Location move = getMoveLocation(direction);
        if(field[move.getX()][move.getZ()] == FIELD_SNAKE)
            return;
        snakeDirection = direction;
    }

    public void gameTick() {
        while(tail.size() > snakeLength) {
            Location loc = tail.get(0);
            field[loc.getX()][loc.getZ()] = FIELD_NONE;
            tail.remove(0);
        }
        //Move now: Otherwise you can't go from left to right if you're as long as the field's width
        move();
        panel.repaint();
    }

    private Location getMoveLocation(byte direction) {
        Location currentSnakeLocation = new Location(this.currentSnakeLocation.getX(), this.currentSnakeLocation.getZ());
        switch(direction) {
            case 0:
                int x = currentSnakeLocation.getX() - 1;
                if(x < 0)
                    x = fieldWidth - 1;
                currentSnakeLocation.setX(x);
                break;
            case 1:
                int z = currentSnakeLocation.getZ() - 1;
                if(z < 0)
                    z = fieldHeight - 1;
                currentSnakeLocation.setZ(z);
                break;
            case 2:
                int x1 = currentSnakeLocation.getX() + 1;
                if(x1 == fieldWidth)
                    x1 = 0;
                currentSnakeLocation.setX(x1);
                break;
            case 3:
                int z1 = currentSnakeLocation.getZ() + 1;
                if(z1 == fieldHeight)
                    z1 = 0;
                currentSnakeLocation.setZ(z1);
                break;
            default:
                System.out.println("Invalid snake direction?!");
                break;
        }
        return currentSnakeLocation;
    }

    private void move() {
        currentSnakeLocation = getMoveLocation(snakeDirection);
        field[currentSnakeLocation.getX()][currentSnakeLocation.getZ()] = FIELD_SNAKE;
        if(isInsideSnake(currentSnakeLocation.getX(), currentSnakeLocation.getZ())) {
            dead();
            return;
        }
        if(currentSnakeLocation.getX() == appleLocation.getX() && currentSnakeLocation.getZ() == appleLocation.getZ()) {
            snakeLength += 1;
            placeApple();
        }
        tail.add(new Location(currentSnakeLocation.getX(), currentSnakeLocation.getZ()));
    }

    private boolean isInsideSnake(int x, int z) {
        for(Location loc : tail)
            if(loc.getX() == x && loc.getZ() == z)
                return true;
        return false;
    }

    private void placeApple() {
        if(tail.size() == fieldWidth * fieldHeight) {
            setTitle("Game completed!");
            dead();
            return;
        }

        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Location loc = new Location(tlr.nextInt(0, fieldWidth), tlr.nextInt(0, fieldHeight));
        while(isInsideSnake(loc.getX(), loc.getZ())) {
            loc = new Location(tlr.nextInt(0, fieldWidth), tlr.nextInt(0, fieldHeight));
        }
        appleLocation = loc;
        field[loc.getX()][loc.getZ()] = FIELD_APPLE;
    }

    private void dead() {
        panel.dead();
        gameTimer.cancel();
    }

    public static SnakeGame getInstance() {
        return instance;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public byte[][] getField() {
        return field;
    }
}
