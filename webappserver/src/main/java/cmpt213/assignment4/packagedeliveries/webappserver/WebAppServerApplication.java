package cmpt213.assignment4.packagedeliveries.webappserver;

import cmpt213.assignment4.packagedeliveries.webappserver.control.PackageTracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WebAppServerApplication: Contains the main method for starting the web server
 *
 * @Author: Mohammad Saghafian
 */
@SpringBootApplication
public class WebAppServerApplication {
	static PackageTracker packageTracker = PackageTracker.sharedInstance();
	public static void main(String[] args) {
		packageTracker.readJSON();
		SpringApplication.run(WebAppServerApplication.class, args);
	}

}
