/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.map;

import com.cds.api.Coordinates;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static jdk.nashorn.internal.objects.NativeArray.map;
import org.json.JSONException;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.cds.map//Map//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "MapTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "com.cds.map.MapTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MapAction",
        preferredID = "MapTopComponent"
)
@Messages({
    "CTL_MapAction=Map",
    "CTL_MapTopComponent=Map Window",
    "HINT_MapTopComponent=This is a Map window"
})
public final class MapTopComponent extends TopComponent  implements LookupListener {
    private final double topCenterLatitudeDifferent = 4.8403255;
    private final double leftCenterLongitudeDifferent = 5.5447411;
    private final double latitudeDifferent = 0.032268835;
    private final double longitudeDifferent = 0.0369649405;
    private Lookup.Result<Coordinates> allCoordinates;
    private HashMap<PointCoordinates, PointParameters> pointsHashMap = new HashMap<PointCoordinates, PointParameters>();
    private double centerCoordinateLatitude;
    private double centerCoordinateLongitude;
    private double leftTopCoordinateLatitude;
    private double leftTopCoordinateLongitude;
    
    private double tmpLatitude;
    private double tmpLongitude;
    
    public MapTopComponent() {
        initComponents();
        setName(Bundle.CTL_MapTopComponent());
        setToolTipText(Bundle.HINT_MapTopComponent());
        
       allCoordinates = WindowManager.getDefault().findTopComponent("MapParametersTopComponent").getLookup().lookupResult(Coordinates.class);
       allCoordinates.addLookupListener(this);
    }
    
    public void resultChanged(LookupEvent le) {
       
        centerCoordinateLatitude = allCoordinates.allInstances().iterator().next().getLatitude();
        centerCoordinateLongitude = allCoordinates.allInstances().iterator().next().getLongitude();
        leftTopCoordinateLatitude = centerCoordinateLatitude - topCenterLatitudeDifferent;
        leftTopCoordinateLongitude = centerCoordinateLongitude - leftCenterLongitudeDifferent;
        
        
        imagePanel1.setCoordinates(String.valueOf(centerCoordinateLatitude), String.valueOf(centerCoordinateLongitude));
        
        tmpLatitude = leftTopCoordinateLatitude;
        tmpLongitude = leftTopCoordinateLongitude;
        
        for(int i = 0; i < 300; i++)
        {
            for(int j = 0; j < 300; j++)
            {
                pointsHashMap.put(new PointCoordinates(i, j), new PointParameters(tmpLatitude, tmpLongitude));
                //System.out.println("i: " + i + " j: " + j + " latitude: " + tmpLatitude + " longitude: " + tmpLongitude );
                tmpLongitude+= longitudeDifferent;
            }
            tmpLongitude = leftTopCoordinateLongitude;
            tmpLatitude += latitudeDifferent;
        }
        
        
        //Przykład dzialania pobierania danych - tablica tmp dla przykladu
        
        double[][] tmp = new double[2][2];
    	tmp[0][0] = 27;
    	tmp[0][1] = 31;
    	tmp[1][0] = 28;
    	tmp[1][1] = 35;
        
        //odkomentuj linijki 109-114 i tu jakis maly blad z brakiem modulu/klasy, jak to sie naprawi to jest pelne narzedzie, w eclipse all dziala
        /*
        HttpConnect httpConnect = new HttpConnect();
        try {
            httpConnect.http(tmp);
        } catch (JSONException ex) {
            Exceptions.printStackTrace(ex);
        }
        */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        imagePanel1 = new com.cds.map.ImagePanel();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout imagePanel1Layout = new javax.swing.GroupLayout(imagePanel1);
        imagePanel1.setLayout(imagePanel1Layout);
        imagePanel1Layout.setHorizontalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        imagePanel1Layout.setVerticalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(imagePanel1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.cds.map.ImagePanel imagePanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
