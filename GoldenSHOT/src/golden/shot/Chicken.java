/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.util.ArrayList;

/**
 *
 * @author CBN
 */
public class Chicken {
    int x;
    final int y = 20;
    ArrayList<Egg> nest;
    
    public Chicken() {
        nest = new ArrayList<Egg>();
        x = 400;
    }
    
    public void move_left(){
        if (x>30)
            x-=20;
    }
    
    public void move_right(){
        if (x<860)
            x+=20;
    }
    
    public void spawn(){
     if(nest.size() < 3)
        nest.add(new Egg(this.x + 15));
    }
}
