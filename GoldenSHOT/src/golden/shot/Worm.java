/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

/**
 *
 * @author CBN
 */
public class Worm extends Volatiles{
    
    public Worm(){
        super();
        this.x = random.nextInt(820);
        this.y = 1000;
        this.speed = 8;
    }

    @Override
    public void move() {
        this.y -= speed;
        if(this.y < 0)
            onScope = false;
    }
    
    
}
