package cmpt213.assignment4.packagedeliveries.client.view;

import cmpt213.assignment4.packagedeliveries.client.control.PackageTracker;
import cmpt213.assignment4.packagedeliveries.client.model.Book;
import cmpt213.assignment4.packagedeliveries.client.model.Electronic;
import cmpt213.assignment4.packagedeliveries.client.model.Package;
import cmpt213.assignment4.packagedeliveries.client.model.Perishable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * A class to render the GUI for the package delivery tracker application
 *
 * @Author Mohammad Saghafian
 */

public class ApplicationUI implements ActionListener, WindowListener {

    private PackageTracker packageTracker = PackageTracker.sharedInstance();
    private JFrame applicationFrame;
    private JButton allPackages;
    private JButton overDuePackages;
    private JButton upcomingPackages;

    private JButton addPackage;


    private JPanel verticalPanel;

    private JPanel middlePanel;

    JScrollPane listScrollPane;


    private JLabel label;
    private JLabel NoItem = new JLabel();

    /**
     * ApplicationUI(): Constructor
     *
     */
    public ApplicationUI() {
        // Initializing and customizeing the JFrame needed


        try {
            packageTracker.getRequestAllPackages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        verticalPanel = new JPanel();

        applicationFrame = new JFrame("Package Delivery Tracker");
        applicationFrame.setSize(600, 800);
        applicationFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        applicationFrame.addWindowListener(this);

        // Initializing the JButton needed
        allPackages = new JButton("All");
        overDuePackages = new JButton("Overdue");
        upcomingPackages = new JButton("Upcoming");
        addPackage = new JButton("Add Package");

        middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));


        // Adding the action listener
        allPackages.addActionListener(this);
        overDuePackages.addActionListener(this);
        upcomingPackages.addActionListener(this);
        addPackage.addActionListener(this);

        GUIhelper("All Package");
        JPanel noItemPanel = new JPanel();
        noItemPanel.setLayout(new FlowLayout());
        noItemPanel.add(NoItem);
        listScrollPane = new JScrollPane(middlePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        verticalPanel.add(noItemPanel);
        verticalPanel.add(listScrollPane);
        getAllPackages();

        JPanel lowerButtonPanel = new JPanel();
        lowerButtonPanel.setLayout(new FlowLayout());

        lowerButtonPanel.add(addPackage);
        verticalPanel.add(lowerButtonPanel);
        applicationFrame.add(verticalPanel);

        applicationFrame.setVisible(true);
    }

    /**
     * actionPerformed: Handles ActionEvents
     * @Param: ActionEvent e
     * @return: notes (String)
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Handling the cancel button from the add package panel
        if (e.getActionCommand().equals("All")) {
            label.setText("All Package");
            getAllPackages();


        } else if (e.getActionCommand().equals("Overdue")) {
            label.setText("Overdue Package");
            getOverduePackages();

        } else if (e.getActionCommand().equals("Upcoming")) {
            label.setText("Upcoming Package");
            getUpcomingPackages();


        } else if (e.getActionCommand().equals("Add Package")) {
            PKGInfo pkgInfo = new PKGInfo();
            pkgInfo.setVisible(true);
            pkgInfo.addWindowListener(this);

        }


    }
    /**
     * GUIhelper: A helper function to draw GUI for main applicationx
     * @Param: String view
     */
    private void GUIhelper(String view) {
        //Upper panel for botton
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setLayout(new FlowLayout());
        ButtonPanel.add(allPackages);
        ButtonPanel.add(overDuePackages);
        ButtonPanel.add(upcomingPackages);

        label = new JLabel(view);
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());
        labelPanel.add(label);
        // LowerButtonPanel


        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.add(ButtonPanel);
        verticalPanel.add(labelPanel);


    }
    /**
     * getUpcomingPackages: A helper function for GUI of upcoming
     */
    private void getUpcomingPackages(){
        middlePanel.removeAll();
        middlePanel.revalidate();
        middlePanel.repaint();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        List<Package> listOfUpcomingPackages = packageTracker.getUpcomingPackages();
        if (listOfUpcomingPackages.size() != 0) {
            NoItem.setText("");
            for (int i = 0; i < listOfUpcomingPackages.size(); i++) {
                Package tmp = listOfUpcomingPackages.get(i);
                JPanel packagePanel = new JPanel();
                packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.Y_AXIS));

                JLabel number = new JLabel(Integer.toString(i + 1));
                JLabel type;

                JPanel lastPanel = new JPanel();
                lastPanel.setLayout(new FlowLayout());

                if (tmp.getPackageType() == 1) {
                    type = new JLabel("Book");
                    lastPanel.add(new JLabel("Author:"));
                    lastPanel.add(new JLabel(((Book) tmp).getAuthor()));
                } else if (tmp.getPackageType() == 2) {
                    type = new JLabel("Perishable");
                    lastPanel.add(new JLabel("Expiry Data"));
                    lastPanel.add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(((Perishable) tmp).getEXPDate())));
                } else {
                    type = new JLabel("Electronic");
                    lastPanel.add(new JLabel("Environment Fee"));
                    lastPanel.add(new JLabel(String.valueOf(((Electronic) tmp).getENVFee())));
                }
                JPanel firstRow = new JPanel();
                firstRow.setLayout(new FlowLayout());
                firstRow.add(new JLabel("Package"));
                firstRow.add(number);
                firstRow.add(type);

                JPanel secondRow = new JPanel();
                secondRow.setLayout(new FlowLayout());
                secondRow.add(new JLabel("Name: "));
                secondRow.add(new JLabel(tmp.getName()));

                JPanel thirdRow = new JPanel();
                thirdRow.setLayout(new FlowLayout());
                thirdRow.add(new JLabel("Notes: "));
                thirdRow.add(new JLabel(tmp.getNotes()));

                JPanel fourthRow = new JPanel();
                fourthRow.setLayout(new FlowLayout());
                fourthRow.add(new JLabel("Price: "));
                fourthRow.add(new JLabel(Double.toString(tmp.getPrice())));

                JPanel fifthRow = new JPanel();
                fifthRow.setLayout(new FlowLayout());
                fifthRow.add(new JLabel("Weight: "));
                fifthRow.add(new JLabel(Double.toString(tmp.getWeight())));

                JPanel sixthRow = new JPanel();
                sixthRow.setLayout(new FlowLayout());
                sixthRow.add(new JLabel("Expected Delivery Date: "));
                sixthRow.add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(tmp.getDate())));



                middlePanel.add(new JSeparator());
                packagePanel.add(firstRow);
                packagePanel.add(secondRow);
                packagePanel.add(thirdRow);
                packagePanel.add(fourthRow);
                packagePanel.add(fifthRow);
                packagePanel.add(sixthRow);

                packagePanel.add(lastPanel);
                middlePanel.add(packagePanel);
                middlePanel.add(new JSeparator());

            }
            listScrollPane = new JScrollPane(middlePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            verticalPanel.remove(3);
            verticalPanel.add(listScrollPane,3);

        } else {
            NoItem.setText("No Items to show");

        }
        // middlePanel.revalidate();
        // middlePanel.repaint();
    }
    /**
     * getOverduePackages: A helper function to draw the GUI of Overdue Packages
     */
    private void getOverduePackages() {
        middlePanel.removeAll();
        middlePanel.revalidate();
        middlePanel.repaint();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        List<Package> listOfOverduePackages = packageTracker.getOverduePackages();
        if (listOfOverduePackages.size() != 0) {
            NoItem.setText("");
            for (int i = 0; i < listOfOverduePackages.size(); i++) {
                Package tmp = listOfOverduePackages.get(i);
                JPanel packagePanel = new JPanel();
                packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.Y_AXIS));

                JLabel number = new JLabel(Integer.toString(i + 1));
                JLabel type;
                JPanel lastPanel = new JPanel();
                lastPanel.setLayout(new FlowLayout());

                if (tmp.getPackageType() == 1) {
                    type = new JLabel("Book");
                    lastPanel.add(new JLabel("Author"));
                    lastPanel.add(new JLabel(((Book) tmp).getAuthor()));
                } else if (tmp.getPackageType() == 2) {
                    type = new JLabel("Perishable");
                    lastPanel.add(new JLabel("Expiry Data"));
                    lastPanel.add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(((Perishable) tmp).getEXPDate())));
                } else {
                    type = new JLabel("Electronic");
                    lastPanel.add(new JLabel("Environment Fee: "));
                    lastPanel.add(new JLabel(String.valueOf(((Electronic) tmp).getENVFee())));
                }
                JPanel firstRow = new JPanel();
                firstRow.setLayout(new FlowLayout());
                firstRow.add(new JLabel("Package"));
                firstRow.add(number);
                firstRow.add(type);

                JPanel secondRow = new JPanel();
                secondRow.setLayout(new FlowLayout());
                secondRow.add(new JLabel("Name: "));
                secondRow.add(new JLabel(tmp.getName()));

                JPanel thirdRow = new JPanel();
                thirdRow.setLayout(new FlowLayout());
                thirdRow.add(new JLabel("Notes: "));
                thirdRow.add(new JLabel(tmp.getNotes()));

                JPanel fourthRow = new JPanel();
                fourthRow.setLayout(new FlowLayout());
                fourthRow.add(new JLabel("Price: "));
                fourthRow.add(new JLabel(Double.toString(tmp.getPrice())));

                JPanel fifthRow = new JPanel();
                fifthRow.setLayout(new FlowLayout());
                fifthRow.add(new JLabel("Weight"));
                fifthRow.add(new JLabel(Double.toString(tmp.getWeight())));

                JPanel sixthRow = new JPanel();
                sixthRow.setLayout(new FlowLayout());
                sixthRow.add(new JLabel("Expected Delivery Date:"));
                sixthRow.add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(tmp.getDate())));



                middlePanel.add(new JSeparator());
                packagePanel.add(firstRow);
                packagePanel.add(secondRow);
                packagePanel.add(thirdRow);
                packagePanel.add(fourthRow);
                packagePanel.add(fifthRow);
                packagePanel.add(sixthRow);

                packagePanel.add(lastPanel);
                middlePanel.add(packagePanel);
                middlePanel.add(new JSeparator());

            }
            listScrollPane = new JScrollPane(middlePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            verticalPanel.remove(3);
            verticalPanel.add(listScrollPane,3);

        } else {
            NoItem.setText("No Items to show");

        }
        // middlePanel.revalidate();
        // middlePanel.repaint();
    }

    /**
     * getAllPackages: A helper function to draw the gui for all packages screen
     */
    private void getAllPackages() {
        middlePanel.removeAll();
        middlePanel.revalidate();
        middlePanel.repaint();

        if (packageTracker.getNumOfPackage() != 0) {
            NoItem.setText("");
            for (int i = 0; i < packageTracker.getListOfPackages().size(); i++) {
                Package tmp = packageTracker.getListOfPackages().get(i);

                JPanel packagePanel = new JPanel();
                packagePanel.setLayout(new BoxLayout(packagePanel, BoxLayout.Y_AXIS));

                JLabel number = new JLabel(Integer.toString(i + 1));
                JLabel type;

                JPanel lastPanel = new JPanel();
                lastPanel.setLayout(new FlowLayout());

                if (tmp.getPackageType() == 1) {
                    type = new JLabel("Book");
                    lastPanel.add(new JLabel("Author"));
                    lastPanel.add(new JLabel(((Book) tmp).getAuthor()));
                } else if (tmp.getPackageType() == 2) {
                    type = new JLabel("Perishable");
                    lastPanel.add(new JLabel("Expiry Data"));
                    lastPanel.add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(((Perishable) tmp).getEXPDate())));
                } else {
                    type = new JLabel("Electronic");
                    lastPanel.add(new JLabel("Environment Fee"));
                    lastPanel.add(new JLabel(String.valueOf(((Electronic) tmp).getENVFee())));
                }
                JPanel firstRow = new JPanel();
                firstRow.setLayout(new FlowLayout());
                firstRow.add(new JLabel("Package"));
                firstRow.add(number);
                firstRow.add(type);

                JPanel secondRow = new JPanel();
                secondRow.setLayout(new FlowLayout());
                secondRow.add(new JLabel("Name: "));
                secondRow.add(new JLabel(tmp.getName()));

                JPanel thirdRow = new JPanel();
                thirdRow.setLayout(new FlowLayout());
                thirdRow.add(new JLabel("Notes: "));
                thirdRow.add(new JLabel(tmp.getNotes()));

                JPanel fourthRow = new JPanel();
                fourthRow.setLayout(new FlowLayout());
                fourthRow.add(new JLabel("Price: "));
                fourthRow.add(new JLabel(Double.toString(tmp.getPrice())));

                JPanel fifthRow = new JPanel();
                fifthRow.setLayout(new FlowLayout());
                fifthRow.add(new JLabel("Weight"));
                fifthRow.add(new JLabel(Double.toString(tmp.getWeight())));

                JPanel sixthRow = new JPanel();
                sixthRow.setLayout(new FlowLayout());
                sixthRow.add(new JLabel("Expected Delivery Date:"));
                sixthRow.add(new JLabel(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(tmp.getDate())));

                JPanel seventhRow = new JPanel();
                seventhRow.setLayout(new FlowLayout());
                JButton removeButton = new JButton("Remove");
                JCheckBox checkBox = new JCheckBox("Delivered?");

                if (tmp.getDelivered()) {
                    checkBox.setSelected(true);
                }
                int finalI = i;
                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        packageTracker.removePackage(tmp.getId());
                        getAllPackages();
                        applicationFrame.revalidate();
                        applicationFrame.repaint();
                    }
                });
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       packageTracker.markPackageAsDelivered(tmp.getId());
                        getAllPackages();
                        applicationFrame.revalidate();
                        applicationFrame.repaint();
                    }
                });
                seventhRow.add(checkBox);
                seventhRow.add(removeButton);

                middlePanel.add(new JSeparator());
                packagePanel.add(firstRow);
                packagePanel.add(secondRow);
                packagePanel.add(thirdRow);
                packagePanel.add(fourthRow);
                packagePanel.add(fifthRow);
                packagePanel.add(sixthRow);
                packagePanel.add(seventhRow);
                packagePanel.add(lastPanel);
                middlePanel.add(packagePanel);
                middlePanel.add(new JSeparator());

            }

            listScrollPane = new JScrollPane(middlePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            verticalPanel.remove(3);
            verticalPanel.add(listScrollPane,3);

        } else {
            NoItem.setText("No Items to show");

        }

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }
    /**
     * windowClosing: Function handler for the application being closed programmatically
     */
    @Override
    public void windowClosing(WindowEvent e) {

        if (e.getWindow().equals(applicationFrame)) {
            packageTracker.exit();
            applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
    /**
     * windowClosed: Function handler for the JDialog being closed programmatically
     */
    @Override
    public void windowClosed(WindowEvent e) {

        middlePanel.removeAll();
        middlePanel.revalidate();
        middlePanel.repaint();
        label.setText("All Package");

        if (packageTracker.getNumOfPackage() == 0) {
            label.setText("All Packages");
            getAllPackages();
        } else {
            label.setText("All Packages");
            getAllPackages();

        }
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}