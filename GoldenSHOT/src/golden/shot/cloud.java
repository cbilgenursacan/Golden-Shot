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
public class cloud extends Volatiles{

    public cloud(){
        super();
        x = random.nextInt(900);
        y = 1000;
        speed = 5;
    } 

    @Override
    public void move() {
        this.y -= speed;
        if (y < 0)
            onScope = false;
    }
    
    
};
