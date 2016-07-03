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
    public ArrayList<Point> mazeParts = new ArrayList<Point> ();
    public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SCALE = 40;
    public int direction = DOWN, score, tailLength;
    public int[] cherryCoord = new int[2];
    public int[] mazeCoord = new int[2];
    public double speed, ticks;
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
        speed = 5;
        tailLength = 0;
        direction = DOWN;
        head = new Point(0,-1);
        random = new Random();
        snakeParts.clear();
        cherryCoord = generateCherry();
        cherry = new Point(cherryCoord[0], cherryCoord[1]);
        snakeParts.add(new Point(head.x, head.y));
        mazeParts.clear();

        for (int i = 0; i < 10; i++) {
            mazeCoord = generateMazePart();
            mazeParts.add(new Point(mazeCoord[0], mazeCoord[1]));        }

        timer.start();
    }

    public int[] generateMazePart () {
        int[] coord = new int[2];
        do {
            coord[0] = random.nextInt(frame.getWidth() / SCALE - 1);
            coord[1] = random.nextInt(frame.getHeight() / SCALE - 3);
        }
        while (coord[1] < 3);

        return coord;
    }

    public void actionPerformed(ActionEvent arg0) {
        render.repaint();

        if (!over && !paused)
            ticks+= 0.5;

        if (ticks % speed == 0 && head != null && !over && !paused) {
            snakeParts.add(new Point(head.x, head.y));

            if (direction == UP && head.y - 1 >= 0 && noTailAt(head.x, head.y - 1) && noMazeAt(head.x, head.y - 1))
                head = new Point(head.x, head.y - 1);
            else if (direction == DOWN && head.y + 1 < frame.getHeight() / SCALE && noTailAt(head.x, head.y + 1) &&
                    noMazeAt(head.x, head.y + 1))
                head = new Point(head.x, head.y + 1);
            else if (direction == LEFT && head.x - 1 >= 0 && noTailAt(head.x - 1, head.y) && noMazeAt(head.x - 1, head.y))
                head = new Point(head.x - 1, head.y);
            else if (direction == RIGHT && head.x + 1 < frame.getWidth() / SCALE && noTailAt(head.x + 1, head.y) &&
                    noMazeAt(head.x + 1, head.y))
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

            if (tailLength != 0 && tailLength%5 == 0) {
                if (5 - tailLength/10.0 > 1.5)
                    speed = 5 - tailLength/10.0;
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

    public boolean noMazeAt(int x, int y) {
        for (Point point: mazeParts) {
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
            for (Point point : mazeParts) {
                if (point.getX() == coord[0] && point.getY() == coord[1])
                    conflict = true;
            }
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
