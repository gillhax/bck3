package quiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import quiz.domain.AbstractAuditingEntity;

@Entity
@Table(
    name = "jhi_user"
)
@Cache(
    usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
@Getter
@Setter
public class User extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(
        strategy = GenerationType.AUTO
    )
    private Long id;
    @NotNull
    @Email
    @Size(
        min = 1,
        max = 100
    )
    @Column(
        length = 100,
        unique = true,
        nullable = false
    )
    private String login;
    @JsonIgnore
    @NotNull
    @Size(
        min = 60,
        max = 60
    )
    @Column(
        name = "password_hash",
        length = 60
    )
    private String password;
    @NotNull
    @Column(
        nullable = false
    )
    private boolean activated = false;
    @Size(
        min = 2,
        max = 5
    )
    @Column(
        name = "lang_key",
        length = 5
    )
    private String langKey;
    @Size(
        max = 20
    )
    @Column(
        name = "activation_key",
        length = 20
    )
    @JsonIgnore
    private String activationKey;
    @Size(
        max = 20
    )
    @Column(
        name = "reset_key",
        length = 20
    )
    private String resetKey;
    @Column(
        name = "reset_date",
        nullable = true
    )
    private ZonedDateTime resetDate = null;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
        )},
        inverseJoinColumns = {@JoinColumn(
            name = "authority_name",
            referencedColumnName = "name"
        )}
    )
    @Cache(
        usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
    )
    private Set authorities = new HashSet();

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            User user = (User) o;
            return this.login.equals(user.login);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.login.hashCode();
    }

    public String toString() {
        return "User{login=\'" + this.login + '\'' + ", activated=\'" + this.activated + '\'' + ", langKey=\'" + this.langKey + '\'' + ", activationKey=\'" + this.activationKey + '\'' + "}";
    }

}
