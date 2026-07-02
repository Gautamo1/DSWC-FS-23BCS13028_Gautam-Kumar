import java.sql.*;

interface RegistrationManager {
    void enrollAtRiskStudents();
}

abstract class DatabaseConnectionProvider {

    private String url = "jdbc:postgresql://localhost:5432/edixo";
    private String user = "postgres";
    private String password = "password";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

class EdixoRegistrationRepository extends DatabaseConnectionProvider implements RegistrationManager {

    public void enrollAtRiskStudents() {

        String selectQuery =
                "SELECT s.student_id,s.full_name " +
                "FROM students s " +
                "LEFT JOIN course_registrations c " +
                "ON s.student_id=c.student_id " +
                "WHERE c.student_id IS NULL";

        String insertQuery =
                "INSERT INTO course_registrations(student_id,course_code,semester) VALUES(?,?,?)";

        try (
                Connection con = getConnection();
                PreparedStatement ps1 = con.prepareStatement(selectQuery);
                PreparedStatement ps2 = con.prepareStatement(insertQuery);
                ResultSet rs = ps1.executeQuery()
        ) {

            while (rs.next()) {

                ps2.setInt(1, rs.getInt("student_id"));
                ps2.setString(2, "Orientation 101");
                ps2.setString(3, "Semester 1");

                ps2.addBatch();
            }

            ps2.executeBatch();

            System.out.println("Students Enrolled");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Q2 {

    public static void main(String[] args) {

        EdixoRegistrationRepository e = new EdixoRegistrationRepository();
        e.enrollAtRiskStudents();
    }
}