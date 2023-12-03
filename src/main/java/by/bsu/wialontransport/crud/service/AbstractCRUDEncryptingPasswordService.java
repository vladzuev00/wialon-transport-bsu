package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.EntityWithPassword;
import by.bsu.wialontransport.crud.mapper.Mapper;
import by.bsu.wialontransport.crud.repository.EntityWithPasswordRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class AbstractCRUDEncryptingPasswordService<
        ID,
        ENTITY extends EntityWithPassword<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends EntityWithPasswordRepository<ID, ENTITY>
        >
        extends AbstractCRUDService<ID, ENTITY, DTO, MAPPER, REPOSITORY> {
    private final BCryptPasswordEncoder passwordEncoder;

    public AbstractCRUDEncryptingPasswordService(final MAPPER mapper,
                                                 final REPOSITORY repository,
                                                 final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository);
        this.passwordEncoder = passwordEncoder;
    }

    public void updatePassword(final DTO dto, final String newPassword) {
        final String encryptedPassword = this.passwordEncoder.encode(newPassword);
        final ID id = dto.getId();
        super.repository.updatePassword(id, encryptedPassword);
    }

    @Override
    protected final ENTITY configureBeforeSave(final ENTITY entity) {
        this.injectEncryptedPassword(entity);
        return entity;
    }

    private void injectEncryptedPassword(final ENTITY entity) {
        final String rawPassword = entity.getPassword();
        final String encryptedPassword = this.passwordEncoder.encode(rawPassword);
        entity.setPassword(encryptedPassword);
    }

}
