package ru.practicum.bankapp.accounts.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", schema = "accounts_service")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(name = "user-fetch-accounts",
        attributeNodes = {
                @NamedAttributeNode("accounts")
        }
)
public class User {
    @Id
    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_day", nullable = false)
    private LocalDate birthdate;

    @OneToMany(mappedBy = "userLogin", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    private List<UserAccount> accounts;

    @AssertTrue
    public boolean isOnlyUsersAccounts() {
        if (accounts == null || accounts.isEmpty() || login == null) {
            return true;
        }
        Set<String> logins = accounts.stream().filter(Objects::nonNull)
                .map(UserAccount::getUserLogin)
                .collect(Collectors.toSet());
        return logins.size() == 1 && logins.contains(login);
    }
}
