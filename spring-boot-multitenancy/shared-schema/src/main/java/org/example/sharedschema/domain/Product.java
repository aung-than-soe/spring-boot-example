package org.example.sharedschema.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.PersistenceCreator;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@PersistenceCreator))
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"code", "tenant_id"}, name = "unique_code_per_tenant")})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private BigDecimal price;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id", name = "fk_category_id")
    @ToString.Exclude
    private Category category;

    @TenantId()
    private String tenantId;

}
