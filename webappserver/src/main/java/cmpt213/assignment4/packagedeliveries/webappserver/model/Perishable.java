package cmpt213.assignment4.packagedeliveries.webappserver.model;

/**
 * A class to model the Perishable and its attributes
 *
 * @Author Mohammad Saghafian
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Perishable extends Package {

    LocalDateTime expdate;

    public Perishable(){}

    public Perishable(LocalDateTime expDate){
        this.expdate = expdate;
    }

    public void setEXPDate(LocalDateTime EXPDate) {
        this.expdate = EXPDate;
    }

    public LocalDateTime getEXPDate() {
        return expdate;
    }

    /**
     * returns the info about the perishable in a String
     * @return (String)
     */
    @Override
    public String toString(){
        return  "Package Type: Perishable" +
                "\nPackage: " + getName() +
                "\nNotes: " + getNotes() +
                "\nPrice: $" + getPrice() +
                "\nWeight: " + getWeight() + "kg" +
                "\nExpected Delivery Data: " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(getDate()) +
                "\nDelivered? " + ((getDelivered()) ? "Yes" : "No") +
                "\nExpiry Date: " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(getEXPDate());
    }
}
