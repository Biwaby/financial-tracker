package com.biwaby.financialtracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @SequenceGenerator(sequenceName = "currency_id_seq", name = "currency_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "currency_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "The <code> must not be empty.")
    @Pattern(regexp = "^[0-9]{3}$", message = "The <code> must contain 3 digits from 0 to 9.")
    @Column(name = "code", nullable = false, length = 3)
    private String code;

    @NotBlank(message = "The <letterCode> must not be empty.")
    @Pattern(regexp = "^[a-zA-Z]{3}$", message = "The <letterCode> must consist of 3 case-insensitive characters of the Latin alphabet.")
    @Column(name = "letter_code", nullable = false, length = 3)
    private String letterCode;

    @Size(min = 3, max = 100, message = "The <name> must contain from 3 to 100 characters.")
    @NotBlank(message = "The <name> must not be empty.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(targetEntity = Wallet.class, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<Wallet> walletsWithCurrency;

    @OneToMany(targetEntity = SavingsAccount.class, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private List<SavingsAccount> savingsAccountsWithCurrency;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Currency currency = (Currency) o;
        return getId() != null && Objects.equals(getId(), currency.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
