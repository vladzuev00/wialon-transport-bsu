package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.mapToList;

public abstract class AbstractCRUDService<
        ID,
        ENTITY extends Entity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, ID>
        >
        extends AbstractRUDService<ID, ENTITY, DTO, MAPPER, REPOSITORY> {

    public AbstractCRUDService(final MAPPER mapper, final REPOSITORY repository) {
        super(mapper, repository);
    }

    public DTO save(final DTO dto) {
        final ENTITY entityToBeSaved = this.mapToConfiguredEntity(dto);
        final ENTITY savedEntity = super.repository.save(entityToBeSaved);
        return super.mapper.mapToDto(savedEntity);
    }

    public List<DTO> saveAll(final Collection<DTO> dtos) {
        final List<ENTITY> entitiesToBeSaved = this.mapToConfiguredEntities(dtos);
        final List<ENTITY> savedEntities = super.repository.saveAll(entitiesToBeSaved);
        return super.mapper.mapToDtos(savedEntities);
    }

    protected abstract ENTITY configureBeforeSave(final ENTITY source);

    private ENTITY mapToConfiguredEntity(final DTO source) {
        final ENTITY sourceEntity = super.mapper.mapToEntity(source);
        return this.configureBeforeSave(sourceEntity);
    }

    private List<ENTITY> mapToConfiguredEntities(final Collection<DTO> sources) {
        return mapToList(sources, this::mapToConfiguredEntity);
    }
}