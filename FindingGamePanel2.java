import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.Random;;

public class FindingGamePanel2 extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 75;

    private JButton showkidButton;
    private JButton showwallButton;
    private JButton playAgainButton;
    // private JButton bfsButton;
    int playerX;
    int playerY;
    int nextplayerX;
    int nextplayerY;
    int lostKidX;
    int lostKidY;
    boolean lostKidVisible = false;
    boolean wallVisible = false;
    ArrayList<Point> obstacles;
    char direction = 'U';
    boolean running = false;
    Random random;
    Image playerImage;
    Image wallImage;
    Image backgroundImage;
    Image targetImage;


    Timer timer;
    int steps = -1;
    int distance;

    FindingGamePanel2() {
        random = new Random();
        obstacles = new ArrayList<>();
        showkidButton = new JButton("Show LIRILI");
        showwallButton = new JButton("Show Wall");
        playAgainButton = new JButton("Play Again?");
        // bfsButton = new JButton("CHEAT!!!");
        // add(bfsButton);
        add(showkidButton);
        add(showwallButton);
        add(playAgainButton);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        // bfsButton.addActionListener(this);
        // bfsButton.setFocusable(false);
        showkidButton.addActionListener(this);
        showkidButton.setFocusable(false);
        showwallButton.addActionListener(this);
        showwallButton.setFocusable(false);
        playAgainButton.addActionListener(this);
        playAgainButton.setFocusable(false);
        playerImage = new ImageIcon("Brr_Brr_Patapim-removebg-preview.png").getImage();
        targetImage = new ImageIcon("Lirili_rili_ralila-removebg-preview.png").getImage();
        wallImage = new ImageIcon("tree.png").getImage();
        backgroundImage = new ImageIcon("grassBackground.jpg").getImage();



        startGame();
      
    }

    public void startGame() {
        obstacles.clear();
        obstacles();
        newLostkid();
        player();
        steps = -1;
        running = true;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {

            g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            if (wallVisible) {
                g.setColor(Color.gray);
                for (Point p : obstacles) {
                    g.drawImage(wallImage, p.x, p.y, UNIT_SIZE, UNIT_SIZE, this);
                }
            }

            if (lostKidVisible) {
               
                g.drawImage(targetImage,lostKidX, lostKidY, UNIT_SIZE, UNIT_SIZE,this);
            }

            g.drawImage(playerImage, playerX, playerY, UNIT_SIZE, UNIT_SIZE, this);

            radar(g);
            steps(g);
        } else {
            g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            lostKidfound(g);
            steps(g);
        }
    }

    public void newLostkid() {
        do {
            lostKidX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            lostKidY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        } while (obstacles.stream().anyMatch(p -> p.x == lostKidX && p.y == lostKidY));

    }

    public void player() {
        do {
            playerX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            playerY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        } while (obstacles.stream().anyMatch(p -> p.x == playerX && p.y == playerY));
    }

    public void move() {
        nextplayerX = playerX;
        nextplayerY = playerY;

        switch (direction) {
            case 'U':
                nextplayerY = playerY - UNIT_SIZE;

                break;
            case 'D':
                nextplayerY = playerY + UNIT_SIZE;

                break;
            case 'L':
                nextplayerX = playerX - UNIT_SIZE;

                break;
            case 'R':
                nextplayerX = playerX + UNIT_SIZE;

                break;
        }
        for (Point p : obstacles) {
            if (nextplayerX == p.x && nextplayerY == p.y) {
                return;
            }
        }
        playerX = nextplayerX;
        playerY = nextplayerY;
    }

    public void checkLostkid() {
        if (playerX == lostKidX && playerY == lostKidY) {
            running = false;
        }
    }

    public void lostKidfound(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("INK FREE", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("U FOUND LIRILI", (SCREEN_WIDTH - metrics.stringWidth("U FOUND THE KID")) / 2, SCREEN_HEIGHT / 2);

    }

    private void toggleLostKidVisibility() {
        lostKidVisible = !lostKidVisible;
        repaint();
    }

    private void toggleWallVisibility() {
        wallVisible = !wallVisible;
        repaint();
    }
    
    private void PlayAgain(){
        startGame();
    }

    public void steps(Graphics g) {
        if (playerX != playerX - 1 || playerX != playerX + 1 || playerY != playerY + 1 || playerY != playerY + 1) {
            steps++;
        }
        g.setColor(Color.red);
        g.setFont(new Font("INK FREE", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("STEPS: " + steps, (SCREEN_WIDTH - metrics.stringWidth("STEPS: " + steps)) + 1 / 2,
                g.getFont().getSize());

    }

    public void obstacles() {
        int obstacleX;
        int obstacleY;
        int X = SCREEN_WIDTH / UNIT_SIZE;
        int Y = SCREEN_HEIGHT / UNIT_SIZE;
        boolean[][] grid = new boolean[X][Y];
        ArrayList<Point> walls = new ArrayList<>();
        for (int x = 0; x < X; x++) {
            for (int y = 0; y < Y; y++) {
                grid[x][y] = false;
            }
        }
        int startx = random.nextInt(X);
        int starty = random.nextInt(Y);
        startx -= startx % 2;
        starty -= starty % 2;
        grid[startx][starty] = true;
        ArrayList<Point> list = new ArrayList<>();
        int[][] directions = { { -2, 0 }, { 2, 0 }, { 0, 2 }, { 0, -2 } };
        for (int i = 0; i < 4; i++) {
            int nextx = startx + directions[i][0];
            int nexty = starty + directions[i][1];
            if (nextx < 0 || nextx >= X)
                continue;
            if (nexty < 0 || nexty >= Y)
                continue;
            grid[nextx][nexty] = true;
            Point nextPoint = new Point();
            nextPoint.x = nextx;
            nextPoint.y = nexty;
            nextPoint.prevX = startx;
            nextPoint.prevY = starty;
            list.add(nextPoint);
        }
        while (!list.isEmpty()) {
            int index = random.nextInt(list.size());
            Point currpoint = list.get(index);
            list.remove(index);
            // grid[currpoint.x][currpoint.y] = true;
            int midx = (currpoint.x + currpoint.prevX) / 2;
            int midy = (currpoint.y + currpoint.prevY) / 2;
            grid[midx][midy] = true;
            for (int i = 0; i < 4; i++) {
                int nextx = currpoint.x + directions[i][0];
                int nexty = currpoint.y + directions[i][1];
                if (nextx < 0 || nextx >= X)
                    continue;
                if (nexty < 0 || nexty >= Y)
                    continue;
                if (grid[nextx][nexty])
                    continue;
                grid[nextx][nexty] = true;
                Point nextPoint = new Point();
                nextPoint.x = nextx;
                nextPoint.y = nexty;
                nextPoint.prevX = currpoint.x;
                nextPoint.prevY = currpoint.y;
                list.add(nextPoint);
            }
        }
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                if (!grid[i][j]) {
                    obstacleX = i;
                    obstacleY = j;
                    obstacles.add(new Point(obstacleX * UNIT_SIZE, obstacleY * UNIT_SIZE));
                }
            }
        }

    }

    public void radar(Graphics g) {
        distance = Math.abs(lostKidX - playerX) / UNIT_SIZE + Math.abs(lostKidY - playerY) / UNIT_SIZE;
        g.setColor(Color.red);
        g.setFont(new Font("INK FREE", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Distance " + distance, (SCREEN_WIDTH - metrics.stringWidth("Distance " + distance)) / 2,
                SCREEN_HEIGHT - 2 * UNIT_SIZE);
        if (distance <= 5) {
            g.drawString("very close", (SCREEN_WIDTH - metrics.stringWidth("very close")) / 2,
                    SCREEN_HEIGHT - UNIT_SIZE);
        }
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    direction = 'D';
                    break;
            }
            if (running) {
                move();
                checkLostkid();
            }
            repaint();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == showkidButton) {
            toggleLostKidVisibility();
        }
        if(e.getSource() == showwallButton){
            toggleWallVisibility();
        }
        if(e.getSource() == playAgainButton){
            PlayAgain();
            repaint();
        }
    }

}