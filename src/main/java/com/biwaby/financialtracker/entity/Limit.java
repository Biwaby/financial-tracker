package com.biwaby.financialtracker.entity;

import com.biwaby.financialtracker.enums.LimitType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "limits")
public class Limit {

    @Id
    @SequenceGenerator(sequenceName = "limit_id_seq", name = "limit_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "limit_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    
    @OneToOne
    @JoinColumn(
            name = "wallet_id",
            referencedColumnName = "id",
            unique = true,
            nullable = false
    )
    private Wallet wallet;

    @Column(name = "type", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private LimitType type;

    @Column(
            name = "target_amount",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal targetAmount;

    @Column(
            name = "current_amount",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(name = "is_exceeded", nullable = false)
    private Boolean isExceeded = Boolean.FALSE;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.FALSE;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Limit limit = (Limit) o;
        return getId() != null && Objects.equals(getId(), limit.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
