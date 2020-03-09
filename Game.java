import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;


public class Game {

    private Boolean[][] gameField = new Boolean[50][50];
    private Boolean[][] newGameField = new Boolean[50][50];
    private Boolean gameIsOn = false;
    private volatile boolean goNewField = false;

    public static void main(String[] args) {

        Game gui = new Game();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame("Game of life");
        JPanel panel = new JPanel();

        JButton bottom0 = new JButton("Play");
        JButton bottom1 = new JButton("Step");
        JButton bottom2 = new JButton("Filling");
        JButton bottom3 = new JButton("Clear Panel");

        bottom0.addActionListener(new playTheGame(){
            public void actionPerformed(ActionEvent e) {
                goNewField = !goNewField;
                bottom0.setText(goNewField? "Stop":"Play"); }});
        bottom1.addActionListener(new playTheGame());
        bottom2.addActionListener(fillingPanelRandom);
        bottom3.addActionListener(ClearPanel);
        bottom1.setSize(100, 150);


        Canvas.setSize(300, 300);
        Canvas.setBackground(Color.white);

        panel.add(bottom0);
        panel.add(bottom1);
        panel.add(bottom2);
        panel.add(bottom3);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 417);
        frame.getContentPane().add(BorderLayout.CENTER, Canvas);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.setResizable(false);
        frame.setVisible(true);

        while (true){
            if (goNewField){
                process();
                Canvas.repaint();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
        private int countNeighbors(int x, int y) {
            int count = 0;
            for (int dx = -1; dx < 2; dx++) {
                for (int dy = -1; dy < 2; dy++) {
                    int nX = x + dx;
                    int nY = y + dy;
                    nX = (nX < 0) ? gameField.length - 1 : nX;
                    nY = (nY < 0) ? gameField.length - 1 : nY;
                    nX = (nX > gameField.length - 1) ? 0 : nX;
                    nY = (nY > gameField.length - 1) ? 0 : nY;
                    count += (gameField[nX][nY]) ? 1 : 0;
                }
            }
            if (gameField[x][y]) {
                count--;
            }
            return count;
        }
        void process(){
            for (int x = 0; x < gameField.length; x++) {
                for (int y = 0; y < gameField.length; y++) {
                    int count = countNeighbors(x, y);
                    newGameField[x][y] = gameField[x][y];
                    // if are 3 live neighbors around empty cells - the cell becomes alive
                    newGameField[x][y] = (count == 3) ? true : newGameField[x][y];
                    // if cell has less than 2 or greater than 3 neighbors - it will be die
                    newGameField[x][y] = ((count < 2) || (count > 3)) ? false : newGameField[x][y];
                }
            }
            for (int x = 0; x < gameField.length; x++) {
                System.arraycopy(newGameField[x], 0, gameField[x], 0, gameField.length);
            }
            gameIsOn = false;
            Canvas.repaint();
        }


    class playTheGame implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        process();
        }
    }

    ActionListener ClearPanel = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int x = 0; x < gameField.length; x++) {
                for (int y = 0; y < gameField.length; y++) {
                    gameField[x][y] = false;
                }
            }
            gameIsOn = true;
            Canvas.repaint();
        }};

    ActionListener fillingPanelRandom = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            int random = 0;
            for (int x = 0; x < gameField.length; x++) {
                for (int y = 0; y < gameField.length; y++) {
                    random = (int) (Math.random() * 2);
                    if (random == 0) {
                        gameField[x][y] = true;
                    } else {
                        gameField[x][y] = false;
                    }
                }
            }
            gameIsOn = true;
            Canvas.repaint();
        }};

    JPanel Canvas = new JPanel(){
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < 50; x++) {
                for (int y = 0; y < 50; y++) {
                    if(gameIsOn){
                        if (gameField[x][y]) {
                            g.setColor(Color.BLACK);
                            g.fillOval(x * 7, y * 7, 7, 7);
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillOval(x * 7, y * 7, 7, 7);
                        }
                    } else {
                        if (newGameField[x][y]) {
                            g.setColor(Color.BLACK);
                            g.fillOval(x * 7, y * 7, 7, 7);
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillOval(x * 7, y * 7, 7, 7);
                        }
                    }
                }
            }
        }
    };
}