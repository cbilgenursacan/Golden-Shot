/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.awt.Image;

/**
 *
 * @author CBN
 */
public class Cat extends Volatiles{
    
    Image CatImage;
    public Cat(int speed){
        super();
        y = 1000;
        x = random.nextInt(820);   
        this.speed = speed;
    }
  
    @Override
    public void move(){
        this.y -= speed;
        if(this.y < 0)
            onScope = false;
    }
}
