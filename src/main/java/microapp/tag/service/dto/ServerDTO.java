package microapp.tag.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.tag.domain.ServerTag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServerDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String server;

    private UUID uuid;

    private String decoder;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDecoder() {
        return decoder;
    }

    public void setDecoder(String decoder) {
        this.decoder = decoder;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerDTO)) {
            return false;
        }

        ServerDTO serverDTO = (ServerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServerDTO{" +
            "id=" + getId() +
            ", server='" + getServer() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", decoder='" + getDecoder() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
