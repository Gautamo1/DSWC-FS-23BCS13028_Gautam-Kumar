import java.util.*;

class Passenger {
    String passportNumber;
    String fullName;
    String nationality;

    Passenger(String p, String n, String c) {
        passportNumber = p;
        fullName = n;
        nationality = c;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger)) return false;

        Passenger p = (Passenger) o;

        return passportNumber.equals(p.passportNumber)
                && nationality.equals(p.nationality);
    }

    public int hashCode() {
        return Objects.hash(passportNumber, nationality);
    }
}

class ManifestManager {

    Set<Passenger> noFly = new HashSet<>();
    Map<String, List<Passenger>> flights = new HashMap<>();
    Map<Passenger, String> directory = new HashMap<>();

    void addToNoFlyList(Passenger p) {
        noFly.add(p);
    }

    void checkInPassenger(String flight, Passenger p) {
        flights.computeIfAbsent(flight, k -> new ArrayList<>()).add(p);
    }

    boolean processCheckIn(String flight, Passenger p) {

        if (noFly.contains(p))
            return false;

        checkInPassenger(flight, p);
        directory.put(p, flight);

        return true;
    }

    String locatePassengerFlight(Passenger p) {
        return directory.get(p);
    }
}

public class Ques1 {

    public static void main(String[] args) {

        ManifestManager m = new ManifestManager();

        Passenger p1 = new Passenger("A123","Ali","India");
        Passenger p2 = new Passenger("B456","Sara","USA");

        m.addToNoFlyList(p2);

        System.out.println(m.processCheckIn("AI101",p1));
        System.out.println(m.processCheckIn("AI101",p2));

        System.out.println(m.locatePassengerFlight(p1));
    }
}