package microapp.tag.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ServerTag.
 */
@Table("server")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServerTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("server")
    private String server;

    @Column("uuid")
    private UUID uuid;

    @Column("decoder")
    private String decoder;

    @Column("password")
    private String password;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServerTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServer() {
        return this.server;
    }

    public ServerTag server(String server) {
        this.setServer(server);
        return this;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ServerTag uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDecoder() {
        return this.decoder;
    }

    public ServerTag decoder(String decoder) {
        this.setDecoder(decoder);
        return this;
    }

    public void setDecoder(String decoder) {
        this.decoder = decoder;
    }

    public String getPassword() {
        return this.password;
    }

    public ServerTag password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerTag)) {
            return false;
        }
        return id != null && id.equals(((ServerTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServerTag{" +
            "id=" + getId() +
            ", server='" + getServer() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", decoder='" + getDecoder() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
