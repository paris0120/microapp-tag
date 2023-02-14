package microapp.tag.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.tag.domain.TagTag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagDTO implements Serializable {

    private Long id;

    private String tag;

    private String color;

    private String icon;

    @NotNull(message = "must not be null")
    private Long parentId;

    @NotNull(message = "must not be null")
    private String parentType;

    @NotNull(message = "must not be null")
    private String parentServer;

    @NotNull(message = "must not be null")
    private UUID parentUuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getParentServer() {
        return parentServer;
    }

    public void setParentServer(String parentServer) {
        this.parentServer = parentServer;
    }

    public UUID getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(UUID parentUuid) {
        this.parentUuid = parentUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagDTO)) {
            return false;
        }

        TagDTO tagDTO = (TagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagDTO{" +
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
