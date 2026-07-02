import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@Embeddable
class MaintenanceLog {

    String action;
    LocalDate maintenanceDate;
}

@Entity
class Aircraft {

    @Id
    @GeneratedValue
    Long aircraftId;

    String modelName;
    boolean isGrounded;

    @ElementCollection
    @CollectionTable(
            name = "maintenance_logs",
            joinColumns = @JoinColumn(name = "aircraft_id")
    )
    List<MaintenanceLog> logs;
}

interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    @Query("""
        SELECT DISTINCT a
        FROM Aircraft a
        JOIN a.logs l
        WHERE l.action = :action
        AND l.maintenanceDate BETWEEN :startDate AND :endDate
    """)
    Page<Aircraft> findAircraftByMaintenance(
            String action,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    List<Aircraft> findByModelNameInAndIsGroundedTrue(
            List<String> modelNames
    );
}

public class Q5 {

    public static void main(String[] args) {

        System.out.println("AeroFleet System");
    }
}