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
public class Egg extends Volatiles{
   
    public Egg(int x){
        super();
        this.x = x;
        this.y = 20;
        speed = 10;
    }
    
    @Override
    public void move(){
        this.y += speed;
        if (this.y > 1000)
            onScope = false;
    }
    
}
