import java.sql.*;
import java.time.LocalDateTime;

interface TelemetryService {
    void printLatestLocations();
}

abstract class FleetDatabaseConnection {

    private String url = "jdbc:postgresql://localhost:5432/fleet";
    private String user = "postgres";
    private String password = "password";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

class FleetRepository extends FleetDatabaseConnection implements TelemetryService {

    public void printLatestLocations() {

        String sql =
                "SELECT rider_name,bike_model,latitude,longitude,recorded_at " +
                "FROM (" +
                "SELECT r.rider_name,r.bike_model,g.latitude,g.longitude,g.recorded_at," +
                "ROW_NUMBER() OVER(PARTITION BY g.rider_id ORDER BY g.recorded_at DESC) rn " +
                "FROM riders r INNER JOIN gps_pings g " +
                "ON r.rider_id=g.rider_id" +
                ") t WHERE rn=1";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String name = rs.getString("rider_name");
                String bike = rs.getString("bike_model");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                LocalDateTime time =
                        rs.getObject("recorded_at", LocalDateTime.class);

                System.out.println(name + " " + bike + " " + lat + " " + lon + " " + time);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Q4 {

    public static void main(String[] args) {

        FleetRepository f = new FleetRepository();
        f.printLatestLocations();
    }
}