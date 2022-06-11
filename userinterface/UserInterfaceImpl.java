/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.userinterface;

import java.awt.event.*;
import java.util.Random;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.Timer;
import snake.constants.SnakeMovement;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.Timer;
import snake.constants.SnakeMovement;

/**
 *
 * @author William-UEM
 */
//public class UserInterfaceImpl extends JPanel implements ActionListener {
public class UserInterfaceImpl implements ActionListener,
        EventHandler<KeyEvent> {
    
    private static final int  SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 600;
    private static final int    UNIT_SIZE = 25;
    private static final int    MAX_UNITS = (SCENE_HEIGHT*SCENE_WIDTH)/(UNIT_SIZE*UNIT_SIZE);
    private static final int        DELAY = 50;
    
    private final Group  root;
    private final Stage stage;
//    o i-ésimo elemento destes arrays armazena
//    a posição do i-ésimo segmento da cobra
    int[] x = new int[MAX_UNITS];
    int[] y = new int[MAX_UNITS];
    SnakeMovement move;
    int bodyParts = 6;
    int foodEaten;
    int foodX;
    int foodY;
    
    boolean running;
    
    Timer timer;
    Random random;
    KeyListener listener;
    
    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        startGame();
        drawBackground(root);
        stage.show();
        //Não encontrei como eu poderia chamar o método 
        //handle da classe UserInterfaceImpl, acabou
        //que ficou esta gambiarra
        stage.addEventHandler(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent e) {
                        KeyCode code = e.getCode();
                        if(!movimentoValido(code)) return;
                        
                        switch(code) {
                        case UP:
                            move = SnakeMovement.UP;
                            break;
                            
                        case DOWN:
                            move = SnakeMovement.DOWN;
                            break;
                            
                        case LEFT:
                            move = SnakeMovement.LEFT;
                            break;
                            
                        case RIGHT:
                            move = SnakeMovement.RIGHT;
                            break;
                        }
                    };
        });
    }
    
    private boolean movimentoValido(KeyCode keyCode) {
        if (keyCode == KeyCode.LEFT) {
            return move != SnakeMovement.RIGHT;
            
        } else if (keyCode == KeyCode.RIGHT) {
            return move != SnakeMovement.LEFT;
            
        } else if (keyCode == KeyCode.UP) {
            return move != SnakeMovement.DOWN;
                    
        } else if (keyCode == KeyCode.DOWN) {
            return move != SnakeMovement.UP;
        }
        return false;
    }
    
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.BLACK);

        stage.setTitle("Snake Game");
        stage.setScene(scene);
    }

    private void startGame() {
        random = new Random();
        newFood();
        startSnake();
        move = SnakeMovement.RIGHT;
        running = true;
        
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void startSnake() {
        for (int i = 0; i < bodyParts; i++) {
            int xCabeca = bodyParts;
            int yCabeca = (SCENE_HEIGHT/UNIT_SIZE)/2;
            
            x[i] = xCabeca - i;
            y[i] = yCabeca;
        }
    }
    
    private void newFood() {
        foodX = random.nextInt(SCENE_WIDTH/UNIT_SIZE);
        foodY = random.nextInt(SCENE_HEIGHT/UNIT_SIZE);
    }
    
    private void moveSnake() {
        //Cada bodypart recebe as coordenadas do próximo bodypart
        //não altera as coordenadas da cabeça.
        for (int i = bodyParts-1; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //Movimento da cabeça
        switch(move) {
            case RIGHT:
                x[0]++;
                break;
                
            case LEFT:
                x[0]--;
                break;
                
            case UP:
                y[0]--;
                break;
                
            case DOWN:
                y[0]++;
                break;
        }
    }
    
    private void checkFood() {
        // Se a cabeça está na mesma posição que a comida
        if ((x[0] == foodX) && (y[0] == foodY)) {
            newFood();
            //posição da nova parte da cobra é igual à última
            x[bodyParts] = x[bodyParts-1];
            y[bodyParts] = y[bodyParts-1];
            bodyParts++;
        }
    }
    
    private void checkCollisions() {
        int lastHorizontalUnit = SCENE_WIDTH/UNIT_SIZE;
        int   lastVerticalUnit = SCENE_HEIGHT/UNIT_SIZE;
        //Colidiu com as bordas de tela
        if (
                (x[0] >= lastHorizontalUnit) 
                || (y[0] >= lastVerticalUnit)
                || (x[0] < 0)
                || (y[0] < 0)
        ){
            running = false;
            System.out.println("Screen border collision");
        }
        //Colidiu com o próprio corpo
        for(int i = 1; i < bodyParts; i++) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                System.out.println("Body collision");
                System.out.println("i: " + i);
                System.out.println("x:" + x[i]);
                System.out.println("y:" + y[i]);
            }
        }
        if(!running) timer.stop();
    }
    
    private void updateView() {
        root.getChildren().clear();
        drawFood();
        drawSnake();
//        drawGridLines();
    }

    private void drawSnake() {
        for (int i = 0; i < bodyParts; i++) {
            Rectangle bodyPart = new Rectangle(
                    x[i] * UNIT_SIZE,
                    y[i] * UNIT_SIZE,
                    UNIT_SIZE,
                    UNIT_SIZE
            );
            // Pinta a cabeça de GREEN e o resto do corpo de DARKGREEN
            if (i == 0)
                bodyPart.setFill(Color.GREEN);
            else
                bodyPart.setFill(Color.DARKGREEN);
            
            root.getChildren().add(bodyPart);
        }        
    }
    
    private void drawFood() {
        Circle food = new Circle(
                (foodX+0.5)*UNIT_SIZE,
                (foodY+0.5)*UNIT_SIZE,
                UNIT_SIZE/2,
                Color.RED
        );
        
        root.getChildren().add(food);
    }
    
    private void drawGridLines() {
        for(int i = 1;i < (SCENE_WIDTH / UNIT_SIZE); i++) {
            Rectangle verticalLine = new Rectangle(
                    i * UNIT_SIZE,
                    0,
                    1,
                    SCENE_HEIGHT
            );
            verticalLine.setFill(Color.WHITE);
            
            root.getChildren().add(verticalLine);
        }
        for(int i = 1;i < (SCENE_HEIGHT / UNIT_SIZE); i++) {
            Rectangle horizontalLine = new Rectangle(
                    0,
                    i * UNIT_SIZE,
                    SCENE_WIDTH,
                    1
            );
            horizontalLine.setFill(Color.WHITE);
            
            root.getChildren().add(horizontalLine);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(running) {
            moveSnake();
            checkFood();
            checkCollisions();
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateView();
            }
        });
    }
    
    @Override
    public void handle(KeyEvent event) {
        System.out.println("Botao apertado");
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            
        }
    }
}