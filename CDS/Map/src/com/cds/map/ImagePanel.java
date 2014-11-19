/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.map;

import com.cds.api.Coordinates;
import com.cds.api.PersonCell;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sebastian
 */
public class ImagePanel extends JPanel implements LookupListener{
    
    private BufferedImage image, orginalImage;
    private double latitude, longitude;
    private Lookup.Result<PersonCell> peopleList;
    

    public ImagePanel() 
    {
        peopleList = WindowManager.getDefault().findTopComponent("CAParametersTopComponent").getLookup().lookupResult(PersonCell.class);
        peopleList.addLookupListener(this);
    }
    
    public void setCoordinates(String latitude, String longitude){
        try {    
            URL mapUrl = new URL("https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=6&size=500x500&sensor=false&visual_refresh=true&style=element:labels|visibility:off&style=feature:transit|visibility:off&style=feature:poi|visibility:off&style=feature:road|visibility:off&style=feature:administrative|visibility:off");
            orginalImage = ImageIO.read(mapUrl);
            image = orginalImage;
       } catch (IOException ex) {}
        this.revalidate();
        this.repaint();
    }

    
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if(image == null)
            return;
        double scale;
        double xScale = getWidth() / (double) image.getWidth();
        double yScale = getHeight() / (double) image.getHeight();
        if (xScale > yScale) scale = yScale;
        else scale = xScale;
        g.drawImage(image,getWidth() / 2 - (int) ((image.getWidth() * scale) / 2),
                getHeight() / 2 - (int) ((image.getHeight() * scale) / 2),
                (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
    }

    @Override
    public void resultChanged(LookupEvent le) 
    {
        if(image == null)
            return;
        
        int scale = this.getWidth()/300;
        PersonCell person = peopleList.allInstances().iterator().next();
        Graphics g = image.getGraphics();
        g.setColor(new Color(100));
        g.fillOval(person.getLivingPlace().getX()*scale, person.getLivingPlace().getY()*scale, 2, 2);
        repaint();
    }
}
