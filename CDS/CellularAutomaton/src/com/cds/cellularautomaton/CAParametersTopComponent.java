/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.cellularautomaton;

import com.cds.api.MapsParametersContainer;
import com.cds.api.Person;
import com.cds.assetapi.AssetLoader;
import com.cds.lookup.LookupObject;
import com.google.gson.JsonArray;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * @author Jakub Piotrowski
 * Top component which makes simulation.
 */
@ConvertAsProperties(
        dtd = "-//com.cds.cellularautomaton//CAParameters//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "CAParametersTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true, position = 1)
@ActionID(category = "Window", id = "com.cds.cellularautomaton.CAParametersTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_CAParametersAction",
        preferredID = "CAParametersTopComponent"
)
@Messages({
    "CTL_CAParametersAction=CAParameters",
    "CTL_CAParametersTopComponent=Parametry symulacji",
    "HINT_CAParametersTopComponent=This is a CAParameters window"
})
public final class CAParametersTopComponent extends TopComponent implements Lookup.Provider
{
    private Thread simulationThread;
    private MapsParametersContainer mapsParametersContainer;
    private InstanceContent content;
    private Lookup lookup;
    private SecureRandom rand;
    private int imageWidth;
    private int generation;
    private boolean runningFlag;
    
    public CAParametersTopComponent() {
        initComponents();
        setName(Bundle.CTL_CAParametersTopComponent());
        setToolTipText(Bundle.HINT_CAParametersTopComponent());
        mapsParametersContainer = MapsParametersContainer.getInstance();
        loadMapParameters();
        content=new InstanceContent();
        lookup=new AbstractLookup(content);
        rand = new SecureRandom();
    }
    
    // WORKS
    private void loadMapParameters()
    {
        AssetLoader.load();
        JsonArray arrayMatrix = AssetLoader.mapParametersJsonMatrix;
        
        for(int i=0; i<arrayMatrix.size(); i++)
        {
            for(int j = 0; j < ((JsonArray)arrayMatrix.get(i)).size(); j++)
            {
                if(((JsonArray)arrayMatrix.get(i)).get(j).getAsString().equals("W"))
                    mapsParametersContainer.get(i, j).setLand(false);
                else
                    mapsParametersContainer.get(i, j).setLand(true);
            }
        }
    }
    
    // WORKS
    private void initializePopulation()
    {
        if(populationSizeText.getText().equals("") || radiusText.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Proszę uzupełnić parametry symulacji");
            return;
        }
        int population = Integer.parseInt(populationSizeText.getText());
        int radius = Integer.parseInt(radiusText.getText());
        
        imageWidth = MapsParametersContainer.SIZE;
        if(radius >= (int) (imageWidth / 3))
        {
            JOptionPane.showMessageDialog(null, "Za duży promień rozrzutu");
            return;
        }
        
        int startX = rand.nextInt(imageWidth - radius);
        int startY = rand.nextInt(imageWidth - radius);
        if(startX < radius)
            startX += radius;
        if(startY < radius)
            startY += radius;
        
        for(int i = 0; i < population; i++)
        {
            Person person;
            int x,y;
            switch(rand.nextInt(4))
            {
                case 0 : 
                    do
                    {
                        x = startX + rand.nextInt(radius);
                        y = startY + rand.nextInt(radius);
                    }while((Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius) || mapsParametersContainer.get(x, y).isLand() == false);
                    break;
                case 1 :
                    do
                    {
                        x = startX - rand.nextInt(radius);
                        y = startY + rand.nextInt(radius);
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius || mapsParametersContainer.get(x, y).isLand() == false);
                    break;
                case 2 :
                    do
                    {
                        x = startX + rand.nextInt(radius);
                        y = startY - rand.nextInt(radius);
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius || mapsParametersContainer.get(x, y).isLand() == false);
                    break;
                default :
                    do
                    {
                        x = startX - rand.nextInt(radius);
                        y = startY - rand.nextInt(radius);
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius || mapsParametersContainer.get(x, y).isLand() == false);
                    break;
            }
                
            boolean satisfaction = isWaterNear(x, y);
            person = new Person(rand.nextBoolean(), rand.nextInt(40), satisfaction);
            mapsParametersContainer.get(x, y).getPeopleList().add(person);
        }
        
        content.set(Collections.singleton(new LookupObject()), null);
        generation = 1;
        populatioNumberField.setText(Integer.toString(generation));
        runningFlag = true;
        startSimulation();
    }
    
    private void startSimulation()
    {
        while(runningFlag)
        {
            try 
            {
                Thread.sleep(300);
            } catch (InterruptedException ex) 
            {}
            for(int x = 0; x < MapsParametersContainer.SIZE; x++)
            {
                for(int y = 0; y < MapsParametersContainer.SIZE; y++)
                {
                    if(!mapsParametersContainer.get(x, y).getPeopleList().isEmpty())
                    {
                        try
                        {
                            if(mapsParametersContainer.get(x, y).getPeopleList().get(mapsParametersContainer.get(x, y).getPeopleList().size() - 1).isSatisfacion())
                            {
                                if(rand.nextInt(100) < 20)
                                {
                                    movePeople(x, y);
                                }
                            }
                            else
                            {
                                movePeople(x, y);
                            }
                        }catch(NullPointerException ex){}
                    }
                }
            }
            
            content.set(Collections.singleton(new LookupObject()), null);
            generation++;
            populatioNumberField.setText(Integer.toString(generation));
        }
        mapsParametersContainer.clearPopulation();
        startSimulationButton.setEnabled(true);
        stopSimulationButton.setEnabled(false);
        generation = 0;
        populatioNumberField.setText(Integer.toString(generation));
    }
    
    private void movePeople(int x, int y)
    {
        ArrayList<Integer> toRemove = new ArrayList<Integer>();
        for(int i = 0; i < mapsParametersContainer.get(x, y).getPeopleList().size(); i++)
        {
            Person tmpPerson = new Person(mapsParametersContainer.get(x, y).getPeopleList().get(i));
            toRemove.add(i);
            ArrayList<Integer> newCoordinates = null;
            do
            {
                newCoordinates = chooseNewPlace(x, y);
            }while(newCoordinates.size() != 2);
            mapsParametersContainer.get(newCoordinates.get(0), newCoordinates.get(1)).getPeopleList().add(tmpPerson);
        }
        mapsParametersContainer.get(x, y).getPeopleList().clear();
    }
    
    private ArrayList<Integer> chooseNewPlace(int x, int y)
    {
        ArrayList<Integer> array = new ArrayList<Integer>();
        Integer newX = null;
        Integer newY = null;
        switch(rand.nextInt(8))
        {
            case 0 : 
                if(x-1 >= 0 && y-1 >= 0)
                {
                    newX = new Integer(x - 1);
                    newY = new Integer(y - 1);
                    break;
                }        
            case 1 :
                if(x-1 >= 0)
                {
                    newX = new Integer(x - 1);
                    newY = new Integer(y);
                    break;
                }  
            case 2 : 
                if(x-1 >= 0 && y+1 < MapsParametersContainer.SIZE)
                {
                    newX = new Integer(x - 1);
                    newY = new Integer(y + 1);
                    break;
                }  
            case 3 : 
                if(y-1 >= 0)
                {
                    newX = new Integer(x);
                    newY = new Integer(y - 1);
                    break;
                }  
            case 4 :
                if(y+1 < MapsParametersContainer.SIZE)
                {
                    newX = new Integer(x);
                    newY = new Integer(y + 1);
                    break;
                }  
            case 5 : 
                if(x+1 < MapsParametersContainer.SIZE && y-1 >= 0)
                {
                    newX = new Integer(x + 1);
                    newY = new Integer(y - 1);
                    break;
                } 
            case 6 : 
                if(x+1 < MapsParametersContainer.SIZE)
                {
                    newX = new Integer(x + 1);
                    newY = new Integer(y);
                    break;
                } 
            default : 
                if(x+1 < MapsParametersContainer.SIZE && y+1 < MapsParametersContainer.SIZE)
                {
                    newX = new Integer(x + 1);
                    newY = new Integer(y + 1);
                    break;
                }
        }
        
        if(newX != null)
            array.add(newX);
        if(newY != null)
            array.add(newY);
        
        return array;
    }
    
    private boolean isWaterNear(int x, int y)
    {
        final int CHECK_AREA = 2; 
        if(x < CHECK_AREA || y < CHECK_AREA || x > MapsParametersContainer.SIZE - CHECK_AREA - 1 || y > MapsParametersContainer.SIZE - CHECK_AREA - 1)
        {
            for(int i = -CHECK_AREA; i <= CHECK_AREA; i++)
            {
                for(int j = -CHECK_AREA; j <= CHECK_AREA; j++)
                {
                    if(x+i < 0 || x+i > MapsParametersContainer.SIZE-1 || y+j < 0 || y+j > MapsParametersContainer.SIZE-1)
                        continue;
                    if(mapsParametersContainer.checkWater(x + i, y + j))
                        return true;
                }
            }
        }
        else
        {
            for(int i = -CHECK_AREA; i <= CHECK_AREA; i++)
            {
                for(int j = -CHECK_AREA; j <= CHECK_AREA; j++)
                {
                    if(mapsParametersContainer.checkWater(x + i, y + j))
                        return true;
                }
            }
        }
        
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        populationSizeLabel = new javax.swing.JLabel();
        startSimulationButton = new javax.swing.JButton();
        radiusLabel = new javax.swing.JLabel();
        radiusText = new javax.swing.JTextField();
        populationSizeText = new javax.swing.JTextField();
        stopSimulationButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        populatioNumberField = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setLayout(new java.awt.GridLayout());

        org.openide.awt.Mnemonics.setLocalizedText(populationSizeLabel, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.populationSizeLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(startSimulationButton, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.startSimulationButton.text")); // NOI18N
        startSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(radiusLabel, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.radiusLabel.text")); // NOI18N

        radiusText.setText(org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.radiusText.text")); // NOI18N

        populationSizeText.setText(org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.populationSizeText.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(stopSimulationButton, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.stopSimulationButton.text")); // NOI18N
        stopSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopSimulationButtonActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(205, 216, 216));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.jLabel1.text")); // NOI18N

        populatioNumberField.setBackground(new java.awt.Color(205, 216, 216));
        populatioNumberField.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        populatioNumberField.setForeground(new java.awt.Color(0, 51, 102));
        populatioNumberField.setText(org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.populatioNumberField.text")); // NOI18N
        populatioNumberField.setBorder(null);
        populatioNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                populatioNumberFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(populatioNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(populatioNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(populationSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(populationSizeText, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(radiusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(radiusText, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(startSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(stopSimulationButton)))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(populationSizeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(populationSizeLabel))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radiusText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radiusLabel))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stopSimulationButton)
                    .addComponent(startSimulationButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void startSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationButtonActionPerformed

        simulationThread = new Thread(new Runnable() {

            @Override
            public void run() {
                initializePopulation();
            }
        });
        simulationThread.start();
        startSimulationButton.setEnabled(false);
        stopSimulationButton.setEnabled(true);
    }//GEN-LAST:event_startSimulationButtonActionPerformed

    private void stopSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopSimulationButtonActionPerformed

        runningFlag = false;
    }//GEN-LAST:event_stopSimulationButtonActionPerformed

    private void populatioNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_populatioNumberFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_populatioNumberFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField populatioNumberField;
    private javax.swing.JLabel populationSizeLabel;
    private javax.swing.JTextField populationSizeText;
    private javax.swing.JLabel radiusLabel;
    private javax.swing.JTextField radiusText;
    private javax.swing.JButton startSimulationButton;
    private javax.swing.JButton stopSimulationButton;
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
    
    @Override
    public Lookup getLookup()
    {
        return lookup;
    }
}
