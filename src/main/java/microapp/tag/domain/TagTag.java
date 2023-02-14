package microapp.tag.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TagTag.
 */
@Table("tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("tag")
    private String tag;

    @Column("color")
    private String color;

    @Column("icon")
    private String icon;

    @NotNull(message = "must not be null")
    @Column("parent_id")
    private Long parentId;

    @NotNull(message = "must not be null")
    @Column("parent_type")
    private String parentType;

    @NotNull(message = "must not be null")
    @Column("parent_server")
    private String parentServer;

    @NotNull(message = "must not be null")
    @Column("parent_uuid")
    private UUID parentUuid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TagTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public TagTag tag(String tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getColor() {
        return this.color;
    }

    public TagTag color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return this.icon;
    }

    public TagTag icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public TagTag parentId(Long parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return this.parentType;
    }

    public TagTag parentType(String parentType) {
        this.setParentType(parentType);
        return this;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getParentServer() {
        return this.parentServer;
    }

    public TagTag parentServer(String parentServer) {
        this.setParentServer(parentServer);
        return this;
    }

    public void setParentServer(String parentServer) {
        this.parentServer = parentServer;
    }

    public UUID getParentUuid() {
        return this.parentUuid;
    }

    public TagTag parentUuid(UUID parentUuid) {
        this.setParentUuid(parentUuid);
        return this;
    }

    public void setParentUuid(UUID parentUuid) {
        this.parentUuid = parentUuid;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagTag)) {
            return false;
        }
        return id != null && id.equals(((TagTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagTag{" +
            "id=" + getId() +
            ", tag='" + getTag() + "'" +
            ", color='" + getColor() + "'" +
            ", icon='" + getIcon() + "'" +
            ", parentId=" + getParentId() +
            ", parentType='" + getParentType() + "'" +
            ", parentServer='" + getParentServer() + "'" +
            ", parentUuid='" + getParentUuid() + "'" +
            "}";
    }
}
