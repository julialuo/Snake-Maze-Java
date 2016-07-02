package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by julia on 2016-07-02.
 */

public class SnakeMaze implements ActionListener, KeyListener {

    public static SnakeMaze snakeGame;
    public JFrame frame;
    public Render render;
    public Timer timer = new Timer(10, this);
    public ArrayList<Point> snakeParts = new ArrayList<Point>();
    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SCALE = 40;
    public int direction = DOWN, score, tailLength;
    public int[] cherryCoord = new int[2];
    public double ticks;
    public boolean over, paused, mazeConflict;
    public Point head, cherry;
    public Random random;
    public Dimension dim;

    public SnakeMaze() {
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 1000);
        frame.setResizable(false);
        frame.setLocation(dim.width/2 - frame.getWidth()/2, dim.height/2 - frame.getHeight()/2);
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.add(render = new Render());
        startGame();
    }

    public void startGame() {
        ticks = 0;
        over = false;
        paused = false;
        score = 0;
        tailLength = 0;
        direction = DOWN;
        mazeConflict = false;
        head = new Point(0,-1);
        random = new Random();
        snakeParts.clear();
        cherryCoord = generateCherry();
        cherry = new Point(cherryCoord[0], cherryCoord[1]);
        snakeParts.add(new Point(head.x, head.y));
        timer.start();
    }

    public void actionPerformed(ActionEvent arg0) {
        render.repaint();

        if (!over && !paused)
            ticks+= 0.5;

        if (ticks % 5 == 0 && head != null && !over && !paused) {
            snakeParts.add(new Point(head.x, head.y));

            if (direction == UP && head.y - 1 >= 0 && noTailAt(head.x, head.y - 1))
                head = new Point(head.x, head.y - 1);
            else if (direction == DOWN && head.y + 1 < frame.getHeight() / SCALE && noTailAt(head.x, head.y + 1))
                head = new Point(head.x, head.y + 1);
            else if (direction == LEFT && head.x - 1 >= 0 && noTailAt(head.x - 1, head.y))
                head = new Point(head.x - 1, head.y);
            else if (direction == RIGHT && head.x + 1 < frame.getWidth() / SCALE && noTailAt(head.x + 1, head.y))
                head = new Point(head.x + 1, head.y);
            else {
                over = true;
            }

            if (snakeParts.size() > tailLength)
                snakeParts.remove(0);

            if (cherry != null) {
                if (head.equals(cherry)) {
                    score += 10;
                    tailLength++;
                    cherryCoord = generateCherry();
                    cherry.setLocation(cherryCoord[0], cherryCoord[1]);
                }
            }
        }
    }

    public boolean noTailAt(int x, int y) {
        for (Point point: snakeParts) {
            if (point.getX() == x && point.getY() == y)
                return false;
        }
        return true;
    }

    public int[] generateCherry () {
        int[] coord = new int[2];
        boolean conflict;
        do {
            conflict = false;
            coord[0] = random.nextInt(frame.getWidth() / SCALE - 1);
            coord[1] = random.nextInt(frame.getHeight() / SCALE - 3);
        }
        while (conflict);

        return coord;
    }

    public static void main(String[] args) {
        snakeGame = new SnakeMaze();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int i = e.getKeyCode();

        if (!paused) {
            if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && direction != DOWN)
                direction = UP;
            if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && direction != UP)
                direction = DOWN;
            if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && direction != RIGHT)
                direction = LEFT;
            if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && direction != LEFT)
                direction = RIGHT;
            if (i == KeyEvent.VK_ESCAPE)
                System.exit(0);
        }

        if (i == KeyEvent.VK_SPACE) {
            if (over)
                startGame();
            else
                paused = !paused; //toggles paused boolean
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
