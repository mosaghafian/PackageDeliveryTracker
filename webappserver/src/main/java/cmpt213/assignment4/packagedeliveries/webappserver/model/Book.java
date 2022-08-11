package cmpt213.assignment4.packagedeliveries.webappserver.model;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * A class to model the Book and its attributes
 *
 * @Author Mohammad Saghafian
 */
public class Book extends Package {

    private String author;
    public Book(){}
    public Book(String author){
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }

    public void setAuthor(String author){
        this.author = author;
    }
    /**
     * returns the info about the book in a String
     * @return (String)
     */
    @Override
    public String toString(){
        return  "Package Type: Book" +
                "\nPackage: " + getName() +
                "\nNotes: " + getNotes() +
                "\nPrice: $" + getPrice() +
                "\nWeight: " + getWeight() + "kg" +
                "\nExpected Delivery Data: " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(getDate()) +
                "\nDelivered? " + ((getDelivered()) ? "Yes" : "No") +
                "\nAuthor: " + author;
    }
}
