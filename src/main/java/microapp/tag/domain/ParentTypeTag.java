package microapp.tag.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ParentTypeTag.
 */
@Table("parent_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParentTypeTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("topic")
    private String topic;

    @NotNull(message = "must not be null")
    @Column("parent_id")
    private Long parentId;

    @NotNull(message = "must not be null")
    @Column("parent_type")
    private String parentType;

    @NotNull(message = "must not be null")
    @Column("server")
    private String server;

    @NotNull(message = "must not be null")
    @Column("user_manageable")
    private Boolean userManageable;

    @NotNull(message = "must not be null")
    @Column("is_encrypted")
    private Boolean isEncrypted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParentTypeTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return this.topic;
    }

    public ParentTypeTag topic(String topic) {
        this.setTopic(topic);
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public ParentTypeTag parentId(Long parentId) {
        this.setParentId(parentId);
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return this.parentType;
    }

    public ParentTypeTag parentType(String parentType) {
        this.setParentType(parentType);
        return this;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getServer() {
        return this.server;
    }

    public ParentTypeTag server(String server) {
        this.setServer(server);
        return this;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Boolean getUserManageable() {
        return this.userManageable;
    }

    public ParentTypeTag userManageable(Boolean userManageable) {
        this.setUserManageable(userManageable);
        return this;
    }

    public void setUserManageable(Boolean userManageable) {
        this.userManageable = userManageable;
    }

    public Boolean getIsEncrypted() {
        return this.isEncrypted;
    }

    public ParentTypeTag isEncrypted(Boolean isEncrypted) {
        this.setIsEncrypted(isEncrypted);
        return this;
    }

    public void setIsEncrypted(Boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParentTypeTag)) {
            return false;
        }
        return id != null && id.equals(((ParentTypeTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParentTypeTag{" +
            "id=" + getId() +
            ", topic='" + getTopic() + "'" +
            ", parentId=" + getParentId() +
            ", parentType='" + getParentType() + "'" +
            ", server='" + getServer() + "'" +
            ", userManageable='" + getUserManageable() + "'" +
            ", isEncrypted='" + getIsEncrypted() + "'" +
            "}";
    }
}
