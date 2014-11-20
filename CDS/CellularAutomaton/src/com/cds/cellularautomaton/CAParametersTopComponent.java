/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.cellularautomaton;

import com.cds.api.PersonCell;
import com.cds.api.PointCoordinates;
import com.cds.map.MapTopComponent;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
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
    private LinkedList<PersonCell> peopleList;
    private double imageWidth;
    private Thread simulationThread;
    
    public CAParametersTopComponent() {
        initComponents();
        setName(Bundle.CTL_CAParametersTopComponent());
        setToolTipText(Bundle.HINT_CAParametersTopComponent());
        content=new InstanceContent();
        lookup=new AbstractLookup(content);
        peopleList = new LinkedList<PersonCell>();
        stopSimulationButton.setEnabled(false);
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
        
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                imageWidth = ((MapTopComponent) WindowManager.getDefault().findTopComponent("MapTopComponent")).getImagePanel1().getImageWidthWithScale();
            }
        });
        if(radius >= imageWidth / 2)
        {
            JOptionPane.showMessageDialog(null, "Za duży promień rozrzutu");
            return;
        }
        double scale = imageWidth/300;
        
        int startX = rand.nextInt(300 - ((int) (radius/scale)));
        int startY = rand.nextInt(300 - ((int) (radius/scale)));
        if(startX < ((int) (radius/scale)))
            startX += ((int) (radius/scale));
        if(startY < ((int) (radius/scale)))
            startY += ((int) (radius/scale));
        
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
                
            person = new PersonCell(rand.nextBoolean(), rand.nextInt(40), new PointCoordinates(x, y));
            peopleList.add(person);
            content.set(Collections.singleton(person), null);
        }
        
        startSimulation();
    }
    
    private void startSimulation()
    {
        SecureRandom rand = new SecureRandom();
        while(true)
        {
            LinkedList<PersonCell> toRemove = new LinkedList<PersonCell>();
            LinkedList<PersonCell> toAdd = new LinkedList<PersonCell>();
            synchronized(peopleList)
            {
//                try 
//                {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
                for(PersonCell person : peopleList)
                {
                    person.setAge(person.getAge() + 1);
                    if(person.getAge() > 105)
                    {
                        toRemove.add(person);
                        continue;
                    }        
                    LinkedList<PersonCell> neighbours = findNeighbours(person);
                    if(neighbours.isEmpty())
                    {
                        toRemove.add(person);
                        continue;
                    }

                    for(PersonCell neighbour : neighbours)
                    {
                        if(neighbour.getSex() != person.getSex())
                        {
                            int fatherX = person.getLivingPlace().getX();
                            int fatherY = person.getLivingPlace().getY();
                            int motherX = neighbour.getLivingPlace().getX();
                            int motherY = neighbour.getLivingPlace().getY();
                            int choosedX, choosedY;
                            switch(rand.nextInt(11))
                            {
                                case 0:
                                    choosedX = fatherX++; choosedY = fatherY++;
                                    break;
                                case 1:
                                    choosedX = fatherX++; choosedY = motherY--;
                                    break;
                                case 2:
                                    choosedX = motherX--; choosedY = motherY++;
                                    break;
                                case 3:
                                    choosedX = motherX++; choosedY = fatherY--;
                                    break;
                                case 4:
                                    choosedX = fatherX--; choosedY = fatherY++;
                                    break;
                                case 5:
                                    choosedX = motherX--; choosedY = fatherY++;
                                    break;
                                case 6:
                                    choosedX = motherX--; choosedY = fatherY--;
                                    break;
                                case 7:
                                    choosedX = fatherX--; choosedY = motherY--;
                                    break;
                                case 8:
                                    choosedX = fatherX--; choosedY = motherY++;
                                    break;
                                case 9:
                                    choosedX = motherX++; choosedY = fatherY++;
                                    break;
                                default:
                                    choosedX = motherX--; choosedY = motherY--;
                                    break;
                            }
                            toAdd.add(new PersonCell(rand.nextBoolean(), 0, new PointCoordinates(choosedX, choosedY)));
                        }
                    }
                }
                for(PersonCell person : toRemove)
                {
                    peopleList.remove(person);
                }
                for(PersonCell person : toAdd)
                {
                    peopleList.add(person);
                }
                int i = 0;
                for(PersonCell person : peopleList)
                {
                    if(i == 0)
                        person.setNewGeneration(true);
                    content.set(Collections.singleton(person), null);
                    i++;
                }
            }
        }
    }
    
    private LinkedList<PersonCell> findNeighbours(PersonCell person)
    {
        LinkedList<PersonCell> neighbours = new LinkedList<PersonCell>();
        LinkedList<PersonCell> temp = new LinkedList<PersonCell>();
        int x = person.getLivingPlace().getX();
        int y = person.getLivingPlace().getY();
        if(x == 0 && y == 0)
        {
            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(getPersonByCoordinates(x + 1, y) != null)
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(x == 0 && y == 299)
        {
            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(x == 299 && y == 0)
        {
            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(x == 299 && y == 299)
        {
            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(x == 0)
        {
            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(x == 299)
        {
            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(y == 0)
        {
            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else if(y == 299)
        {
            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        else
        {
            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
                neighbours = addPeopleToNeighbour(temp, neighbours);
        }
        
        return neighbours;
    }
    
    private LinkedList<PersonCell> getPersonByCoordinates(int x, int y)
    {
        LinkedList<PersonCell> peopleOnPlace = new LinkedList<PersonCell>();
        for(PersonCell person : peopleList)
        {
            if(person.getLivingPlace().getX() == x && person.getLivingPlace().getY() == y)
                peopleOnPlace.add(person);
        }
        
        return peopleOnPlace;
    }
    
    private LinkedList<PersonCell> addPeopleToNeighbour(LinkedList<PersonCell> people, LinkedList<PersonCell> neighbours)
    {
        for(PersonCell person : people)
            neighbours.add(person);
        
        return neighbours;
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
        stopSimulationButton = new javax.swing.JButton();

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        add(startSimulationButton, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(stopSimulationButton, org.openide.util.NbBundle.getMessage(CAParametersTopComponent.class, "CAParametersTopComponent.stopSimulationButton.text")); // NOI18N
        stopSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopSimulationButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(stopSimulationButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void startSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSimulationButtonActionPerformed
        
        simulationThread = new Thread(new Runnable() {

            @Override
            public void run() {
                initSimulation();
            }
        });
        simulationThread.start();
        startSimulationButton.setEnabled(false);
        stopSimulationButton.setEnabled(true);
    }//GEN-LAST:event_startSimulationButtonActionPerformed

    private void stopSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopSimulationButtonActionPerformed
        simulationThread.interrupt();
        peopleList.clear();
        startSimulationButton.setEnabled(true);
        stopSimulationButton.setEnabled(false);
    }//GEN-LAST:event_stopSimulationButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
