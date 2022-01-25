/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author CBN
 */
public class MainFrame extends JFrame implements ActionListener {
    private GamePanel gamePanel;
    Timer gameTimer = new Timer(15, this);
    private final StartPanel openingPanel;
    private final ContinuePanel nextlevelPanel;
    private final EndPanel endingPanel;
    private final JButton startButton;
    private final JButton menuButton;
    private final JButton exitButton;
    private final JButton continueButton;
    //Image Variables
    private ImageIcon startIcon = new ImageIcon("img/start_button.png");
    private ImageIcon exitIcon = new ImageIcon("img/exit_button.png");
    private ImageIcon menuIcon = new ImageIcon("img/menu_button.png");
    //Sound Variables
    private AudioInputStream buttonSound;
    private AudioInputStream dieSound;
    private AudioInputStream levelUpSound;
    
    public MainFrame() {
        //MainFrame attributes
        setTitle("Golden Shot");
        setResizable(false);
        setFocusable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(918,1024);
        //StartPanel attributes
        openingPanel = new StartPanel();
        openingPanel.setFocusable(true);
        openingPanel.setLayout(null);
        startButton = new JButton(startIcon);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(this);
        startButton.setBounds(370, 600, 200, 90);
        openingPanel.add(startButton);
        //ContinuePanel attributes
        nextlevelPanel = new ContinuePanel();
        nextlevelPanel.setFocusable(true);
        nextlevelPanel.setLayout(null);
        continueButton = new JButton("NEXT LEVEL >");
        continueButton.setFont(new Font("Georgia",Font.PLAIN,24));
        continueButton.addActionListener(this);
        continueButton.setBackground(Color.MAGENTA);
        continueButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
                continueButton.setBackground(Color.MAGENTA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
                continueButton.setBackground(Color.GREEN);
            }
        });
        continueButton.setBounds(685, 700, 250, 90);
        nextlevelPanel.add(continueButton);
        //EndPanel attributes
        endingPanel = new EndPanel();
        endingPanel.setFocusable(true);
        endingPanel.setLayout(null);
        menuButton = new JButton();
        menuButton.addActionListener(this);
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setBounds(500,450,230,110);
        exitButton = new JButton();
        exitButton.addActionListener(this);
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBounds(230,890 , 463, 95);
        endingPanel.add(menuButton);
        endingPanel.add(exitButton);
        
        add(openingPanel);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
            sounds('b');
            start();
        }
        
        if(e.getSource() == menuButton){
            sounds('b');
            endingPanel.setVisible(false);
            remove(endingPanel);
            openingPanel.setVisible(true);
            add(openingPanel);
        }
        
        if(e.getSource() == continueButton){
            sounds('b');
            
            nextlevelPanel.setVisible(false);
            add(gamePanel);
            gameTimer.start();
            gamePanel.setVisible(true);
            add(gamePanel);
            gamePanel.requestFocus();
        }
        
        if(e.getSource() == exitButton){
            sounds('b');
            System.exit(0);
        }
        
        if(e.getSource() == gameTimer){
            gamePanel.actionPerformed(e);
            
            if(gamePanel.LEVEL_FINISHED){
                gamePanel.pillow.clear();
                gamePanel.worms.clear();
                gamePanel.gworms.clear();
                gamePanel.theChicken.nest.clear();
                gameTimer.stop();
                gamePanel.LEVEL_FINISHED = false;
                gamePanel.cCheck = false;
                gamePanel.cTime = 0;
                gamePanel.catTime = 0;
                gamePanel.targetTime = 0;
                gamePanel.gtargetTime = 0;
                gamePanel.totalTime = 0;
                gamePanel.level += 1;
                gamePanel.repaint();
                sounds('l');
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                gamePanel.setVisible(false);
                remove(gamePanel);
                nextlevelPanel.setVisible(true);
                add(nextlevelPanel);
            }
            
            if(gamePanel.GAME_OVER){
                gameTimer.stop();
                gamePanel.setVisible(false);
                remove(gamePanel);
                endingPanel.score = gamePanel.score;
                highScore();
                endingPanel.repaint();
                sounds('d');
                endingPanel.setVisible(true);
                add(endingPanel);
            }
        }
    }

    private void start(){
            openingPanel.setVisible(false);
            remove(openingPanel);
            gamePanel = new GamePanel();  
            gamePanel.setSize(918,1024);
            gamePanel.setLayout(null);
            gamePanel.addKeyListener(gamePanel);
            gamePanel.setFocusable(true);
            gamePanel.setFocusTraversalKeysEnabled(false);
            gamePanel.setVisible(true);
            gameTimer.start();
            add(gamePanel);
            gamePanel.requestFocus();
        }
   private void sounds(char c){
       try {
            buttonSound = AudioSystem.getAudioInputStream(new File("audio/button.wav"));
            dieSound = AudioSystem.getAudioInputStream(new File("audio/gameover.wav"));
            levelUpSound = AudioSystem.getAudioInputStream(new File("audio/levelup.wav"));
            Clip sound = AudioSystem.getClip();
            switch(c){
                case 'b': sound.open(buttonSound); sound.start(); break;
                case 'd': sound.open(dieSound); sound.start(); break;
                case 'l': sound.open(levelUpSound); sound.start(); break;
            }
        } catch (LineUnavailableException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   private void highScore(){
       FileInputStream fis = null; 
       FileOutputStream fos = null;
       int value = 0;
       String hs;
       String readed = "";
       try {
            fis = new FileInputStream("hs.txt");
            while (true) {
                value = fis.read();
                if (value == -1) 
                    break;
                else
                    readed += (char)value;
           }
            fis.close();
            hs = readed;
            System.out.println(hs);
            if(gamePanel.score > Integer.parseInt(readed)){
                fos = new FileOutputStream("hs.txt");
                readed = Integer.toString(gamePanel.score) ;
                byte[] line = readed.getBytes();
                fos.write(line);
                fos.close();
                hs = Integer.toString(gamePanel.score) ;
            }
            endingPanel.highScore = hs;
            System.out.println(hs);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   public static void main(String[] args) {
        new MainFrame();
    }
    
}
