import java.sql.*;

interface QueueWorker {
    void processNextJob();
}

abstract class EnterpriseConnectionFactory {

    private String url = "jdbc:postgresql://localhost:5432/company";
    private String user = "postgres";
    private String password = "password";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

class JobRepository extends EnterpriseConnectionFactory implements QueueWorker {

    public void processNextJob() {

        String selectQuery =
                "SELECT b.job_id " +
                "FROM background_jobs b " +
                "INNER JOIN departments d " +
                "ON b.dept_id=d.dept_id " +
                "WHERE b.status=? " +
                "AND d.dept_name=? " +
                "ORDER BY b.created_at " +
                "LIMIT 1 " +
                "FOR UPDATE SKIP LOCKED";

        String updateQuery =
                "UPDATE background_jobs SET status=? WHERE job_id=?";

        Connection con = null;

        try {

            con = getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(selectQuery);

            ps1.setString(1, "PENDING");
            ps1.setString(2, "Engineering");

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("job_id");

                PreparedStatement ps2 = con.prepareStatement(updateQuery);

                ps2.setString(1, "PROCESSING");
                ps2.setInt(2, id);

                ps2.executeUpdate();
                ps2.close();

                System.out.println("Processing Job : " + id);
            }

            con.commit();

            rs.close();
            ps1.close();
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

public class Q5 {

    public static void main(String[] args) {

        JobRepository j = new JobRepository();
        j.processNextJob();
    }
}