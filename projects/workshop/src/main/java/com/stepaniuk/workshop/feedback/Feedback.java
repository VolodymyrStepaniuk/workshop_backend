package com.stepaniuk.workshop.feedback;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedbacks_id_gen")
    @SequenceGenerator(name = "feedbacks_id_gen", sequenceName = "feedbacks_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "reviewer_id", nullable = false)
    private Long reviewerId;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "last_modified_at", nullable = false)
    @LastModifiedDate
    private Instant lastModifiedAt;

    @Type(ListArrayType.class)
    @Column(name = "categories", columnDefinition = "bigint[]", nullable = false)
    private List<Long> categories;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Feedback feedback = (Feedback) o;
        return getId() != null && Objects.equals(getId(), feedback.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "orderId = " + orderId + ", " +
                "reviewerId = " + reviewerId + ", " +
                "rating = " + rating + ", " +
                "comment = " + comment + ", " +
                "createdAt = " + createdAt + ", " +
                "lastModifiedAt = " + lastModifiedAt + ", " +
                "categories = " + categories + ")";
    }
}
