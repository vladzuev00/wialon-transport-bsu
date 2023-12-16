package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.util.HibernateUtil;
import lombok.*;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static by.bsu.wialontransport.util.HibernateUtil.isLoaded;
import static java.lang.String.join;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class MapperTest extends AbstractContextTest {
    private final ModelMapper modelMapper = new ModelMapper();

    @SuppressWarnings("unused")
    private final AddressMapper addressMapper = new AddressMapper(this.modelMapper);

    @SuppressWarnings("unused")
    private final PhoneMapper phoneMapper = new PhoneMapper(this.modelMapper);

    private final PersonMapper personMapper = new PersonMapper(this.modelMapper);

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

        final PersonDto actual = this.personMapper.mapToDto(givenEntity);
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

        final PersonDto actual = this.personMapper.mapToDto(givenEntity);
        assertNull(actual);
    }

    @Test
    public void entitiesShouldBeMappedToDtos() {
        final Collection<PersonEntity> givenEntities = List.of(
                PersonEntity.builder()
                        .id(255L)
                        .name("vlad")
                        .surname("zuev")
                        .patronymic("sergeevich")
                        .address(new AddressEntity(256L))
                        .phones(List.of(new PhoneEntity(257L), new PhoneEntity(258L)))
                        .build(),
                PersonEntity.builder()
                        .id(259L)
                        .name("pavel")
                        .surname("provashinskiy")
                        .patronymic("sergeevich")
                        .address(new AddressEntity(260L))
                        .phones(List.of(new PhoneEntity(261L), new PhoneEntity(262L)))
                        .build()
        );

        final List<PersonDto> actual = this.personMapper.mapToDtos(givenEntities);
        final List<PersonDto> expected = List.of(
                PersonDto.builder()
                        .id(255L)
                        .description("vlad;zuev;sergeevich")
                        .address(new AddressDto(256L))
                        .phones(List.of(new PhoneDto(257L), new PhoneDto(258L)))
                        .build(),
                PersonDto.builder()
                        .id(259L)
                        .description("pavel;provashinskiy;sergeevich")
                        .address(new AddressDto(260L))
                        .phones(List.of(new PhoneDto(261L), new PhoneDto(262L)))
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void nullEntitiesShouldBeMappedToNullDtos() {
        final Collection<PersonEntity> givenEntities = null;

        final List<PersonDto> actual = this.personMapper.mapToDtos(givenEntities);
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

        final PersonEntity actual = this.personMapper.mapToEntity(givenDto);
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

        final PersonEntity actual = this.personMapper.mapToEntity(givenDto);
        assertNull(actual);
    }

    @Test
    public void dtosShouldBeMappedToEntities() {
        final Collection<PersonDto> givenDtos = List.of(
                PersonDto.builder()
                        .id(255L)
                        .description("vlad;zuev;sergeevich")
                        .address(new AddressDto(256L))
                        .phones(List.of(new PhoneDto(257L), new PhoneDto(258L)))
                        .build(),
                PersonDto.builder()
                        .id(259L)
                        .description("pavel;provashinskiy;sergeevich")
                        .address(new AddressDto(260L))
                        .phones(List.of(new PhoneDto(261L), new PhoneDto(262L)))
                        .build()
        );

        final List<PersonEntity> actual = this.personMapper.mapToEntities(givenDtos);
        final List<PersonEntity> expected = List.of(
                PersonEntity.builder()
                        .id(255L)
                        .name("vlad")
                        .surname("zuev")
                        .patronymic("sergeevich")
                        .address(new AddressEntity(256L))
                        .phones(List.of(new PhoneEntity(257L), new PhoneEntity(258L)))
                        .build(),
                PersonEntity.builder()
                        .id(259L)
                        .name("pavel")
                        .surname("provashinskiy")
                        .patronymic("sergeevich")
                        .address(new AddressEntity(260L))
                        .phones(List.of(new PhoneEntity(261L), new PhoneEntity(262L)))
                        .build()
        );
        checkEquals(expected, actual);
    }

    @Test
    public void nullDtosShouldBeMappedToNullEntities() {
        final Collection<PersonDto> givenDtos = null;

        final List<PersonEntity> actual = this.personMapper.mapToEntities(givenDtos);
        assertNull(actual);
    }

    @Test
    public void sourceShouldBeMappedToDestination() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        final AddressDto actual = this.personMapper.map(givenSource, givenDestinationType);
        final AddressDto expected = new AddressDto(255L);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sourceShouldNotBeMappedToDestinationBecauseOfSourceIsNull() {
        final AddressEntity givenSource = null;
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        this.personMapper.map(givenSource, givenDestinationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sourceShouldNotBeMappedToDestinationBecauseOfDestinationTypeIsNull() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<PersonDto> givenDestinationType = null;

        this.personMapper.map(givenSource, givenDestinationType);
    }

    @Test
    public void nullableSourceShouldBeMappedToDestination() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        final AddressDto actual = this.personMapper.mapNullable(givenSource, givenDestinationType);
        final AddressDto expected = new AddressDto(255L);
        assertEquals(expected, actual);
    }

    @Test
    public void nullableSourceShouldBeMappedToNullBecauseOfSourceIsNull() {
        final AddressEntity givenSource = null;
        final Class<AddressDto> givenDestinationType = AddressDto.class;

        final AddressDto actual = this.personMapper.mapNullable(givenSource, givenDestinationType);
        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullableSourceShouldNotBeMappedToDestinationBecauseOfDestinationTypeIsNull() {
        final AddressEntity givenSource = new AddressEntity(255L);
        final Class<AddressDto> givenDestinationType = null;

        this.personMapper.mapNullable(givenSource, givenDestinationType);
    }

    @Test
    public void lazyPropertyShouldBeMapped() {
        final AddressEntity givenProperty = new AddressEntity(255L);
        final PersonEntity givenEntity = PersonEntity.builder()
                .address(givenProperty)
                .build();

        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            mockedHibernateUtil.when(() -> isLoaded(same(givenProperty))).thenReturn(true);

            final AddressDto actual = this.personMapper.mapLazyProperty(
                    givenEntity,
                    PersonEntity::getAddress,
                    AddressDto.class
            );
            final AddressDto expected = new AddressDto(255L);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void lazyPropertyShouldBeMappedToNull() {
        final AddressEntity givenProperty = new AddressEntity(255L);
        final PersonEntity givenEntity = PersonEntity.builder()
                .address(givenProperty)
                .build();

        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            mockedHibernateUtil.when(() -> isLoaded(same(givenProperty))).thenReturn(false);

            final AddressDto actual = this.personMapper.mapLazyProperty(
                    givenEntity,
                    PersonEntity::getAddress,
                    AddressDto.class
            );
            assertNull(actual);
        }
    }

    @Test
    public void lazyCollectionPropertyShouldBeMappedToList() {
        final List<PhoneEntity> givenProperty = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
        final PersonEntity givenEntity = PersonEntity.builder()
                .phones(givenProperty)
                .build();

        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            mockedHibernateUtil.when(() -> isLoaded(same(givenProperty))).thenReturn(true);

            final List<PhoneDto> actual = this.personMapper.mapLazyCollectionPropertyToList(
                    givenEntity,
                    PersonEntity::getPhones,
                    PhoneDto.class
            );
            final List<PhoneDto> expected = List.of(new PhoneDto(257L), new PhoneDto(258L));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void lazyCollectionPropertyShouldBeMappedToNullList() {
        final List<PhoneEntity> givenProperty = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
        final PersonEntity givenEntity = PersonEntity.builder()
                .phones(givenProperty)
                .build();

        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            mockedHibernateUtil.when(() -> isLoaded(same(givenProperty))).thenReturn(false);

            final List<PhoneDto> actual = this.personMapper.mapLazyCollectionPropertyToList(
                    givenEntity,
                    PersonEntity::getPhones,
                    PhoneDto.class
            );
            assertNull(actual);
        }
    }

    @Test
    public void lazyCollectionPropertyShouldBeMappedToMap() {
        final List<PhoneEntity> givenProperty = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
        final PersonEntity givenEntity = PersonEntity.builder()
                .phones(givenProperty)
                .build();

        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            mockedHibernateUtil.when(() -> isLoaded(same(givenProperty))).thenReturn(true);

            final Map<Long, PhoneDto> actual = this.personMapper.mapLazyCollectionPropertyToMap(
                    givenEntity,
                    PersonEntity::getPhones,
                    PhoneDto.class,
                    PhoneDto::getId
            );
            final Map<Long, PhoneDto> expected = Map.of(
                    257L, new PhoneDto(257L),
                    258L, new PhoneDto(258L)
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    public void lazyCollectionPropertyShouldBeMappedToNullMap() {
        final List<PhoneEntity> givenProperty = List.of(new PhoneEntity(257L), new PhoneEntity(258L));
        final PersonEntity givenEntity = PersonEntity.builder()
                .phones(givenProperty)
                .build();

        try (final MockedStatic<HibernateUtil> mockedHibernateUtil = mockStatic(HibernateUtil.class)) {
            mockedHibernateUtil.when(() -> isLoaded(same(givenProperty))).thenReturn(false);

            final Map<Long, PhoneDto> actual = this.personMapper.mapLazyCollectionPropertyToMap(
                    givenEntity,
                    PersonEntity::getPhones,
                    PhoneDto.class,
                    PhoneDto::getId
            );
            assertNull(actual);
        }
    }

    @Test
    public void propertyShouldBeMappedAndSet() {
        final PersonDto givenSource = PersonDto.builder()
                .id(255L)
                .build();
        final PersonEntity givenEntity = new PersonEntity();

        this.personMapper.mapPropertyAndSet(givenSource, PersonDto::getId, givenEntity, PersonEntity::setId);

        final PersonEntity expected = PersonEntity.builder()
                .id(255L)
                .build();
        checkEquals(expected, givenEntity);
    }

    private static void checkEquals(final PersonEntity expected, final PersonEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getPatronymic(), actual.getPatronymic());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getPhones(), actual.getPhones());
    }

    private static void checkEquals(final List<PersonEntity> expected, final List<PersonEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
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
                    super.mapLazyProperty(entity, PersonEntity::getAddress, AddressDto.class),
                    super.mapLazyCollectionPropertyToList(entity, PersonEntity::getPhones, PhoneDto.class)
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
