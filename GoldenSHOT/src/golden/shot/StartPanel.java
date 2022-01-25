/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golden.shot;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author CBN
 */
public class StartPanel extends JPanel{
    private final Image StartPanelBackground;
    
    public StartPanel(){
        StartPanelBackground = new ImageIcon("img/start_background.png").getImage();
        this.setSize(StartPanelBackground.getWidth(this), StartPanelBackground.getHeight(this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.drawImage(StartPanelBackground, 0, 0, this);
    }

    
}
