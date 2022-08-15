package cmpt213.assignment4.packagedeliveries.client;

import cmpt213.assignment4.packagedeliveries.client.model.Package;
import cmpt213.assignment4.packagedeliveries.client.view.ApplicationUI;

import javax.swing.*;

/**
 * PackageTrackerApp: Contains the main method for starting the GUI application
 * @Author:
 */
public class PackageTrackerApp {

    public static void main(String[] args) {
        Package p = new Package();
        System.out.println(p.getClass());

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new ApplicationUI();
//            }
//        });

        // testing excepton and other things
       //print(mo);
        String s = new String("Hello");
        print(s);
        print(Integer.valueOf(2));

    }

    static private <U> void print(U u){
        System.out.println(u.getClass().getName());

    }
}
