package cmpt213.assignment4.packagedeliveries.webappserver.controllers;

/**
 * RESTController is responsible for handling HTTP Requests to the server
 *
 * @Author: Mohammad Saghafian
 */
import cmpt213.assignment4.packagedeliveries.webappserver.control.PackageTracker;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Book;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Electronic;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Package;
import cmpt213.assignment4.packagedeliveries.webappserver.model.Perishable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLTableCaptionElement;

import java.util.List;
import java.util.UUID;

@RestController
public class RESTController {

    PackageTracker packageTracker = PackageTracker.sharedInstance();

    /**
     * Handles GET request for /ping
     * @return "System is up!"
     */
    @GetMapping("/ping")
    public String getSystemUpMSG(){
        return "System is up!";
    }

    /**
     * Handles GET request for /listAll
     * @return JSON(List<Package>)
     */
    @GetMapping("/listAll")
    public List<Package> getAllPackages(){
        return packageTracker.getListOfPackages();
    }

    /**
     * Handles POST request for /addBook
     * @param book
     * @return List<Package>
     */
    @PostMapping("/addBook")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Package> addBook(@RequestBody Book book){
        packageTracker.addPackage(book);
       // System.out.println("Things went well");
        return packageTracker.getListOfPackages();
    }

    /**
     * Handles POST request for /addPerishabel
     * @param perishable
     * @return List<Package>
     */
    @PostMapping("/addPerishable")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Package> addPerishable(@RequestBody Perishable perishable){
        packageTracker.addPackage(perishable);
        return packageTracker.getListOfPackages();
    }

    /**
     * Handles POST request for /addElectronic
     * @param electronic
     * @return List<Package>
     */
    @PostMapping("/addElectronic")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Package> addElectronic(@RequestBody Electronic electronic){
        packageTracker.addPackage(electronic);
        return packageTracker.getListOfPackages();
    }

    /**
     * Handles POST request for /removePackage/PackageID
     * @param packageID
     * @return List<Package>
     */
    @PostMapping("/removePackage/{id}")
    public List<Package> removePackage(@PathVariable("id") String packageID){
       // System.out.println(packageID);
        packageTracker.removePackage(UUID.fromString(packageID));
        return packageTracker.getListOfPackages();
    }

    /**
     * Handles POST request for /listOverduePackage
     * @return List<Package>
     */
    @GetMapping("/listOverduePackage")
    public List<Package> getOverduePackages(){
        return packageTracker.getOverduePackages();
    }

    /**
     * Handles GET request for /listUpcomingPackage
     * @return List<Package>
     */
    @GetMapping("/listUpcomingPackage")
    @ResponseStatus(HttpStatus.OK)
    public List<Package> getUpcomingPackages(){
        return packageTracker.getUpcomingPackages();
    }

    /**
     * Handles GET request for /markPackageAsDelivered/{id}
     * @param packageID
     * @return ResponseEntity
     */
    @PostMapping("/markPackageAsDelivered/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Package> markPackageDelivered(@PathVariable("id") String packageID){
        packageTracker.markPackageDelivered(UUID.fromString(packageID));
        return packageTracker.getListOfPackages();
    }

    /**
     * Handles GET request for /exit
     * @return ResponseEntity
     */
    @GetMapping("/exit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity exit(){
        packageTracker.exit();
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
