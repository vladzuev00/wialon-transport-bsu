package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.EntityWithPassword;
import by.bsu.wialontransport.crud.mapper.Mapper;
import by.bsu.wialontransport.crud.repository.EntityWithPasswordRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class AbstractCRUDEncryptingPasswordService<
        IdType,
        EntityType extends EntityWithPassword<IdType>,
        DtoType extends Dto<IdType>,
        MapperType extends Mapper<EntityType, DtoType>,
        RepositoryType extends EntityWithPasswordRepository<IdType, EntityType>
        >
        extends AbstractCRUDService<IdType, EntityType, DtoType, MapperType, RepositoryType> {
    private final BCryptPasswordEncoder passwordEncoder;

    public AbstractCRUDEncryptingPasswordService(final MapperType mapper,
                                                 final RepositoryType repository,
                                                 final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository);
        this.passwordEncoder = passwordEncoder;
    }

    public void updatePassword(final DtoType source, final String newPassword) {
        final String encryptedPassword = this.passwordEncoder.encode(newPassword);
        super.repository.updatePassword(source.getId(), encryptedPassword);
    }

    @Override
    protected final EntityType configureBeforeSave(final EntityType source) {
        this.injectEncryptedPassword(source);
        return source;
    }

    private void injectEncryptedPassword(final EntityType source) {
        final String rawPassword = source.getPassword();
        final String encryptedPassword = this.passwordEncoder.encode(rawPassword);
        source.setPassword(encryptedPassword);
    }

}
