import java.sql.*;

interface ReportGenerator {
    void printDelayedReport();
}

abstract class DatabaseRepository {

    private String url = "jdbc:postgresql://localhost:5432/logistics";
    private String user = "postgres";
    private String password = "password";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

class LogisticsRepository extends DatabaseRepository implements ReportGenerator {

    public void printDelayedReport() {

        String sql = "SELECT s.shipment_id,c.company_name,s.dispatch_date "
                + "FROM shipments s "
                + "JOIN couriers c ON s.courier_id=c.courier_id "
                + "WHERE s.status=? "
                + "ORDER BY s.dispatch_date DESC";

        try (
                Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {

            ps.setString(1, "DELAYED");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    System.out.println(
                            rs.getInt("shipment_id") + " "
                                    + rs.getString("company_name") + " "
                                    + rs.getDate("dispatch_date")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Q1 {

    public static void main(String[] args) {

        LogisticsRepository r = new LogisticsRepository();
        r.printDelayedReport();
    }
}