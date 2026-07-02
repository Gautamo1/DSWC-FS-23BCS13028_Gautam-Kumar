import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "record_type")
abstract class MedicalRecord {

    @Id
    @GeneratedValue
    Long id;

    String details;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;
}

@Entity
@DiscriminatorValue("PRESCRIPTION")
class PrescriptionRecord extends MedicalRecord {

    String medicationName;
}

@Entity
@DiscriminatorValue("LAB")
class LabResultRecord extends MedicalRecord {

    String testName;
}

@Entity
class Patient {

    @Id
    @GeneratedValue
    Long patientId;

    String patientName;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MedicalRecord> records;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    BillingAccount billingAccount;
}

@Entity
class BillingAccount {

    @Id
    Long patientId;

    BigDecimal currentBalance;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    Patient patient;
}

class PatientBillingSummaryDTO {

    String patientName;
    String recordType;
    BigDecimal currentBalance;

    PatientBillingSummaryDTO(String patientName,
                             String recordType,
                             BigDecimal currentBalance) {

        this.patientName = patientName;
        this.recordType = recordType;
        this.currentBalance = currentBalance;
    }
}

interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("""
        SELECT new PatientBillingSummaryDTO(
        p.patientName,
        TYPE(r).name,
        b.currentBalance)
        FROM Patient p
        JOIN p.records r
        JOIN p.billingAccount b
    """)
    List<PatientBillingSummaryDTO> getBillingSummary();

    List<Patient> findDistinctByBillingAccountCurrentBalanceGreaterThanAndRecordsMedicationName(
            BigDecimal amount,
            String medicationName
    );
}

public class Q1 {

    public static void main(String[] args) {

        System.out.println("HealthSync System");
    }
}