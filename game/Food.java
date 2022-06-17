/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake.game;

import java.util.Random;

/**
 *
 * @author William-UEM
 */
public class Food {
    private static int x;
    private static int y;
    Random random;

    public Food(int horizontalUnits, int verticalUnits) {
        random = new Random();
        Food.x = random.nextInt(horizontalUnits);
        Food.y = random.nextInt(verticalUnits);
    }
    
    public void newFood(int horizontalUnits, int verticalUnits) {
        Food.x = random.nextInt(horizontalUnits);
        Food.y = random.nextInt(verticalUnits);
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }
}
