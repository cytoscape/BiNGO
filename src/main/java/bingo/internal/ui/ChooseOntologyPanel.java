package bingo.internal.ui;

/* * Copyright (c) 2005 Flanders Interuniversitary Institute for Biotechnology (VIB)
 * *
 * * Authors : Steven Maere
 * *
 * * This program is free software; you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation; either version 2 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * * The software and documentation provided hereunder is on an "as is" basis,
 * * and the Flanders Interuniversitary Institute for Biotechnology
 * * has no obligations to provide maintenance, support,
 * * updates, enhancements or modifications.  In no event shall the
 * * Flanders Interuniversitary Institute for Biotechnology
 * * be liable to any party for direct, indirect, special,
 * * incidental or consequential damages, including lost profits, arising
 * * out of the use of this software and its documentation, even if
 * * the Flanders Interuniversitary Institute for Biotechnology
 * * has been advised of the possibility of such damage. See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program; if not, write to the Free Software
 * * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * *
 * * Authors: Steven Maere
 * * Date: Apr.20.2005
 * * Class that extends JPanel and implements ActionListener. Makes
 * * a panel with a drop-down box of ontology choices. Last option custom...
 * * opens FileChooser
 **/


import javax.swing.*;

import bingo.internal.BingoAlgorithm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;

/**
 * ***************************************************************
 * ChooseOntologyPanel.java:   Steven Maere (c) April 2005
 * -----------------------
 * <p/>
 * Class that extends JPanel and implements ActionListener. Makes
 * a panel with a drop-down box of ontology choices. Last option custom...
 * opens FileChooser
 * ******************************************************************
 */

public class ChooseOntologyPanel extends JPanel implements ActionListener, ItemListener {


    /*--------------------------------------------------------------
     Fields.
    --------------------------------------------------------------*/
    private final String CUSTOM = BingoAlgorithm.CUSTOM;
    private final String NONE = BingoAlgorithm.NONE;

    /**
     * JComboBox with the possible choices.
     */
    private JComboBox<String> choiceBox;
    /**
     * parent window
     */
    private SettingsPanel settingsPanel;
    /**
     * boolean to assess default (<code>true</code>) or custom
     * input (<code>false</code>)
     */
    private boolean defaultItem;
    /**
     * the selected (kind of) ontology
     */
    private String specifiedOntology;

    private String previousSelectedItem;


    /*-----------------------------------------------------------------
     CONSTRUCTOR.
    -----------------------------------------------------------------*/

    /**
     * Constructor
     *
     * @param settingsPanel : parent window
     */
    public ChooseOntologyPanel(SettingsPanel settingsPanel, String[] choiceArray, String choice_def) {
        super();
        this.settingsPanel = settingsPanel;

        setOpaque(false);

        choiceBox = new JComboBox<>(choiceArray);
        choiceBox.addActionListener(this);
        choiceBox.addItemListener(this);

        // Layout with GridLayout
        setLayout(new GridLayout(1, 0));
        add(choiceBox);

        // select default combo box item
        if (Arrays.asList(choiceArray).contains(choice_def)) {
            choiceBox.setSelectedItem(choice_def);
            this.settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
            specifiedOntology = (String) choiceBox.getSelectedItem();
            defaultItem = true;
        } else {
            choiceBox.removeActionListener(this);
            choiceBox.setEditable(true);
            choiceBox.setSelectedItem(choice_def);
            choiceBox.setEditable(false);
            specifiedOntology = CUSTOM;
            defaultItem = false;
            if (choice_def.toLowerCase().endsWith(".obo")) {
                this.settingsPanel.getNamespacePanel().choiceBox.setEnabled(true);
            } else {
                this.settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
            }
            choiceBox.addActionListener(this);
        }
    }

    /*----------------------------------------------------------------
    METHODS.
    ----------------------------------------------------------------*/

    /**
     * Method that returns the selected item.
     *
     * @return String selection.
     */
    public String getSelection() {
        return (String) choiceBox.getSelectedItem();
    }
    
    public String getSpecifiedOntology() {
        return specifiedOntology;
    }

    /**
     * Method that returns <code>true</code> if one of teh default choices was
     * chosen, or <code>false</code> if a custom ontology was chosen
     */
    public boolean isDefault() {
        return defaultItem;
    }

    /*----------------------------------------------------------------
    LISTENER-PART.
    ----------------------------------------------------------------*/

    /**
     * Method performed when combo box item was selected.
     *
     * @param event event that triggers action
     */
    public void actionPerformed(ActionEvent event) {

        if (CUSTOM.equals(choiceBox.getSelectedItem())) {
//            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
//            int returnVal = chooser.showOpenDialog(settingsPanel);
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                specifiedOntology = CUSTOM;
//                choiceBox.setEditable(true);
//                choiceBox.setSelectedItem(chooser.getSelectedFile().toString());
//                choiceBox.setEditable(false);
//                defaultItem = false;
//                if (((String) choiceBox.getSelectedItem()).endsWith(".obo")) {
//                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(true);
//                } else {
//                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
//                }
//            }
//            if (returnVal == JFileChooser.CANCEL_OPTION) {
//                choiceBox.setSelectedItem(NONE);
//                specifiedOntology = NONE;
//                defaultItem = true;
//                settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
//            }

            Frame parentFrame = (Frame) settingsPanel.getTopLevelAncestor();
            String directoryPath;
            if (CUSTOM.equals(specifiedOntology)) {
                directoryPath = new File(previousSelectedItem).getParent();
                System.out.println("directoryPath = " + directoryPath);
            } else {
                directoryPath = System.getProperty("user.home");
            }
            FileChooserOS fileChooser = new FileChooserOS(parentFrame, directoryPath);
            FileChooserOS.ReturnState returnState = fileChooser.showDialog();
            if (returnState == FileChooserOS.ReturnState.APPROVE) {
                specifiedOntology = CUSTOM;
                choiceBox.setEditable(true);
                choiceBox.setSelectedItem(fileChooser.getSelectedFilePath());
                choiceBox.setEditable(false);
                defaultItem = false;
                if (((String) choiceBox.getSelectedItem()).toLowerCase().endsWith(".obo")) {
                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(true);
                } else {
                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
                }
            } else if (returnState == FileChooserOS.ReturnState.CANCEL) {
                if (CUSTOM.equals(specifiedOntology)) {
                    choiceBox.setEditable(true);
                    choiceBox.setSelectedItem(previousSelectedItem);
                    choiceBox.setEditable(false);
                    if (((String) choiceBox.getSelectedItem()).toLowerCase().endsWith(".obo")) {
                        settingsPanel.getNamespacePanel().choiceBox.setEnabled(true);
                    } else {
                        settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
                    }
                } else {
                    choiceBox.setSelectedItem(previousSelectedItem);
                    settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
                }
            }
        } else if (NONE.equals(choiceBox.getSelectedItem())) {
            specifiedOntology = NONE;
            defaultItem = true;
            settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
        } else {
            specifiedOntology = (String) choiceBox.getSelectedItem() ;
            defaultItem = true;
            settingsPanel.getNamespacePanel().choiceBox.setEnabled(false);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            previousSelectedItem = e.getItem().toString();
        }
    }
}
