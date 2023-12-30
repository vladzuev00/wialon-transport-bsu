package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.util.HibernateUtil;
import lombok.*;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static by.bsu.wialontransport.util.HibernateUtil.isFetched;
import static java.lang.String.join;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class MapperTest extends AbstractContextTest {
    private final ModelMapper modelMapper = new ModelMapper();

    @SuppressWarnings("unused")
    private final AddressMapper addressMapper = new AddressMapper(modelMapper);

    @SuppressWarnings("unused")
    private final PhoneMapper phoneMapper = new PhoneMapper(modelMapper);

    private final PersonMapper personMapper = new PersonMapper(modelMapper);

    @Test
    public void entityShouldBeMappedToDto() {
        final PersonEntity givenEntity = PersonEntity.builder()
                .id(255L)
                .name("vlad")
                .surname("zuev")
                .patronymic("sergeevich")
                .address(new AddressEntity(256L))
                .phones(List.of(new PhoneEntity(257L), new PhoneEntity(258L)))
                .build();

        final PersonDto actual = personMapper.mapToDto(givenEntity);
        final PersonDto expected = PersonDto.builder()
                .id(255L)
                .description("vlad;zuev;sergeevich")
                .address(new AddressDto(256L))
                .phones(List.of(new PhoneDto(257L), new PhoneDto(258L)))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void nullEntityShouldBeMappedToNullDto() {
        final PersonEntity givenEntity = null;

        final PersonDto actual = personMapper.mapToDto(givenEntity);
        assertNull(actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final PersonDto givenDto = PersonDto.builder()
                .id(255L)
                .description("vlad;zuev;sergeevich")
                .address(new AddressDto(256L))
                .phones(List.of(new PhoneDto(257L), new PhoneDto(258L)))
                .build();

        final PersonEntity actual = personMapper.mapToEntity(givenDto);
        final PersonEntity expected = PersonEntity.builder()
                .id(255L)
                .name("vlad")
                .surname("zuev")
                .patronymic("sergeevich")
                .address(new AddressEntity(256L))
                .phones(List.of(new PhoneEntity(257L), new PhoneEntity(258L)))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void nullDtoShouldBeMappedToNullEntity() {
        final PersonDto givenDto = null;

        final PersonEntity actual = personMapper.mapToEntity(givenDto);
        assertNull(actual);
    }

    @Test
    public void sourceShouldBeMappedToDestination() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        final AddressDto actual = personMapper.map(givenSource, givenDestinationType);
        final AddressDto expected = new AddressDto(255L);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sourceShouldNotBeMappedToDestinationBecauseOfSourceIsNull() {
        final AddressEntity givenSource = null;
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        personMapper.map(givenSource, givenDestinationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sourceShouldNotBeMappedToDestinationBecauseOfDestinationTypeIsNull() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<PersonDto> givenDestinationType = null;

        personMapper.map(givenSource, givenDestinationType);
    }

    @Test(expected = MappingException.class)
    public void sourceShouldNotBeMappedToDestinationBecauseOfNotSuitableDestinationType() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<PersonDto> givenDestinationType = PersonDto.class;

        personMapper.map(givenSource, givenDestinationType);
    }

    @Test
    public void nullableSourceShouldBeMappedToDestination() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        final AddressDto actual = personMapper.mapNullable(givenSource, givenDestinationType);
        final AddressDto expected = new AddressDto(255L);
        assertEquals(expected, actual);
    }

    @Test
    public void nullableSourceShouldBeMappedToNullBecauseOfSourceIsNull() {
        final AddressEntity givenSource = null;
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        final AddressDto actual = personMapper.mapNullable(givenSource, givenDestinationType);
        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullableSourceShouldNotBeMappedToDestinationBecauseOfDestinationTypeIsNull() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<AddressDto> givenDestinationType = null;

        personMapper.mapNullable(givenSource, givenDestinationType);
    }

    @Test(expected = MappingException.class)
    public void nullableSourceShouldNotBeMappedToDestinationBecauseOfNotSuitableDestinationType() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<PersonDto> givenDestinationType = PersonDto.class;

        personMapper.mapNullable(givenSource, givenDestinationType);
    }

    @Test
    public void lazySourceShouldBeMapped() {
        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            final AddressEntity givenSource = new AddressEntity(255L);
            mockedHibernateUtil.when(() -> isFetched(same(givenSource))).thenReturn(true);

            final AddressDto actual = personMapper.mapLazy(givenSource, AddressDto.class);
            final AddressDto expected = new AddressDto(255L);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void lazySourceShouldBeMappedToNull() {
        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            final AddressEntity givenSource = new AddressEntity(255L);
            mockedHibernateUtil.when(() -> isFetched(same(givenSource))).thenReturn(false);

            final AddressDto actual = personMapper.mapLazy(givenSource, AddressDto.class);
            assertNull(actual);
        }
    }

    @Test
    public void lazyCollectionShouldBeMappedToList() {
        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            final Collection<PhoneEntity> givenSource = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
            mockedHibernateUtil.when(() -> isFetched(same(givenSource))).thenReturn(true);

            final List<PhoneDto> actual = personMapper.mapLazyToList(givenSource, PhoneDto.class);
            final List<PhoneDto> expected = List.of(new PhoneDto(257L), new PhoneDto(258L));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void lazyCollectionShouldBeMappedToNullList() {
        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            final List<PhoneEntity> givenSource = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
            mockedHibernateUtil.when(() -> isFetched(same(givenSource))).thenReturn(false);

            final List<PhoneDto> actual = personMapper.mapLazyToList(givenSource, PhoneDto.class);
            assertNull(actual);
        }
    }

    @Test
    public void lazyCollectionShouldBeMappedToMap() {
        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            final Collection<PhoneEntity> givenSource = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
            mockedHibernateUtil.when(() -> isFetched(same(givenSource))).thenReturn(true);

            final Map<Long, PhoneDto> actual = personMapper.mapLazyToMap(givenSource, PhoneDto.class, PhoneDto::getId);
            final Map<Long, PhoneDto> expected = Map.of(
                    257L, new PhoneDto(257L),
                    258L, new PhoneDto(258L)
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    public void lazyCollectionShouldBeMappedToNullMap() {
        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            final List<PhoneEntity> givenSource = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
            mockedHibernateUtil.when(() -> isFetched(same(givenSource))).thenReturn(false);

            final Map<Long, PhoneDto> actual = personMapper.mapLazyToMap(givenSource, PhoneDto.class, PhoneDto::getId);
            assertNull(actual);
        }
    }

    private static void checkEquals(final PersonEntity expected, final PersonEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getPatronymic(), actual.getPatronymic());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getPhones(), actual.getPhones());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    private static final class AddressEntity extends Entity<Long> {
        private Long id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    private static final class PhoneEntity extends Entity<Long> {
        private Long id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    @Builder
    private static final class PersonEntity extends Entity<Long> {
        private Long id;
        private String name;
        private String surname;
        private String patronymic;
        private AddressEntity address;
        private List<PhoneEntity> phones;
    }

    @Value
    private static class AddressDto implements Dto<Long> {
        Long id;
    }

    @Value
    private static class PhoneDto implements Dto<Long> {
        Long id;
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class PersonDto implements Dto<Long> {
        Long id;
        String description;
        AddressDto address;
        List<PhoneDto> phones;
    }

    private static final class PersonMapper extends Mapper<PersonEntity, PersonDto> {
        private static final String DESCRIPTION_COMPONENT_DELIMITER = ";";
        private static final int COMPONENT_INDEX_NAME = 0;
        private static final int COMPONENT_INDEX_SURNAME = 1;
        private static final int COMPONENT_INDEX_PATRONYMIC = 2;

        public PersonMapper(final ModelMapper modelMapper) {
            super(modelMapper, PersonEntity.class, PersonDto.class);
        }

        @Override
        protected PersonDto createDto(final PersonEntity entity) {
            return new PersonDto(
                    entity.id,
                    createDtoDescription(entity),
                    mapLazy(entity.getAddress(), AddressDto.class),
                    mapLazyToList(entity.getPhones(), PhoneDto.class)
            );
        }

        @Override
        protected void mapSpecificFields(final PersonDto source, final PersonEntity destination) {
            final String[] descriptionComponents = findDescriptionComponents(source);
            setName(destination, descriptionComponents);
            setSurname(destination, descriptionComponents);
            setPatronymic(destination, descriptionComponents);
        }

        private static String createDtoDescription(final PersonEntity entity) {
            return join(DESCRIPTION_COMPONENT_DELIMITER, entity.name, entity.surname, entity.patronymic);
        }

        private static String[] findDescriptionComponents(final PersonDto dto) {
            return dto.description.split(DESCRIPTION_COMPONENT_DELIMITER);
        }

        private static void setName(final PersonEntity entity, final String[] descriptionComponents) {
            setDescriptionComponent(
                    entity,
                    PersonEntity::setName,
                    descriptionComponents,
                    COMPONENT_INDEX_NAME
            );
        }

        private static void setSurname(final PersonEntity entity, final String[] descriptionComponents) {
            setDescriptionComponent(
                    entity,
                    PersonEntity::setSurname,
                    descriptionComponents,
                    COMPONENT_INDEX_SURNAME
            );
        }

        private static void setPatronymic(final PersonEntity entity, final String[] descriptionComponents) {
            setDescriptionComponent(
                    entity,
                    PersonEntity::setPatronymic,
                    descriptionComponents,
                    COMPONENT_INDEX_PATRONYMIC
            );
        }

        private static void setDescriptionComponent(final PersonEntity entity,
                                                    final BiConsumer<PersonEntity, String> setter,
                                                    final String[] components,
                                                    final int componentIndex) {
            final String component = components[componentIndex];
            setter.accept(entity, component);
        }
    }

    private static final class AddressMapper extends Mapper<AddressEntity, AddressDto> {

        public AddressMapper(final ModelMapper modelMapper) {
            super(modelMapper, AddressEntity.class, AddressDto.class);
        }

        @Override
        protected AddressDto createDto(final AddressEntity entity) {
            return new AddressDto(entity.id);
        }

        @Override
        protected void mapSpecificFields(final AddressDto source, final AddressEntity destination) {

        }
    }

    private static final class PhoneMapper extends Mapper<PhoneEntity, PhoneDto> {

        public PhoneMapper(final ModelMapper modelMapper) {
            super(modelMapper, PhoneEntity.class, PhoneDto.class);
        }

        @Override
        protected PhoneDto createDto(final PhoneEntity entity) {
            return new PhoneDto(entity.id);
        }

        @Override
        protected void mapSpecificFields(final PhoneDto source, final PhoneEntity destination) {

        }
    }
}
