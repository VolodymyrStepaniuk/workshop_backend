package com.stepaniuk.workshop.order;

import com.stepaniuk.workshop.types.order.PriceProperties;
import com.stepaniuk.workshop.types.service.Price;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "order_items")
@IdClass(OrderItem.CompositeId.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_items_id_gen")
    @SequenceGenerator(name = "order_line_items_id_gen", sequenceName = "order_line_items_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Type(JsonType.class)
    @Column(name = "price", nullable = false, columnDefinition = "jsonb")
    private Price price;

    @Type(JsonType.class)
    @Column(name = "price_properties", nullable = true, columnDefinition = "jsonb")
    @Nullable
    private PriceProperties priceProperties;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderItem orderItem = (OrderItem) o;
        return getId() != null && Objects.equals(getId(), orderItem.getId())
                && getOrderId() != null && Objects.equals(getOrderId(), orderItem.getOrderId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, orderId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "orderId = " + orderId + ", " +
                "serviceId = " + serviceId + ", " +
                "price = " + price + ", " +
                "priceProperties = " + priceProperties + ")";
    }

    @Getter
    @Setter
    public static class CompositeId implements Serializable {

        private Long id;

        private Long orderId;


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CompositeId that)) {
                return false;
            }
            return Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, orderId);
        }

        @Override
        public String toString() {
            return "CompositeId{" + "id=" + id + ", orderId=" + orderId + '}';
        }
    }
}
