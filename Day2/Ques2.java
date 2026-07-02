abstract class DeliveryDrone {

    String droneId;

    DeliveryDrone(String id) {
        droneId = id;
    }

    abstract void deliverPackage();
}

interface Airborne {

    void flyToDestination();

    default void requestAirTrafficClearance() {
        System.out.println("Air Clearance Given");
    }
}

interface GroundBased {
    void navigateSidewalks();
}

class Quadcopter extends DeliveryDrone implements Airborne {

    Quadcopter(String id) {
        super(id);
    }

    public void flyToDestination() {
        System.out.println("Flying");
    }

    void deliverPackage() {
        requestAirTrafficClearance();
        flyToDestination();
        System.out.println("Package Delivered");
    }
}

class CityRover extends DeliveryDrone implements GroundBased {

    CityRover(String id) {
        super(id);
    }

    public void navigateSidewalks() {
        System.out.println("Moving");
    }

    void deliverPackage() {
        navigateSidewalks();
        System.out.println("Package Delivered");
    }
}

class HybridVTOL extends DeliveryDrone implements Airborne, GroundBased {

    HybridVTOL(String id) {
        super(id);
    }

    public void flyToDestination() {
        System.out.println("Flying");
    }

    public void navigateSidewalks() {
        System.out.println("Moving");
    }

    void deliverPackage() {
        requestAirTrafficClearance();
        flyToDestination();
        navigateSidewalks();
        System.out.println("Package Delivered");
    }
}

public class Ques2 {

    public static void main(String[] args) {

        DeliveryDrone d1 = new Quadcopter("D1");
        DeliveryDrone d2 = new CityRover("D2");
        DeliveryDrone d3 = new HybridVTOL("D3");

        d1.deliverPackage();
        d2.deliverPackage();
        d3.deliverPackage();
    }
}