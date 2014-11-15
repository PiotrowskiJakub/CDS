/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.mapparameters;

import com.cds.api.Coordinates;
import java.util.Collections;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.cds.mapparameters//MapParameters//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "MapParametersTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = true)
@ActionID(category = "Window", id = "com.cds.mapparameters.MapParametersTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MapParametersAction",
        preferredID = "MapParametersTopComponent"
)
@Messages({
    "CTL_MapParametersAction=MapParameters",
    "CTL_MapParametersTopComponent=MapParameters Window",
    "HINT_MapParametersTopComponent=This is a MapParameters window"
})
public final class MapParametersTopComponent extends TopComponent implements Lookup.Provider {

    private InstanceContent content;
    private Lookup lookup;
    
    public MapParametersTopComponent () {
        initComponents();
        setName(Bundle.CTL_MapParametersTopComponent());
        setToolTipText(Bundle.HINT_MapParametersTopComponent());
        
        content=new InstanceContent();
        lookup=new AbstractLookup(content);

    }
    
    @Override
    public Lookup getLookup()
    {
        return lookup;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        latitudeLabel = new javax.swing.JLabel();
        latitudeValue = new javax.swing.JTextField();
        longitudeLabel = new javax.swing.JLabel();
        longitudeValue = new javax.swing.JTextField();
        showMapButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.jLabel1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(latitudeLabel, org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.latitudeLabel.text")); // NOI18N

        latitudeValue.setText(org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.latitudeValue.text")); // NOI18N
        latitudeValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latitudeValueActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(longitudeLabel, org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.longitudeLabel.text")); // NOI18N

        longitudeValue.setText(org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.longitudeValue.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(showMapButton, org.openide.util.NbBundle.getMessage(MapParametersTopComponent.class, "MapParametersTopComponent.showMapButton.text")); // NOI18N
        showMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showMapButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(longitudeLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(longitudeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(latitudeLabel)
                            .addGap(18, 18, 18)
                            .addComponent(latitudeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(showMapButton))
                .addContainerGap(215, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(latitudeLabel)
                    .addComponent(latitudeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(longitudeLabel)
                    .addComponent(longitudeValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showMapButton)
                .addContainerGap(189, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void latitudeValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latitudeValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_latitudeValueActionPerformed

    private void showMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showMapButtonActionPerformed
           content.set(Collections.singleton(new Coordinates(latitudeValue.getText(), longitudeValue.getText())), null);
    }//GEN-LAST:event_showMapButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel latitudeLabel;
    private javax.swing.JTextField latitudeValue;
    private javax.swing.JLabel longitudeLabel;
    private javax.swing.JTextField longitudeValue;
    private javax.swing.JButton showMapButton;
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
