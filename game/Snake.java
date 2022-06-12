/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.game;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import snake.constants.SnakeMovement;

/**
 *
 * @author William-UEM
 */
public class Snake {
    private static int[] x;
    private static int[] y;
    private static int bodyParts;
    private static SnakeMovement move;

    public Snake(int bodyParts, int maxBodyParts, SnakeMovement move) {
        x = new int[maxBodyParts];
        y = new int[maxBodyParts];
        this.bodyParts = bodyParts;
        this.move = move;
    }
    
    public void startSnake(int bodyParts, int x, int y) {
        move = SnakeMovement.RIGHT;
        bodyParts = bodyParts;
        
        for (int i = 0; i < bodyParts; i++) {
            int xCabeca = x;
            int yCabeca = y;
            
            this.x[i] = xCabeca - i;
            this.y[i] = yCabeca;
        }
    }
    
    public void updateMove(KeyEvent e) {
        KeyCode code = e.getCode();
        if (!movimentoValido(code)) return;
            
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
    }
    
    private boolean movimentoValido(KeyCode keyCode) {
        if (null != keyCode) switch (keyCode) {
            case LEFT:
                return move != SnakeMovement.RIGHT;
            case RIGHT:
                return move != SnakeMovement.LEFT;
            case UP:
                return move != SnakeMovement.DOWN;
            case DOWN:
                return move != SnakeMovement.UP;
        }
        return false;
    }
    
    public void move() {
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

    public static int[] getX() {
        return x;
    }

    public static void setX(int[] x) {
        Snake.x = x;
    }

    public static int[] getY() {
        return y;
    }

    public static void setY(int[] y) {
        Snake.y = y;
    }

    public static int getBodyParts() {
        return bodyParts;
    }

    public static void setBodyParts(int bodyParts) {
        Snake.bodyParts = bodyParts;
    }
    
    
}
