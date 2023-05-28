package by.bsu.wialontransport.security.model;

import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static java.util.Arrays.stream;

@Value
@AllArgsConstructor
@Builder
public class SecurityUser implements UserDetails {
    Long id;
    String email;
    String password;
    SecurityRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(this.role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum SecurityRole implements GrantedAuthority {
        USER(Role.USER), ADMIN(Role.ADMIN);

        private final Role role;

        SecurityRole(final Role role) {
            this.role = role;
        }

        @Override
        public final String getAuthority() {
            return super.name();
        }

        public static SecurityRole findByRole(final Role role) {
            return stream(values())
                    .filter(securityRole -> securityRole.role == role)
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalStateException("There is no security role for %s".formatted(role))
                    );
        }
    }
}
