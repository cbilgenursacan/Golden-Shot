/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author CBN
 */
public class EndPanel extends JPanel{
    private Image EndPanelBackground;
    Integer score = 0;
    String highScore = "0";
    public EndPanel(){
        EndPanelBackground = new ImageIcon("img/end_background.png").getImage();
        this.setSize(918, 1024);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(EndPanelBackground, 0, 0, this); 
        g.setFont(new Font("Courier",Font.PLAIN,48));
        g.drawString(score.toString(), 434, 320);
        g.drawString(highScore, 495, 395);
    }

    @Override
    public void repaint() {
        super.repaint(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
