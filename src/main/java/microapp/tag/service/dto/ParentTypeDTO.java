package microapp.tag.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.tag.domain.ParentTypeTag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParentTypeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long parentId;

    @NotNull(message = "must not be null")
    private String parentType;

    @NotNull(message = "must not be null")
    private String server;

    @NotNull(message = "must not be null")
    private Boolean userManageable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Boolean getUserManageable() {
        return userManageable;
    }

    public void setUserManageable(Boolean userManageable) {
        this.userManageable = userManageable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParentTypeDTO)) {
            return false;
        }

        ParentTypeDTO parentTypeDTO = (ParentTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parentTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParentTypeDTO{" +
            "id=" + getId() +
            ", parentId=" + getParentId() +
            ", parentType='" + getParentType() + "'" +
            ", server='" + getServer() + "'" +
            ", userManageable='" + getUserManageable() + "'" +
            "}";
    }
}
