package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.EntityWithPassword;
import by.bsu.wialontransport.crud.mapper.Mapper;
import by.bsu.wialontransport.crud.repository.EntityWithPasswordRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class CRUDEncryptingPasswordService<
        ID,
        ENTITY extends EntityWithPassword<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends EntityWithPasswordRepository<ID, ENTITY>
        >
        extends CRUDService<ID, ENTITY, DTO, MAPPER, REPOSITORY> {
    private final PasswordEncoder passwordEncoder;

    public CRUDEncryptingPasswordService(final MAPPER mapper,
                                         final REPOSITORY repository,
                                         final PasswordEncoder passwordEncoder) {
        super(mapper, repository);
        this.passwordEncoder = passwordEncoder;
    }

    public int updatePassword(final DTO dto, final String newPassword) {
        return findInt(
                repository -> repository.updatePassword(
                        dto.getId(),
                        passwordEncoder.encode(newPassword)
                )
        );
    }

    @Override
    protected final void configureBeforeSave(final ENTITY entity) {
        injectEncryptedPassword(entity);
    }

    private void injectEncryptedPassword(final ENTITY entity) {
        final String encryptedPassword = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(encryptedPassword);
    }

}
