package com.stepaniuk.workshop.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_gen")
    @SequenceGenerator(name = "orders_id_gen", sequenceName = "orders_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "comment", nullable = true)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OrderStatus status;

    @Column(name = "appointment_time", nullable = false)
    private Instant appointmentTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "last_modified_at", nullable = false)
    @LastModifiedDate
    private Instant lastModifiedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "customerId = " + customerId + ", " +
                "comment = " + comment + ", " +
                "status = " + status + ", " +
                "createdAt = " + createdAt + ", " +
                "lastModifiedAt = " + lastModifiedAt + ")";
    }
}
