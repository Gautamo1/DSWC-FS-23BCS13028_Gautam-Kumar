import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Employee {

    @Id
    @GeneratedValue
    Long employeeId;

    String firstName;
    String department;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    Employee manager;

    @OneToMany(mappedBy = "manager")
    List<Employee> directReports;
}

@Entity
class FullTimeEmployee extends Employee {

    double salary;
}

@Entity
class Contractor extends Employee {

    double hourlyRate;
}

class ManagerSpanDTO {

    String managerName;
    Long directReportCount;

    ManagerSpanDTO(String managerName, Long directReportCount) {
        this.managerName = managerName;
        this.directReportCount = directReportCount;
    }
}

interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
        SELECT new ManagerSpanDTO(
        m.firstName,
        COUNT(e))
        FROM Employee e
        INNER JOIN e.manager m
        GROUP BY m.firstName
    """)
    List<ManagerSpanDTO> getManagerReport();

    List<Employee> findByManagerFirstNameAndDepartment(
            String firstName,
            String department
    );
}

public class Q3 {

    public static void main(String[] args) {

        System.out.println("CorpMatrix System");
    }
}