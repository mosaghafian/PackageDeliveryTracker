package cmpt213.assignment4.packagedeliveries.webappserver.model;


import java.time.format.DateTimeFormatter;

/**
 * A class to model the Electronic and its attributes
 *
 * @Author Mohammad Saghafian
 */

public class Electronic extends Package {

    private double envfee;
    public Electronic(){}

    public Electronic(double envFee){
        this.envfee = envFee;
    }

    public double getENVFee() {
        return envfee;
    }

    public void setENVFee(double ENVFee) {
        this.envfee = ENVFee;
    }

    /**
     * returns the info about the electronic in a String
     * @return (String)
     */
    @Override
    public String toString(){
        return  "Package Type: Electronic" +
                "\nPackage: " + getName() +
                "\nNotes: " + getNotes() +
                "\nPrice: $" + getPrice() +
                "\nWeight: " + getWeight() + "kg" +
                "\nExpected Delivery Data: " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(getDate()) +
                "\nDelivered? " + ((getDelivered()) ? "Yes" : "No") +
                "\nEnvironment Handling Fee: " + envfee;
    }
}
