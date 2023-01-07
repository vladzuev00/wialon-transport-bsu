package by.bsu.wialontransport.crud.dto;

@FunctionalInterface
public interface AbstractDto<IdType> {
    IdType getId();
}
