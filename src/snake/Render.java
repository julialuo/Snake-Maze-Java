package snake;

import javax.swing.*;
import java.awt.*;

/**
 * Created by julia on 2016-07-02.
 */
public class Render extends JPanel {

    public static Font regFont = new Font("Calibri", Font.PLAIN, 32);
    public static Color green = new Color(1666073);

    protected void paintComponent(Graphics g) {
        SnakeMaze snakeGame = SnakeMaze.snakeGame;
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, snakeGame.frame.getWidth(), snakeGame.frame.getHeight());

        g.setColor(Color.GREEN);
        for (Point point : snakeGame.snakeParts) { //iteration
            g.fillRect(point.x * SnakeMaze.SCALE, point.y * SnakeMaze.SCALE, SnakeMaze.SCALE, SnakeMaze.SCALE);
        }
        g.fillRect(snakeGame.head.x * SnakeMaze.SCALE, snakeGame.head.y * SnakeMaze.SCALE, SnakeMaze.SCALE, SnakeMaze.SCALE);

        g.setColor(Color.RED);
        g.fillRect(snakeGame.cherry.x * SnakeMaze.SCALE, snakeGame.cherry.y * SnakeMaze.SCALE, SnakeMaze.SCALE, SnakeMaze.SCALE);

        g.setColor(Color.WHITE);
        g.setFont(regFont);
        String output = "Score: " + snakeGame.score + "  Time: " + snakeGame.ticks/50 + " s";
        g.drawString(output, snakeGame.frame.getWidth()/2 - 100, 32);
    }
}
