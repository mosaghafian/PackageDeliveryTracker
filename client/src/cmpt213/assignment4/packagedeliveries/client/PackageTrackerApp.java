package cmpt213.assignment4.packagedeliveries.client;

import cmpt213.assignment4.packagedeliveries.client.view.ApplicationUI;

import javax.swing.*;

/**
 * PackageTrackerApp: Contains the main method for starting the GUI application
 * @Author:
 */
public class PackageTrackerApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ApplicationUI();
            }
        });


    }
}
