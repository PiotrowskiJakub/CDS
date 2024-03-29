/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.map;

import com.cds.api.MapsParametersContainer;
import com.cds.api.Person;
import com.cds.lookup.LookupObject;
import com.cds.lookup.PeopleLookupProvider;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Sebastian
 */
public class ImagePanel extends JPanel implements LookupListener, Lookup.Provider{
    
    private int color = 100;
    private int populationNumber;
    private BufferedImage image, orginalImage;
    private Lookup.Result<LookupObject> newGenerationFlag;
    private MapsParametersContainer parametersContainer;
    private Lookup.Result<Color> populationColor = null;
    
    private InstanceContent content;
    private Lookup lookup;
    

    public ImagePanel() 
    {
        newGenerationFlag = WindowManager.getDefault().findTopComponent("CAParametersTopComponent").getLookup().lookupResult(LookupObject.class);
        newGenerationFlag.addLookupListener(this);
        
        populationColor = WindowManager.getDefault().findTopComponent("ColorChooserTopComponentTopComponent").getLookup().lookupResult(Color.class);
        populationColor.addLookupListener(this);
        
        
        content=new InstanceContent();
        lookup=new AbstractLookup(content);
        
    }
    
    @Override
    public Lookup getLookup()
    {
        return lookup;
    }
    
    public void setCoordinates(String latitude, String longitude){
        try {    
            URL mapUrl = new URL("https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=6&size=500x500&sensor=false&visual_refresh=true&style=element:labels|visibility:off&style=feature:transit|visibility:off&style=feature:poi|visibility:off&style=feature:road|visibility:off&style=feature:administrative|visibility:off");
            orginalImage = ImageIO.read(mapUrl);
            image = deepCopy(orginalImage);
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
        
//        double xScale = getWidth() / (double) image.getWidth();
//        double yScale = getHeight() / (double) image.getHeight();
//        if (xScale > yScale) scale = yScale;
//        else scale = xScale;
//        if(scale > 1) scale = 1;
//                g.drawImage(image,getWidth() / 2 - (int) ((image.getWidth() * scale) / 2),
//                getHeight() / 2 - (int) ((image.getHeight() * scale) / 2),
//                (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
        
        g.drawImage(image,getWidth()/2 - image.getWidth()/2, getHeight()/2 -  image.getHeight()/2, image.getWidth(), image.getHeight(), null);
    }

    @Override
    public void resultChanged(LookupEvent le) 
    {    
        
        if(image == null)
            return;
        
        if(populationColor.allInstances().size() > 0){
            color = populationColor.allInstances().iterator().next().getRGB();             
        }
        
        
        image = deepCopy(orginalImage);
        revalidate();
        repaint();
        
        populationNumber = 0;
        parametersContainer = MapsParametersContainer.getInstance();
        for(int i = 0; i < MapsParametersContainer.SIZE; i++)
        {
            for(int j = 0; j < MapsParametersContainer.SIZE; j++)
            {
                ArrayList<Person> localPeople = parametersContainer.get(i, j).getPeopleList();
                if(!localPeople.isEmpty())
                {
                    image.setRGB(i, j, color);
                    populationNumber += localPeople.size();
                    repaint();
                }
            }
        }

        MapsParametersContainer.POPULATION_SIZE = populationNumber;
        PeopleLookupProvider.getInstance().getContent().set(Collections.singleton(new Integer(populationNumber)), null);
    }
    
    private BufferedImage deepCopy(BufferedImage bi) 
    {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
