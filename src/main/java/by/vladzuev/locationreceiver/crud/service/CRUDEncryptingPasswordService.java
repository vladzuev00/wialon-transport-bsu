package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Dto;
import by.vladzuev.locationreceiver.crud.entity.SecuredEntity;
import by.vladzuev.locationreceiver.crud.mapper.Mapper;
import by.vladzuev.locationreceiver.crud.repository.SecuredEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class CRUDEncryptingPasswordService<
        ID,
        ENTITY extends SecuredEntity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends SecuredEntityRepository<ID, ENTITY>
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
