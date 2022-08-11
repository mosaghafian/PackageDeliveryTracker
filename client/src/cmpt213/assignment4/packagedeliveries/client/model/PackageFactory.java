package cmpt213.assignment4.packagedeliveries.client.model;

/**
 * PackageFactory: A class to create objects of different type of package
 *
 * @Author Mohammad Saghafian
 */
public class PackageFactory {

    /**
     * Package Factory
     * @return an object of Package's subclass
     */
    public Package getInstance(int packageType){
        switch (packageType) {
            case 1:
                return new Book();
            case 2:
                return new Perishable();
            case 3:
                return new Electronic();
            default:
                return null;
        }
    }
}
