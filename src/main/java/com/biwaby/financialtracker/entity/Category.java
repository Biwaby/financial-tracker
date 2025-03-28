package com.biwaby.financialtracker.entity;

import com.biwaby.financialtracker.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @SequenceGenerator(sequenceName = "category_id_seq", name = "category_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "category_id_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    @NotBlank(message = "The <name> must not be empty.")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotNull(message = "The <type> must not be empty.")
    @Column(name = "type", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
