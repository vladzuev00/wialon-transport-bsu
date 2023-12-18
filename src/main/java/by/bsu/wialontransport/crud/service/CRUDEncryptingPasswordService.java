package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.EntityWithPassword;
import by.bsu.wialontransport.crud.mapper.Mapper;
import by.bsu.wialontransport.crud.repository.EntityWithPasswordRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class CRUDEncryptingPasswordService<
        ID,
        ENTITY extends EntityWithPassword<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends EntityWithPasswordRepository<ID, ENTITY>
        >
        extends CRUDService<ID, ENTITY, DTO, MAPPER, REPOSITORY> {
    private final BCryptPasswordEncoder passwordEncoder;

    public CRUDEncryptingPasswordService(final MAPPER mapper,
                                         final REPOSITORY repository,
                                         final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository);
        this.passwordEncoder = passwordEncoder;
    }

    public int updatePassword(final DTO dto, final String newPassword) {
        return super.findInt(
                repository -> repository.updatePassword(
                        dto.getId(),
                        this.passwordEncoder.encode(newPassword)
                )
        );
    }

    @Override
    protected final void configureBeforeSave(final ENTITY entity) {
        this.injectEncryptedPassword(entity);
    }

    private void injectEncryptedPassword(final ENTITY entity) {
        final String rawPassword = entity.getPassword();
        final String encryptedPassword = this.passwordEncoder.encode(rawPassword);
        entity.setPassword(encryptedPassword);
    }

}
