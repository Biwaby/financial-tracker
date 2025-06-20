package com.biwaby.financialtracker.entity;

import com.biwaby.financialtracker.enums.SavingsAccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "savings_accounts")
public class SavingsAccount {

    @Id
    @SequenceGenerator(sequenceName = "savings_account_id_seq", name = "savings_account_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "savings_account_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id", nullable = false)
    private Currency currency;

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

    @Column(name = "deadline", nullable = true)
    private LocalDate deadlineDate;

    @Column(name = "status", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private SavingsAccountStatus status;

    @JsonIgnore
    @OneToMany(
            mappedBy = "savingsAccount",
            targetEntity = SavingsTransaction.class,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private List<SavingsTransaction> savingsTransactions;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SavingsAccount that = (SavingsAccount) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
