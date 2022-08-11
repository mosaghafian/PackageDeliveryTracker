package cmpt213.assignment4.packagedeliveries.webappserver.control;

/**
 * PackageTracker: Contains the control part of the MVC design
 * Contains all the functionalities and services for adding, removing, moarking, etc of the packages
 */

import cmpt213.assignment4.packagedeliveries.webappserver.model.*;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Package;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

public class PackageTracker {

    private static PackageTracker singleton = null;

    private PackageTracker() {
    }

    /**
     * sharedInstance
     * @return an singleton
     */
    public static PackageTracker sharedInstance() {
        if (singleton == null) {
            singleton = new PackageTracker();
        }
        return singleton;
    }

    private List<Package> listOfPackages = new ArrayList<Package>();
    PackageFactory packageFactory = new PackageFactory();

    public List<Package> getListOfPackages(){
        sortListOfPackages();
        return this.listOfPackages;
    }
    /**
     * Helper method to sort the listOfPackages by comparing their localDateTime field
     *
     */
    private void sortListOfPackages() {
        // Sorting the listOfPackages with the help of lambda function. The lambda function compares the field date of two
        // packages and return either a negative(-1) or positive(1) value.
        // The packages are sorted in order to the oldest package to the newest.
        // Learned and inspired by https://stackoverflow.com/questions/21970719/java-arrays-sort-with-lambda-expression
        Collections.sort(listOfPackages);
    }

    /**
     * getNumOfPackage
     * returns the number of packages in the listOfPackages
     */
    public int getNumOfPackage(){
        return listOfPackages.size();
    }

    /**
     * addPackage
     * Method for adding a package from view
     */
    public void addPackage(Package package1 ){
        package1.setDelivered(false);
        listOfPackages.add(package1);

    }

    /**
     * removePackages: deletes a package that matches the UUID
     */
    public void removePackage(UUID id){
        int index = 0;
       for(Package tmp: listOfPackages){
           if(tmp.getId().equals(id)){
               listOfPackages.remove(listOfPackages.get(index));
               return;
           }
           index++;
       }
    }

    /**
     * removePackages: deletes a package that matches the UUID
     */
    public void markPackageDelivered(UUID id){
        int index = 0;
        for(Package tmp: listOfPackages){
            if(tmp.getId().equals(id)){
                listOfPackages.get(index).setDelivered(!listOfPackages.get(index).getDelivered());
                return;
            }
            index++;
        }
    }

    /**
     * GetOverduePackages: returns a list of overdue packages
     * @Return List<Package>
     */
    public List<Package> getOverduePackages() {
        List<Package> overduePackages = new ArrayList<Package>();
        // Helper variable to generate automatic package number
        int numOfOverduePackages = 0;
        // Sorting the listOfPackages
        sortListOfPackages();

        // Displaying the overdue and undelivered packages.
        for(int i = 0; i < listOfPackages.size(); i++){
            if(listOfPackages.get(i).getDate().isBefore(LocalDateTime.now())
                    && listOfPackages.get(i).getDelivered() == false){

                overduePackages.add(listOfPackages.get(i));
            }

        }
        return overduePackages;
    }

    /**
     * GetUpcomingPackages: returns a list of overdue packages
     * @Return List<Package>
     */
    public List<Package> getUpcomingPackages(){
        List<Package> upcomingPackages = new ArrayList<Package>();
        // Helper variable to generate automatic package number
        int numOfUpcomingPackages = 0;
        // Sorting the listOfPackages
        sortListOfPackages();
        // displaying the upcoming and undelivered packages
        for(int i = 0; i < listOfPackages.size(); i++){
            if(listOfPackages.get(i).getDate().isAfter(LocalDateTime.now())
                    && listOfPackages.get(i).getDelivered() == false){
                upcomingPackages.add(listOfPackages.get(i));

            }

        }
        return upcomingPackages;
    }



    /**
     * exit: Saving the list of packages to a Json file
     */
    public void exit(){

        Gson myGson = createGsonObject();
        // Creating or Overriding the existing list.json file
        File file = new File("./list.json");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            // Create a Map with the key listOfPackages and value of the listOfPackages and serializing it into String
            // Json format
            Map<String, List<Package>> jsonMap = new HashMap<>();
            jsonMap.put("listOfPackages", listOfPackages);
            String json = myGson.toJson(jsonMap);
            // Accessing the file, list.json, and writing the String Json variable into the list.json with the help of
            // a FileWriter Object.
            // Learned and inspired from https://www.techiedelight.com/write-json-data-to-a-file-in-java/
            FileWriter fileWriter = new FileWriter("./list.json");
            fileWriter.write( json );
            fileWriter.close();

        }catch (Throwable e){
            e.printStackTrace();
        }

    }

    /**
     * A helper method to read the JSON file (list.json) and deserialize its data into a memory based list.
     */
    public void readJSON(){
        // accessing the file "list.json"
        File file = new File("./list.json");
        // Using an if statement to ensure the existence of a list.json file. Later reading the packages' data and parsing
        // the information into packages objects and storing them in a in-memory list.
        if(file.exists()){
            try {
                // Accessing the json file and parsing the file to a JsonElement
                JsonElement fileElement = JsonParser.parseReader(new FileReader(file));
                // Creating a JsonObject that can be accessed with methods of JsonObject class.
                JsonObject fileObject = fileElement.getAsJsonObject();
                // Extracting the json array from the fileObject which was a jsonObject
                JsonArray jsonArrayOfPackages = fileObject.get("listOfPackages").getAsJsonArray();
                // Iterating through the JsonArray object created and deserializing the packages information.
                for(JsonElement packageElement :  jsonArrayOfPackages){
                    JsonObject packageObject = packageElement.getAsJsonObject();

                    Package newPackage;
                    switch (packageObject.get("packageType").getAsInt()){
                        case 1:
                            newPackage = packageFactory.getInstance(1);
                            break;
                        case 2:
                            newPackage = packageFactory.getInstance(2);
                            break;
                        case 3:
                            newPackage = packageFactory.getInstance(3);
                            break;
                        default:
                            newPackage = null;
                            break;
                    }
                    newPackage.setName(packageObject.get("name").getAsString());
                    newPackage.setNotes(packageObject.get("notes").getAsString());
                    newPackage.setPrice( packageObject.get("price").getAsDouble());
                    newPackage.setWeight(packageObject.get("weight").getAsDouble());
                    newPackage.setDelivered(packageObject.get("delivered").getAsBoolean());
                    newPackage.setPackageType(packageObject.get("packageType").getAsInt());
                    newPackage.setDate(LocalDateTime.parse(packageObject.get("date").getAsString()));


                    switch (newPackage.getPackageType()){
                        case 1:
                            ((Book)newPackage).setAuthor(packageObject.get("author").getAsString());
                            break;
                        case 2:
                            ((Perishable)newPackage).setEXPDate(LocalDateTime.parse(packageObject.get("expdate").getAsString()));
                            break;
                        case 3:
                            ((Electronic)newPackage).setENVFee(packageObject.get("envfee").getAsInt());
                            break;
                        default:
                            break;

                    }

                    listOfPackages.add(newPackage);
                }
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }}
    }

    /**
     * Helper method to instantiate a Gson object that adapts to the custom class LocalDateTime
     * write and read functions for serializing and deserializing json format
     * @return myGson (Gson)
     */
    private Gson createGsonObject() {
        // Creating a Gson object with modified write and read method for the class LocalDateTime.
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();

        return myGson;
    }


}
