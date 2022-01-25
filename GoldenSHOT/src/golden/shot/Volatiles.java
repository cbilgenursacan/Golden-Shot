/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.util.Random;

/**
 *
 * @author CBN
 */
public abstract class Volatiles {
    int x;
    int y;
    int speed;
    Random random;
    boolean onScope;
    
    public Volatiles(){
        onScope = true;
        random = new Random();
    } 
    public abstract void move(); 
    
}
