package by.bsu.wialontransport.service.security.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.service.security.exception.NoAuthorizedUserException;
import by.bsu.wialontransport.service.security.mapper.SecurityUserMapper;
import by.bsu.wialontransport.service.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
@RequiredArgsConstructor
public final class SecurityService implements UserDetailsService {
    private final UserService userService;
    private final SecurityUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {
        return userService.findByEmail(email)
                .map(userMapper::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '%s' doesn't exist".formatted(email)));
    }

    public User findLoggedOnUser() {
        return ofNullable(getContext().getAuthentication())
                .map(authentication -> (SecurityUser) authentication.getPrincipal())
                .map(userMapper::map)
                .orElseThrow(NoAuthorizedUserException::new);
    }
}
