import java.sql.*;

interface PortfolioManager {
    void restructurePortfolio(long investorId);
}

abstract class FinancialDatabaseConfig {

    private String url = "jdbc:postgresql://localhost:5432/fire";
    private String user = "postgres";
    private String password = "password";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

class PortfolioRepository extends FinancialDatabaseConfig implements PortfolioManager {

    public void restructurePortfolio(long investorId) {

        String selectQuery =
                "SELECT asset_class,SUM(current_value) " +
                "FROM holdings WHERE investor_id=? " +
                "GROUP BY asset_class";

        String updateDebt =
                "UPDATE holdings SET current_value=current_value-1000 " +
                "WHERE investor_id=? AND asset_class='Debt'";

        String updateEquity =
                "UPDATE holdings SET current_value=current_value+1000 " +
                "WHERE investor_id=? AND asset_class='Equity'";

        Connection con = null;

        try {

            con = getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(selectQuery);
            ps1.setLong(1, investorId);

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(1) + " " + rs.getDouble(2));
            }

            PreparedStatement ps2 = con.prepareStatement(updateDebt);
            ps2.setLong(1, investorId);
            ps2.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement(updateEquity);
            ps3.setLong(1, investorId);
            ps3.executeUpdate();

            con.commit();

            rs.close();
            ps1.close();
            ps2.close();
            ps3.close();
            con.close();

        } catch (Exception e) {

            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException ex) {
            }

            System.out.println(e.getMessage());
        }
    }
}

public class Q3 {

    public static void main(String[] args) {

        PortfolioRepository p = new PortfolioRepository();
        p.restructurePortfolio(1);
    }
}