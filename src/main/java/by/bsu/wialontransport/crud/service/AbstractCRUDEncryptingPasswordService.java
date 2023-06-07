package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntityWithPassword;
import by.bsu.wialontransport.crud.mapper.AbstractMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class AbstractCRUDEncryptingPasswordService<
        IdType,
        EntityType extends AbstractEntityWithPassword<IdType>,
        DtoType extends AbstractDto<IdType>,
        MapperType extends AbstractMapper<EntityType, DtoType>,
        RepositoryType extends JpaRepository<EntityType, IdType>
        >
        extends AbstractCRUDService<IdType, EntityType, DtoType, MapperType, RepositoryType> {
    private final BCryptPasswordEncoder passwordEncoder;

    public AbstractCRUDEncryptingPasswordService(final MapperType mapper,
                                                 final RepositoryType repository,
                                                 final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository);
        this.passwordEncoder = passwordEncoder;
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
