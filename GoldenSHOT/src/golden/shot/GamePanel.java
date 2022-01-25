/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author CBN
 */
public class GamePanel extends JPanel implements ActionListener,KeyListener{
    Chicken theChicken;
    ArrayList<cloud> sky;
    ArrayList<Cat> pillow;
    ArrayList<Worm> worms;
    ArrayList<GoldenWorm> gworms;
    int score = 0;
    String scoreT;
    int level = 1;
    int cloudTime = 0;
    int catTime = 0;
    int catV = 1;
    int targetTime = 0;
    int gtargetTime = 0;
    int cTime = 0;
    int totalTime = 0;
    int catSpeed;
    boolean cCheck = false;
    boolean LEVEL_FINISHED = false;
    boolean GAME_OVER = false;
    //image variables
    private final Image GamePanelBackground;
    private final Image CloudImage;
    private final Image ChickenImage;
    private final Image AltChickenImage;
    private final Image WormImage;
    private final Image GoldenWormImage;
    private final Image EggImage;
    private final Image CatImage1;
    private final Image CatImage2;
    private final Image CatImage3;
    private Image ActiveChickenImage;
    //sound variables
    private AudioInputStream spawnSound;
    private AudioInputStream catSound;
    private AudioInputStream scoreSound;
    
    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        // this is actually action performed 
        cloudTime += 15;
        catTime += 15;
        targetTime += 30;
        gtargetTime += 15;
        cTime += 15;
        totalTime += 15;
        
        difficulty();
        
        if (cloudTime / 1000.0 >= 1.5) {
            sky.add(new cloud());
            cloudTime = 0;
        }
        if(catTime /1000.0 >=2){
            Cat c = new Cat(catSpeed);
            if(catV == 4)
                catV = 1;
            switch(catV){
                case 1: c.CatImage = CatImage1; catV += 1; break;
                case 2: c.CatImage = CatImage2; catV += 1; break;
                case 3: c.CatImage = CatImage3; catV += 1; break;
            }
            
            pillow.add(c);
            catTime = 0;
        }
        
        if(targetTime /1000.0 >=4.5){
            worms.add(new Worm());
            targetTime = 0;
        }
        
        if(gtargetTime /1000.0 >= 5){
            gworms.add(new GoldenWorm());
            gtargetTime = 0;
        }
        
        
        
       for(cloud c : sky){
           c.move();
       } 
       for(Cat c : pillow){
           c.move();
       }
       for(Worm w : worms){
           w.move();
       }
       for(GoldenWorm gw : gworms){
           gw.move();
       }
       for(Egg E : theChicken.nest){
           E.move();
       }
        
        g.drawImage(GamePanelBackground, 0, 0, this);
        for(cloud c : sky){
            if(c.onScope)
                continue;
            else
                sky.remove(c);
        }
        for(Cat c : pillow){
            if(c.onScope)
                continue;
            else
                pillow.remove(c);
        }
        for(Worm w : worms){
            if(w.onScope)
                continue;
            else
                worms.remove(w);
        }
        for(GoldenWorm gw : gworms){
            if(gw.onScope)
                continue;
            else
                gworms.remove(gw);
        }
        for(Egg e : theChicken.nest){
            if(e.onScope)
                continue;
            else
                theChicken.nest.remove(e);
        }
        for(cloud c : sky){
            g.drawImage(CloudImage, c.x, c.y, CloudImage.getWidth(this), CloudImage.getHeight(this), this);//drawing clouds
        }
       g.drawImage(ActiveChickenImage, theChicken.x, theChicken.y, ChickenImage.getWidth(this), ChickenImage.getHeight(this), this); // drawing chicken
       for(Cat c : pillow){
            g.drawImage(c.CatImage, c.x, c.y, c.CatImage.getWidth(this), c.CatImage.getHeight(this), this);//drawing cats
        }
        for(Worm w : worms){
            g.drawImage(WormImage, w.x, w.y, WormImage.getWidth(this), WormImage.getHeight(this), this);//drawing worms
        }
        for(GoldenWorm gw : gworms){
            g.drawImage(GoldenWormImage, gw.x, gw.y, GoldenWormImage.getWidth(this), GoldenWormImage.getHeight(this), this);//drawing worms
        }
        for(Egg e : theChicken.nest){
            g.drawImage(EggImage, e.x, e.y, EggImage.getWidth(this), EggImage.getHeight(this), this);//drawing eggs
        }
       
        if(cTime / 1000.0 >= 1.0 && cCheck){
            ActiveChickenImage = ChickenImage;
            cTime = 0;
            cCheck = false;
        }
        
        hitCheck();
        gameCheck();
        scoreT = "Total Score: " + score;
        g.setFont(new Font("Roboto",Font.PLAIN,18));
        g.drawString(scoreT, 780, 15);
        levelCheck();
     
    }
    
    
    public void gameCheck(){
        Rectangle chicken = new Rectangle(theChicken.x, theChicken.y,ActiveChickenImage.getWidth(this), ActiveChickenImage.getHeight(this));
        for(Cat c : pillow){
            if(chicken.intersects(new Rectangle(c.x, c.y, c.CatImage.getWidth(this) -3, c.CatImage.getHeight(this) -3))){
                GAME_OVER = true;
            }
        }
        return; 
    }
    
    public void levelCheck(){
        if(totalTime / 1000.0 >= 300 && level != 3){
            LEVEL_FINISHED = true;
            ActiveChickenImage = ChickenImage;
        }
        if(level == 1 && score >=50){
            LEVEL_FINISHED = true;
            ActiveChickenImage = ChickenImage;
        }
        if(level == 2 && score >=100){
            LEVEL_FINISHED = true;
            ActiveChickenImage = ChickenImage;
        }
        return; 
    }
    
    public void hitCheck(){
        for(Egg e : theChicken.nest)
            for(Worm w : worms)
                if(new Rectangle(e.x, e.y, EggImage.getWidth(this), EggImage.getHeight(this)).intersects(new Rectangle(w.x, w.y, WormImage.getWidth(this), WormImage.getHeight(this)))){
                    e.onScope = false;
                    w.onScope = false;
                    score += 1;
                    sounds('t');
            }
        for(Egg e : theChicken.nest)
            for(GoldenWorm gw : gworms)
                if(new Rectangle(e.x, e.y, EggImage.getWidth(this), EggImage.getHeight(this)).intersects(new Rectangle(gw.x, gw.y, GoldenWormImage.getWidth(this), GoldenWormImage.getHeight(this)))){
                    e.onScope = false;
                    gw.onScope = false;
                    score += 3;
                    sounds('t');
            }
        for(Egg e : theChicken.nest)
            for(Cat c : pillow)
                if(new Rectangle(e.x, e.y, EggImage.getWidth(this), EggImage.getHeight(this)).intersects(new Rectangle(c.x, c.y, c.CatImage.getWidth(this), c.CatImage.getHeight(this)))){
                    e.onScope = false;
                    c.onScope = false;
                    sounds('c');
            }
    }
    
    public void difficulty(){
        if(totalTime /60000.0 < 1){
            switch(level){
                case 1: catSpeed = 8; break;
                case 2: catSpeed = 12; break;
                case 3: catSpeed = 16; break;
            }
            switch(level){
                case 1: catTime +=0 ; break;
                case 2: catTime +=5 ; break;
                case 3: catTime +=10 ; break;
            }
        }
        else if(totalTime /60000.0 >= 1 && totalTime /60000.0 < 2){
            switch(level){
                case 1: catSpeed = 12; break;
                case 2: catSpeed = 16; break;
                case 3: catSpeed = 20; break;
            }
            switch(level){
                case 1: catTime +=5 ; break;
                case 2: catTime +=10 ; break;
                case 3: catTime +=15 ; break;
            }
        }
        else if(totalTime /60000.0 >= 2 && totalTime /60000.0 < 3){
            switch(level){
                case 1: catSpeed = 16; break;
                case 2: catSpeed = 20; break;
                case 3: catSpeed = 24; break;
            }
            switch(level){
                case 1: catTime += 10; break;
                case 2: catTime += 15; break;
                case 3: catTime += 20; break;
            }
        }
        else if(totalTime /60000.0 >= 3){
            switch(level){
                case 1: catSpeed = 20; break;
                case 2: catSpeed = 24; break;
                case 3: catSpeed = 30; break;
            }
            switch(level){
                case 1: catTime += 10; break;
                case 2: catTime += 15; break;
                case 3: catTime += 20; break;
            }
        }
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       this.repaint();
    }

    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key){
            case KeyEvent.VK_LEFT ->  theChicken.move_left();
            case KeyEvent.VK_RIGHT -> theChicken.move_right();
            case KeyEvent.VK_SPACE -> {
                theChicken.spawn(); ActiveChickenImage = AltChickenImage; cTime = 0; cCheck = true; sounds('e');
            }
        }
    }
    
    public void sounds(char c){
        try {
            scoreSound = AudioSystem.getAudioInputStream(new File("audio/score.wav"));
            spawnSound = AudioSystem.getAudioInputStream(new File("audio/spawn.wav"));
            catSound = AudioSystem.getAudioInputStream(new File("audio/cat.wav"));
            Clip sound = AudioSystem.getClip();
            switch(c){
                case 'e': sound.open(spawnSound); sound.start(); break;
                case 'c': sound.open(catSound); sound.start(); break;
                case 't': sound.open(scoreSound); sound.start(); break;
            }
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GamePanel() {
        //object variables 
        theChicken = new Chicken();
        sky = new ArrayList<cloud>();
        pillow = new ArrayList<Cat>();
        worms = new ArrayList<Worm>();
        gworms = new ArrayList<GoldenWorm>();
        
        //image variables
        GamePanelBackground = new ImageIcon("img/game_background.png").getImage();
        CloudImage = new ImageIcon("img/cloud.png").getImage();
        ChickenImage = new ImageIcon("img/chicken.png").getImage();
        AltChickenImage = new ImageIcon("img/altchicken.png").getImage();
        EggImage = new ImageIcon("img/golden_egg.png").getImage();
        WormImage = new ImageIcon("img/worm.png").getImage();
        GoldenWormImage = new ImageIcon("img/goldenworm.png").getImage();
        CatImage1 = new ImageIcon("img/cat1.png").getImage();
        CatImage2 = new ImageIcon("img/cat2.png").getImage();
        CatImage3 = new ImageIcon("img/cat3.png").getImage();
        ActiveChickenImage = ChickenImage; 
    }
    
    
    @Override
    public void repaint() {
        super.repaint(); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}
