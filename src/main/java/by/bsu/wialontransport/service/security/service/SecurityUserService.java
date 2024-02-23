package by.bsu.wialontransport.service.security.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.service.security.mapper.SecurityUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class SecurityUserService implements UserDetailsService {
    private final UserService userService;
    private final SecurityUserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        final Optional<User> optionalUser = this.userService.findByEmail(email);
        return optionalUser
                .map(this.mapper::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with email %s doesn't exist".formatted(email)));
    }
}
