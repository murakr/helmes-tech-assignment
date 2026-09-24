package ee.helmes.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "sector")
public class Sector {

    @Id
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    protected Sector() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}