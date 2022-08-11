package cmpt213.assignment4.packagedeliveries.client.model;


/**
 * A class to model the package and its attributes
 *
 * @Author Mohammad Saghafian
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public  class Package implements Comparable<Package> {
    private String name;
    private String notes;
    private double price;
    private double weight;

    private int packageType;

    private Boolean delivered;
    private LocalDateTime date;

    private UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    // Generic type for storing extra information of a package.

    // extraInfo object to be used to store additional information about a package -> Book has
    // an author, etc.

    /**
     * No args Constructor
     */
    public Package(){}

    /**
     * All args constructor
     * @param name
     * @param notes
     * @param price
     * @param weight
     * @param delivered
     * @param date
     */
    public Package(String name, String notes, double price, double weight, Boolean delivered, LocalDateTime date){
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.weight = weight;
        this.delivered = delivered;
        this.date = date;
    }

    public int getPackageType(){
        return packageType;
    };
    public void setPackageType(int packageType){
        this.packageType = packageType;
    };
    /**
     * Getter method for name
     * @return name (String)
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for notes
     * @return notes (String)
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Getter method for price
     * @return price (String)
     */
    public double getPrice() {
        return price;
    }

    /**
     * Getter method for weight
     * @return weight (weight)
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Getter method for delivered
     * @return delivered (Boolean)
     */
    public Boolean getDelivered() {
        return delivered;
    }

    /**
     * Getter method for delivered
     * @return date (LocalDateTime)
     */
    public LocalDateTime getDate(){
        return date;
    }

    /**
     * Setter method for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter method for notes
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    /**
     * Setter method for price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * Setter method for notes
     * @param weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Setter method for notes
     * @param delivered
     */
    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }
    /**
     * Setter method for notes
     * @param date
     */
    public void setDate(LocalDateTime date){
        this.date = date;
    }



    /**
     * returns the info about the package in a String
     * @return (String)
     */
    @Override
    public String toString(){
        return "Package: " + name +
                "\nNotes: " + notes +
                "\nPrice: $" + price +
                "\nWeight: " + weight + "kg" +
                "\nExpected Delivery Data: " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(date) +
                "\nDelivered? " + ((delivered) ? "Yes" : "No");
    }

    @Override
    public int compareTo(Package package2) {
        if(package2.getDate().isBefore(this.date)){
            return 1;
        } else {
            return -1;
        }
    }
} // Package.java
