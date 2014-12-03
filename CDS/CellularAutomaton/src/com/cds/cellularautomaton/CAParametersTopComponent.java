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
import java.util.Collections;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
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
    private MapsParametersContainer mapsParametersContainer;
    private InstanceContent content;
    private Lookup lookup;
    private int imageWidth;
    
    public CAParametersTopComponent() {
        initComponents();
        setName(Bundle.CTL_CAParametersTopComponent());
        setToolTipText(Bundle.HINT_CAParametersTopComponent());
        mapsParametersContainer = MapsParametersContainer.getInstance();
        loadMapParameters();
        content=new InstanceContent();
        lookup=new AbstractLookup(content);
        
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
        SecureRandom rand = new SecureRandom();
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
                
            person = new Person(rand.nextBoolean(), rand.nextInt(40));
            mapsParametersContainer.get(x, y).getPeopleList().add(person);
        }
        
        content.set(Collections.singleton(new LookupObject()), null);
//        startSimulation();
    }
    
//    private void startSimulation()
//    {
//        SecureRandom rand = new SecureRandom();
//        while(true)
//        {
//            LinkedList<Person> toRemove = new LinkedList<Person>();
//            LinkedList<Person> toAdd = new LinkedList<Person>();
//            synchronized(peopleList)
//            {
////                try 
////                {
////                    Thread.sleep(1000);
////                } catch (InterruptedException ex) {
////                    Exceptions.printStackTrace(ex);
////                }
//                for(Person person : peopleList)
//                {
//                    person.setAge(person.getAge() + 1);
//                    if(person.getAge() > 105)
//                    {
//                        toRemove.add(person);
//                        continue;
//                    }        
//                    LinkedList<Person> neighbours = findNeighbours(person);
//                    if(neighbours.isEmpty())
//                    {
//                        toRemove.add(person);
//                        continue;
//                    }
//
//                    for(Person neighbour : neighbours)
//                    {
//                        if(neighbour.getSex() != person.getSex())
//                        {
//                            int fatherX = person.getLivingPlace().getX();
//                            int fatherY = person.getLivingPlace().getY();
//                            int motherX = neighbour.getLivingPlace().getX();
//                            int motherY = neighbour.getLivingPlace().getY();
//                            int choosedX, choosedY;
//                            switch(rand.nextInt(11))
//                            {
//                                case 0:
//                                    choosedX = fatherX++; choosedY = fatherY++;
//                                    break;
//                                case 1:
//                                    choosedX = fatherX++; choosedY = motherY--;
//                                    break;
//                                case 2:
//                                    choosedX = motherX--; choosedY = motherY++;
//                                    break;
//                                case 3:
//                                    choosedX = motherX++; choosedY = fatherY--;
//                                    break;
//                                case 4:
//                                    choosedX = fatherX--; choosedY = fatherY++;
//                                    break;
//                                case 5:
//                                    choosedX = motherX--; choosedY = fatherY++;
//                                    break;
//                                case 6:
//                                    choosedX = motherX--; choosedY = fatherY--;
//                                    break;
//                                case 7:
//                                    choosedX = fatherX--; choosedY = motherY--;
//                                    break;
//                                case 8:
//                                    choosedX = fatherX--; choosedY = motherY++;
//                                    break;
//                                case 9:
//                                    choosedX = motherX++; choosedY = fatherY++;
//                                    break;
//                                default:
//                                    choosedX = motherX--; choosedY = motherY--;
//                                    break;
//                            }
//                            toAdd.add(new Person(rand.nextBoolean(), 0, new PointCoordinates(choosedX, choosedY)));
//                        }
//                    }
//                }
//                for(Person person : toRemove)
//                {
//                    peopleList.remove(person);
//                }
//                for(Person person : toAdd)
//                {
//                    peopleList.add(person);
//                }
//                int i = 0;
//                for(Person person : peopleList)
//                {
//                    if(i == 0)
//                        person.setNewGeneration(true);
//                    content.set(Collections.singleton(person), null);
//                    i++;
//                }
//            }
//        }
//    }
//    
//    private LinkedList<Person> findNeighbours(Person person)
//    {
//        LinkedList<Person> neighbours = new LinkedList<Person>();
//        LinkedList<Person> temp = new LinkedList<Person>();
//        int x = person.getLivingPlace().getX();
//        int y = person.getLivingPlace().getY();
//        if(x == 0 && y == 0)
//        {
//            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(getPersonByCoordinates(x + 1, y) != null)
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(x == 0 && y == 299)
//        {
//            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(x == 299 && y == 0)
//        {
//            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(x == 299 && y == 299)
//        {
//            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(x == 0)
//        {
//            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(x == 299)
//        {
//            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(y == 0)
//        {
//            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else if(y == 299)
//        {
//            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        else
//        {
//            if(!(temp = getPersonByCoordinates(x - 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x - 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y - 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//            if(!(temp = getPersonByCoordinates(x + 1, y + 1)).isEmpty())
//                neighbours = addPeopleToNeighbour(temp, neighbours);
//        }
//        
//        return neighbours;
//    }
    
//    private LinkedList<Person> getPersonByCoordinates(int x, int y)
//    {
//        LinkedList<Person> peopleOnPlace = new LinkedList<Person>();
//        for(Person person : peopleList)
//        {
//            if(person.getLivingPlace().getX() == x && person.getLivingPlace().getY() == y)
//                peopleOnPlace.add(person);
//        }
//        
//        return peopleOnPlace;
//    }
    
//    private LinkedList<Person> addPeopleToNeighbour(LinkedList<Person> people, LinkedList<Person> neighbours)
//    {
//        for(Person person : people)
//            neighbours.add(person);
//        
//        return neighbours;
//    }

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

        initializePopulation();
//        simulationThread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                initSimulation();
//            }
//        });
//        simulationThread.start();
//        startSimulationButton.setEnabled(false);
//        stopSimulationButton.setEnabled(true);
    }//GEN-LAST:event_startSimulationButtonActionPerformed

    private void stopSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopSimulationButtonActionPerformed

        mapsParametersContainer.clearPopulation();
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
