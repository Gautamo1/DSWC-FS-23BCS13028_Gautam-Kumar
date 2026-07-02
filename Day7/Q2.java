import jakarta.persistence.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Embeddable
class OrderLineItemId {

    Long orderId;
    Long productId;
}

@Entity
class PurchaseOrder {

    @Id
    @GeneratedValue
    Long orderId;

    String customerEmail;
    String status;
    LocalDateTime orderTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderLineItem> items;
}

@Entity
class Product {

    @Id
    @GeneratedValue
    Long productId;

    String productName;

    @OneToMany(mappedBy = "product")
    List<OrderLineItem> items;
}

@Entity
class OrderLineItem {

    @EmbeddedId
    OrderLineItemId id;

    int quantity;
    BigDecimal lockedPrice;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    PurchaseOrder order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;
}

interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM PurchaseOrder p
        JOIN FETCH p.items i
        JOIN FETCH i.product
        WHERE p.status='PENDING'
    """)
    List<PurchaseOrder> getPendingOrders();

    List<PurchaseOrder>
    findByOrderTimeBetweenAndCustomerEmailEndingWith(
            LocalDateTime start,
            LocalDateTime end,
            String domain
    );
}

public class Q2 {

    public static void main(String[] args) {

        System.out.println("CartFlux System");
    }
}