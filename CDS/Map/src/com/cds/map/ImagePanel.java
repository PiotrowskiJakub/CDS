/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.map;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Sebastian
 */
public class ImagePanel extends JPanel{
    
    private BufferedImage image;
    private double latitude, longitude;
    

    public ImagePanel() {
       try {    
            URL mapUrl = new URL("https://maps.googleapis.com/maps/api/staticmap?center=27,31&zoom=6&size=500x500");
            image = ImageIO.read(mapUrl);
       } catch (IOException ex) {
            // handle exception...
       }
        repaint();
    }
    
    public void setCoordinates(String latitude, String longitude){
        try {    
            URL mapUrl = new URL("https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=6&size=500x500");
            image = ImageIO.read(mapUrl);
       } catch (IOException ex) {
            // handle exception...
       }
        repaint();
    }

    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, 600, 600, null);
    }
}
