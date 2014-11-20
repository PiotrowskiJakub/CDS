/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.cellularautomaton;

import com.cds.api.Coordinates;
import com.cds.api.PersonCell;
import com.cds.api.PointCoordinates;
import com.cds.map.MapTopComponent;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
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
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
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
    private InstanceContent content;
    private Lookup lookup;
    
    public CAParametersTopComponent() {
        initComponents();
        setName(Bundle.CTL_CAParametersTopComponent());
        setToolTipText(Bundle.HINT_CAParametersTopComponent());
        content=new InstanceContent();
        lookup=new AbstractLookup(content);
    }
    
    private void initSimulation()
    {
        if(populationSizeText.getText().equals("") || radiusText.getText().equals(""))
        {
            JOptionPane.showMessageDialog(null, "Proszę uzupełnić parametry symulacji");
            return;
        }
        SecureRandom rand = new SecureRandom();
        int population = Integer.parseInt(populationSizeText.getText());
        int radius = Integer.parseInt(radiusText.getText());
        double imageWidth = ((MapTopComponent) WindowManager.getDefault().findTopComponent("MapTopComponent")).getImagePanel1().getImageWidthWithScale();
        if(radius >= imageWidth / 2)
        {
            JOptionPane.showMessageDialog(null, "Za duży promień rozrzutu");
            return;
        }
        double scale = imageWidth/300;
        
        int startX = rand.nextInt(300 - ((int) (radius/scale)));
        int startY = rand.nextInt(300 - ((int) (radius/scale)));
        System.out.println(startX * scale + " " + startY * scale);
        if(startX < ((int) (radius/scale)))
            startX += ((int) (radius/scale));
        if(startY < ((int) (radius/scale)))
            startY += ((int) (radius/scale));
        
        System.out.println(startX * scale + " " + startY * scale);
        for(int i = 0; i < population; i++)
        {
            PersonCell person;
            int x,y;
            switch(rand.nextInt(4))
            {
                case 0 : 
                    do
                    {
                        x = startX + rand.nextInt((int) (radius/scale));
                        y = startY + rand.nextInt((int) (radius/scale));
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius/scale);
                    break;
                case 1 :
                    do
                    {
                        x = startX - rand.nextInt((int) (radius/scale));
                        y = startY + rand.nextInt((int) (radius/scale));
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius/scale);
                    break;
                case 2 :
                    do
                    {
                        x = startX + rand.nextInt((int) (radius/scale));
                        y = startY - rand.nextInt((int) (radius/scale));
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius/scale);
                    break;
                default :
                    do
                    {
                        x = startX - rand.nextInt((int) (radius/scale));
                        y = startY - rand.nextInt((int) (radius/scale));
                    }while(Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2)) > radius/scale);
                    break;
            }
                
//            System.out.println(x * scale + " " + y * scale);
            person = new PersonCell(rand.nextBoolean(), rand.nextInt(100), new PointCoordinates(x, y));
            content.set(Collections.singleton(person), null);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        populationSizeLabel = new javax.swing.JLabel();
        populationSizeText = new javax.swing.JTextField();
        radiusLabel = new javax.swing.JLabel();
        radiusText = new javax.swing.JTextField();
        startSimulationButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(populationSizeLabel, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.populationSizeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        add(populationSizeLabel, gridBagConstraints);

        populationSizeText.setText(org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.populationSizeText.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(populationSizeText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(radiusLabel, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.radiusLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 20, 0);
        add(radiusLabel, gridBagConstraints);

        radiusText.setText(org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.radiusText.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        add(radiusText, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(startSimulationButton, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.startSimulationButton.text")); // NOI18N
        startSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSimulationButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(startSimulationButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void startSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationButtonActionPerformed
        initSimulation();
    }//GEN-LAST:event_startSimulationButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel populationSizeLabel;
    private javax.swing.JTextField populationSizeText;
    private javax.swing.JLabel radiusLabel;
    private javax.swing.JTextField radiusText;
    private javax.swing.JButton startSimulationButton;
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
