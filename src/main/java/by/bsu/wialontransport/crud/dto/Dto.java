package by.bsu.wialontransport.crud.dto;

@FunctionalInterface
public interface Dto<IdType> {
    IdType getId();
}
