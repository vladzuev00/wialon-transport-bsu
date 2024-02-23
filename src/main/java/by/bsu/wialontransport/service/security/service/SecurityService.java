package by.bsu.wialontransport.service.security.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.service.security.mapper.SecurityUserMapper;
import by.bsu.wialontransport.service.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
@RequiredArgsConstructor
public final class SecurityService {
    private final SecurityUserMapper mapper;

    public User findLoggedOnUser() {
        final SecurityUser securityUser = (SecurityUser) getContext().getAuthentication().getPrincipal();
        return mapper.map(securityUser);
    }

}
