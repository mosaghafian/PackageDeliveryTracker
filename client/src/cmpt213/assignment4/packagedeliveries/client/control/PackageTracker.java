package cmpt213.assignment4.packagedeliveries.client.control;

import cmpt213.assignment4.packagedeliveries.client.model.Package;
import cmpt213.assignment4.packagedeliveries.client.model.PackageFactory;
import cmpt213.assignment4.packagedeliveries.client.model.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;
/**
 * PackageTracker: Contains the functionalities of the MVC design,
 * Contains the listOfPackages and makes the API calls to the server
 *
 * @Author Mohammad Saghafian
 */
public class PackageTracker {

    private static PackageTracker singleton = null;
    private static final String rootURL = "http://localhost:8080";

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
        listOfPackages.clear();
        package1.setDelivered(false);
        HttpClient client = HttpClient.newHttpClient();
        String type;
        if(package1.getPackageType() == 1){
            type = "Book";
        } else if (package1.getPackageType() == 2){
            type = "Perishable";
        } else {
            type = "Electronic";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(createGsonObject().toJson(package1)))
                .header("Content-Type", "application/json")
                .uri(URI.create(rootURL + "/add" + type))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String jsonString = "{ \"listOfPackages\": " + response.body() + "}" ;
       // System.out.println(jsonString);
        JsonElement stringElement = JsonParser.parseString(jsonString);
        JsonObject stringObject = stringElement.getAsJsonObject();
        JsonArray jsonArray = stringObject.get("listOfPackages").getAsJsonArray();
        for(JsonElement jsonElement: jsonArray) {
            JsonObject packageObject = jsonElement.getAsJsonObject();
            Package newPackage;
            switch (packageObject.get("packageType").getAsInt()) {
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
            newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
            newPackage.setNotes(packageObject.get("notes").getAsString());
            newPackage.setPrice(packageObject.get("price").getAsDouble());
            newPackage.setWeight(packageObject.get("weight").getAsDouble());
            newPackage.setDelivered(packageObject.get("delivered").getAsBoolean());
            newPackage.setPackageType(packageObject.get("packageType").getAsInt());
            newPackage.setDate(LocalDateTime.parse(packageObject.get("date").getAsString()));


            switch (newPackage.getPackageType()) {
                case 1:
                    ((Book) newPackage).setAuthor(packageObject.get("author").getAsString());
                    break;
                case 2:
                    ((Perishable) newPackage).setEXPDate(LocalDateTime.parse(packageObject.get("expdate").getAsString()));
                    break;
                case 3:
                    ((Electronic) newPackage).setENVFee(packageObject.get("envfee").getAsDouble());
                    break;
                default:
                    break;

            }

            listOfPackages.add(newPackage);
        }
    }

    public void removePackage(UUID id){
        listOfPackages.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("MarkPackageAsDelivered"))
                .header("Content-Type", "application/json")
                .uri(URI.create(rootURL + "/removePackage/" + id.toString()))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String jsonString = "{ \"listOfPackages\": " + response.body() + "}" ;
      //  System.out.println(jsonString);
        JsonElement stringElement = JsonParser.parseString(jsonString);
        JsonObject stringObject = stringElement.getAsJsonObject();
        JsonArray jsonArray = stringObject.get("listOfPackages").getAsJsonArray();
        for(JsonElement jsonElement: jsonArray) {
            JsonObject packageObject = jsonElement.getAsJsonObject();
            Package newPackage;
            switch (packageObject.get("packageType").getAsInt()) {
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
            newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
            newPackage.setNotes(packageObject.get("notes").getAsString());
            newPackage.setPrice(packageObject.get("price").getAsDouble());
            newPackage.setWeight(packageObject.get("weight").getAsDouble());
            newPackage.setDelivered(packageObject.get("delivered").getAsBoolean());
            newPackage.setPackageType(packageObject.get("packageType").getAsInt());
            newPackage.setDate(LocalDateTime.parse(packageObject.get("date").getAsString()));


            switch (newPackage.getPackageType()) {
                case 1:
                    ((Book) newPackage).setAuthor(packageObject.get("author").getAsString());
                    break;
                case 2:
                    ((Perishable) newPackage).setEXPDate(LocalDateTime.parse(packageObject.get("expdate").getAsString()));
                    break;
                case 3:
                    ((Electronic) newPackage).setENVFee(packageObject.get("envfee").getAsDouble());
                    break;
                default:
                    break;

            }
            listOfPackages.add(newPackage);
        }
    }

    /**
     * GetOverduePackages: returns a list of overdue packages
     * @Return List<Package>
     */
    public List<Package> getOverduePackages() {
        List<Package> overduePackages = new ArrayList<Package>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(rootURL + "/listOverduePackage"))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!response.body().equals("[]")) {
            String jsonString = "{ \"listOfPackages\": " + response.body() + "}" ;
         //   System.out.println(jsonString);
            JsonElement stringElement = JsonParser.parseString(jsonString);
            JsonObject stringObject = stringElement.getAsJsonObject();
            JsonArray jsonArray = stringObject.get("listOfPackages").getAsJsonArray();
            for(JsonElement jsonElement: jsonArray){
                JsonObject packageObject = jsonElement.getAsJsonObject();
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
                newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
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
                        ((Electronic)newPackage).setENVFee(packageObject.get("envfee").getAsDouble());
                        break;
                    default:
                        break;

                }

                overduePackages.add(newPackage);
            }

        }
        Collections.sort(overduePackages);
        return overduePackages;
    }

    /**
     * GetUpcomingPackages: returns a list of overdue packages
     * @Return List<Package>
     */
    public List<Package> getUpcomingPackages(){
        List<Package> upcomingPackages = new ArrayList<Package>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(rootURL + "/listUpcomingPackage"))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!response.body().equals("[]")) {
            String jsonString = "{ \"listOfPackages\": " + response.body() + "}" ;
         //   System.out.println(jsonString);
            JsonElement stringElement = JsonParser.parseString(jsonString);
            JsonObject stringObject = stringElement.getAsJsonObject();
            JsonArray jsonArray = stringObject.get("listOfPackages").getAsJsonArray();
            for(JsonElement jsonElement: jsonArray){
                JsonObject packageObject = jsonElement.getAsJsonObject();
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
                newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
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
                        ((Electronic)newPackage).setENVFee(packageObject.get("envfee").getAsDouble());
                        break;
                    default:
                        break;

                }

                upcomingPackages.add(newPackage);
            }

        }
        Collections.sort(upcomingPackages);
        return upcomingPackages;
    }

    /**
     * markPackageAsDelivered: Sending a post request to mark the package as delivered with id
     */
    public void markPackageAsDelivered(UUID id){
        listOfPackages.clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("MarkPackageAsDelivered"))
                .header("Content-Type", "application/json")
                .uri(URI.create(rootURL + "/markPackageAsDelivered/" + id.toString()))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String jsonString = "{ \"listOfPackages\": " + response.body() + "}" ;
       // System.out.println(jsonString);
        JsonElement stringElement = JsonParser.parseString(jsonString);
        JsonObject stringObject = stringElement.getAsJsonObject();
        JsonArray jsonArray = stringObject.get("listOfPackages").getAsJsonArray();
        for(JsonElement jsonElement: jsonArray) {
            JsonObject packageObject = jsonElement.getAsJsonObject();
            Package newPackage;
            switch (packageObject.get("packageType").getAsInt()) {
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
            newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
            newPackage.setNotes(packageObject.get("notes").getAsString());
            newPackage.setPrice(packageObject.get("price").getAsDouble());
            newPackage.setWeight(packageObject.get("weight").getAsDouble());
            newPackage.setDelivered(packageObject.get("delivered").getAsBoolean());
            newPackage.setPackageType(packageObject.get("packageType").getAsInt());
            newPackage.setDate(LocalDateTime.parse(packageObject.get("date").getAsString()));


            switch (newPackage.getPackageType()) {
                case 1:
                    ((Book) newPackage).setAuthor(packageObject.get("author").getAsString());
                    break;
                case 2:
                    ((Perishable) newPackage).setEXPDate(LocalDateTime.parse(packageObject.get("expdate").getAsString()));
                    break;
                case 3:
                    ((Electronic) newPackage).setENVFee(packageObject.get("envfee").getAsDouble());
                    break;
                default:
                    break;

            }

            listOfPackages.add(newPackage);
        }
    }

    /**
     * exit: Saving the list of packages to a Json file
     */
    public void exit(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(rootURL + "/exit"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Method to make a Get Request to receive all the packages from the server
     * @return List<Package>
     */
    public void getRequestAllPackages() throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(rootURL + "/listAll"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(!response.body().equals("[]")) {
            String jsonString = "{ \"listOfPackages\": " + response.body() + "}" ;
        //    System.out.println(jsonString);
            JsonElement stringElement = JsonParser.parseString(jsonString);
            JsonObject stringObject = stringElement.getAsJsonObject();
            JsonArray jsonArray = stringObject.get("listOfPackages").getAsJsonArray();
            for(JsonElement jsonElement: jsonArray){
                JsonObject packageObject = jsonElement.getAsJsonObject();
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
                newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
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
                        ((Electronic)newPackage).setENVFee(packageObject.get("envfee").getAsDouble());
                        break;
                    default:
                        break;

                }

                listOfPackages.add(newPackage);
            }

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
                    newPackage.setId(UUID.fromString(packageObject.get("id").getAsString()));
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
                            ((Perishable)newPackage).setEXPDate(LocalDateTime.parse(packageObject.get("EXPDate").getAsString()));
                            break;
                        case 3:
                            ((Electronic)newPackage).setENVFee(packageObject.get("ENVFee").getAsDouble());
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
