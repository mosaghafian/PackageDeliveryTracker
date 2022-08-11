package cmpt213.assignment4.packagedeliveries.client.view;


import cmpt213.assignment4.packagedeliveries.client.control.PackageTracker;
import cmpt213.assignment4.packagedeliveries.client.model.Book;
import cmpt213.assignment4.packagedeliveries.client.model.Electronic;
import cmpt213.assignment4.packagedeliveries.client.model.Perishable;
import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A class to represent the GUI for add package by extending JDialog
 * And handling the creation and add package to the listOfPackage
 *
 * @Author Mohammad Saghafian
 */

public class PKGInfo extends JDialog implements ActionListener, ItemListener {

    PackageTracker packageTracker = PackageTracker.sharedInstance();
    private JPanel gui;

    public PKGInfo(){

        setTitle("Add Package");
        setPreferredSize(new Dimension(500, 700));
        // Creating t
        gui = new JPanel();
        gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));

        JPanel typePanel = new JPanel();
        typePanel.setLayout(new FlowLayout());
        JLabel typeLabel = new JLabel("Type: ");
        JComboBox typeComboBox = new JComboBox(new String[]{"Book", "Perishable", "Electronic"});
        typeComboBox.addItemListener(this);

        typeComboBox.setEditable(true);
        typePanel.add(typeLabel);
        typePanel.add(typeComboBox);

        gui.add(typePanel);

        guiPanelHelper();
        JPanel authorPanel = new JPanel();
        authorPanel.setLayout(new FlowLayout());
        JLabel authorLabel = new JLabel("Author: ");
        JTextField authorField = new JTextField(20);
        authorPanel.add(authorLabel);
        authorPanel.add(authorField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton createButton = new JButton("Create");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        createButton.addActionListener(this);
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        gui.add(authorPanel);
        gui.add(buttonPanel);


        setContentPane(gui);
        pack();
        setVisible(true);

    }

    /**
     * Getter method for notes
     * @return notes (String)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Create")){
            String type = (String)((JComboBox) ((JPanel)gui.getComponent(0)).getComponent(1)).getItemAt(0);
            String name = ((JTextField) ((JPanel)gui.getComponent(1)) .getComponent(1)).getText();
            String notes = ((JTextField) ((JPanel)gui.getComponent(2)) .getComponent(1)).getText();
            String price = ((JTextField) ((JPanel)gui.getComponent(3)) .getComponent(1)).getText();
            String weight = ((JTextField) ((JPanel)gui.getComponent(4)) .getComponent(1)).getText();
            LocalDateTime expectedDD = ((DateTimePicker) ((JPanel)gui.getComponent(5)) .getComponent(1)).getDateTimeStrict();
            if((!type.equals("Book") && !type.equals("Electronic")) && !type.equals("Perishable")){
                JOptionPane.showMessageDialog(this, "Please choose a package type: Book, Electronic, Perishable");
                return;
            }
            if(name.length() == 0){
                JOptionPane.showMessageDialog(this, "Please input a name!");
                return;
            }
            if(price.length() == 0){
                JOptionPane.showMessageDialog(this, "Please input a price!");
                return;
            }
            if(weight.length() == 0){
                JOptionPane.showMessageDialog(this, "Please input a weight!");
                return;
            }
            if(expectedDD == null){
                JOptionPane.showMessageDialog(this, "Please input an expected delivery date!");
                return;
            }
            if(type.equals("Book")){
                Book book = new Book();
                book.setPackageType(1);
                book.setName(name);
                book.setNotes(notes);
                book.setPrice(Double.parseDouble(price));
                book.setWeight(Double.parseDouble(weight));
                book.setDate(expectedDD);
                String author = ((JTextField) ((JPanel)gui.getComponent(6)) .getComponent(1)).getText();
                book.setAuthor(author);
                packageTracker.addPackage(book);
            } else if(type.equals("Electronic")){
                Electronic electronic = new Electronic();
                electronic.setPackageType(3);
                electronic.setName(name);
                electronic.setNotes(notes);
                electronic.setPrice(Double.parseDouble(price));
                electronic.setWeight(Double.parseDouble(weight));
                electronic.setDate(expectedDD);
                String ENVFee = ((JTextField) ((JPanel)gui.getComponent(6)) .getComponent(1)).getText();
                if(ENVFee.length() == 0){
                    JOptionPane.showMessageDialog(this, "Please input an Environment Fee for the book!");
                    return;
                }
                electronic.setENVFee(Double.parseDouble(ENVFee));
                packageTracker.addPackage(electronic);
            } else if(type.equals("Perishable")){
                Perishable perishable = new Perishable();
                perishable.setPackageType(2);
                perishable.setName(name);
                perishable.setNotes(notes);
                perishable.setPrice(Double.parseDouble(price));
                perishable.setWeight(Double.parseDouble(weight));
                perishable.setDate(expectedDD);
                LocalDateTime EXPDate = ((DateTimePicker) ((JPanel)gui.getComponent(6)) .getComponent(1)).getDateTimeStrict();
                if(EXPDate == null){
                    JOptionPane.showMessageDialog(this, "Please input an Expiry Date for the book!");
                    return;
                }
                perishable.setEXPDate(EXPDate);
                packageTracker.addPackage(perishable);
            }
            this.dispose();
            JOptionPane.showMessageDialog(this, "A " + type + " has been added");
            return;
        }else{
            this.dispose();
        }
    }

    /**
     * itemStateChanged: handles state changes from the JComboBOx
     * @Param: ItemEvent e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getItem().equals("Perishable")) {

                gui.removeAll();
                // Type
                JPanel typePanel = new JPanel();
                typePanel.setLayout(new FlowLayout());
                JLabel typeLabel = new JLabel("Type: ");
                JComboBox typeComboBox = new JComboBox(new String[]{"Perishable", "Electronic", "Book"});
                typeComboBox.addItemListener(this);

                typeComboBox.setEditable(true);
                typePanel.add(typeLabel);
                typePanel.add(typeComboBox);

                gui.add(typePanel);

                guiPanelHelper();
                JPanel EXPPanel = new JPanel();
                EXPPanel.setLayout(new FlowLayout());
                JLabel EXPLabel = new JLabel("Expiry Data: ");
                DateTimePicker dateTimePicker = new DateTimePicker();
                EXPPanel.add(EXPLabel);
                EXPPanel.add(dateTimePicker);


                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                JButton createButton = new JButton("Create");
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(this);
                createButton.addActionListener(this);
                buttonPanel.add(createButton);
                buttonPanel.add(cancelButton);

                gui.add(EXPPanel);
                gui.add(buttonPanel);
                gui.revalidate();
                gui.repaint();

            } else if (e.getItem().equals("Book")) {

                gui.removeAll();
                JPanel typePanel = new JPanel();
                typePanel.setLayout(new FlowLayout());
                JLabel typeLabel = new JLabel("Type: ");
                JComboBox typeComboBox = new JComboBox(new String[]{"Book", "Perishable", "Electronic"});
                typeComboBox.addItemListener(this);

                typeComboBox.setEditable(true);
                typePanel.add(typeLabel);
                typePanel.add(typeComboBox);

                gui.add(typePanel);
                guiPanelHelper();
                JPanel authorPanel = new JPanel();
                authorPanel.setLayout(new FlowLayout());
                JLabel authorLabel = new JLabel("Author: ");
                JTextField authorField = new JTextField(20);
                authorPanel.add(authorLabel);
                authorPanel.add(authorField);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                JButton createButton = new JButton("Create");
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(this);
                createButton.addActionListener(this);
                buttonPanel.add(createButton);
                buttonPanel.add(cancelButton);

                gui.add(authorPanel);
                gui.add(buttonPanel);
                gui.revalidate();
                gui.repaint();
            } else if (e.getItem().equals("Electronic")) {

                gui.removeAll();
                JPanel typePanel = new JPanel();
                typePanel.setLayout(new FlowLayout());
                JLabel typeLabel = new JLabel("Type: ");
                JComboBox typeComboBox = new JComboBox(new String[]{"Electronic", "Book", "Perishable"});
                typeComboBox.addItemListener(this);

                typeComboBox.setEditable(true);
                typePanel.add(typeLabel);
                typePanel.add(typeComboBox);

                gui.add(typePanel);

                guiPanelHelper();
                JPanel ENVPanel = new JPanel();
                ENVPanel.setLayout(new FlowLayout());
                JLabel ENVLabel = new JLabel("Environment Fee: ");
                JTextField ENVField = new JTextField(20);
                ENVPanel.add(ENVLabel);
                ENVPanel.add(ENVField);


                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());
                JButton createButton = new JButton("Create");
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(this);
                createButton.addActionListener(this);
                buttonPanel.add(createButton);
                buttonPanel.add(cancelButton);

                gui.add(ENVPanel);
                gui.add(buttonPanel);
                gui.revalidate();
                gui.repaint();
            }
        }
    }

    /**
     * guiPanelHelper: A helper function to add JPanel
     */
    private void guiPanelHelper(){
        // Name
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Package Name:");
        JTextField nameField = new JTextField(20);

        namePanel.add(nameLabel);
        namePanel.add(nameField);

        // Note
        JPanel notePanel = new JPanel();
        notePanel.setLayout(new FlowLayout());
        JLabel noteLabel = new JLabel("Notes: ");
        JTextField noteField = new JTextField(20);
        notePanel.add(noteLabel);
        notePanel.add(noteField);

        // Price
        JPanel pricePanel = new JPanel();
        notePanel.setLayout(new FlowLayout());
        JLabel priceLabel = new JLabel("Price: ");
        JTextField priceField = new JTextField(20);
        pricePanel.add(priceLabel);
        pricePanel.add(priceField);

        // Weight
        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new FlowLayout());
        JLabel weightLabel = new JLabel("Weight: ");
        JTextField weighField = new JTextField(20);
        weightPanel.add(weightLabel);
        weightPanel.add(weighField);

        // Expected delivery data
        JPanel deliveryPanel = new JPanel();
        deliveryPanel.setLayout(new FlowLayout());
        JLabel deliveryLabel = new JLabel("Expected Delivery Data: ");
        DateTimePicker dateTimePicker = new DateTimePicker();
        deliveryPanel.add(deliveryLabel);
        deliveryPanel.add(dateTimePicker);
        gui.add(namePanel);
        gui.add(notePanel);
        gui.add(pricePanel);
        gui.add(weightPanel);
        gui.add(deliveryPanel);

        gui.revalidate();
        gui.repaint();
    }

}

