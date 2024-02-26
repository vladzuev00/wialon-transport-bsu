package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import lombok.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class CRUDServiceTest {

    @Mock
    private Mapper<TestPersonEntity, TestPersonDto> mockedMapper;

    @Mock
    private PersonRepository mockedRepository;

    private TestPersonService service;

    @Before
    public void initializeService() {
        service = new TestPersonService(mockedMapper, mockedRepository);
    }

    @Test
    public void personShouldBeFoundById() {
        final Long givenId = 255L;

        final TestPersonEntity givenEntity = new TestPersonEntity(givenId);
        when(mockedRepository.findById(same(givenId))).thenReturn(Optional.of(givenEntity));

        final TestPersonDto givenDto = new TestPersonDto(givenId);
        when(mockedMapper.mapToDto(same(givenEntity))).thenReturn(givenDto);

        final Optional<TestPersonDto> optionalActual = service.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final TestPersonDto actual = optionalActual.get();
        assertSame(givenDto, actual);
    }

    @Test
    public void personShouldNotBeFoundById() {
        final Long givenId = 255L;

        when(mockedRepository.findById(same(givenId))).thenReturn(empty());

        final Optional<TestPersonDto> optionalActual = service.findById(givenId);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(mockedMapper);
    }

    @Test
    public void personsShouldBeFoundByIds() {
        final Iterable<Long> givenIds = List.of(255L, 256L);

        final TestPersonEntity firstGivenEntity = new TestPersonEntity(255L);
        final TestPersonEntity secondGivenEntity = new TestPersonEntity(256L);
        final List<TestPersonEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
        when(mockedRepository.findAllById(same(givenIds))).thenReturn(givenEntities);

        final TestPersonDto firstGivenDto = new TestPersonDto(255L);
        when(mockedMapper.mapToDto(same(firstGivenEntity))).thenReturn(firstGivenDto);

        final TestPersonDto secondGivenDto = new TestPersonDto(256L);
        when(mockedMapper.mapToDto(same(secondGivenEntity))).thenReturn(secondGivenDto);

        final List<TestPersonDto> actual = service.findByIds(givenIds);
        final List<TestPersonDto> expected = List.of(firstGivenDto, secondGivenDto);
        assertEquals(expected, actual);
    }

    @Test
    public void personsShouldNotBeFoundByIds() {
        final Iterable<Long> givenIds = List.of(255L, 256L);

        when(mockedRepository.findAllById(same(givenIds))).thenReturn(emptyList());

        final List<TestPersonDto> actual = service.findByIds(givenIds);
        assertTrue(actual.isEmpty());

        verifyNoInteractions(mockedMapper);
    }

    @Test
    public void personShouldExistById() {
        final Long givenId = 255L;

        when(mockedRepository.existsById(same(givenId))).thenReturn(true);

        final boolean exists = service.isExist(givenId);
        assertTrue(exists);
    }

    @Test
    public void personShouldNotExistById() {
        final Long givenId = 255L;

        when(mockedRepository.existsById(same(givenId))).thenReturn(false);

        final boolean exists = service.isExist(givenId);
        assertFalse(exists);
    }

    @Test
    public void personShouldBeUpdated() {
        final TestPersonDto givenInitialDto = new TestPersonDto(255L);

        final TestPersonEntity givenInitialEntity = new TestPersonEntity(255L);
        when(mockedMapper.mapToEntity(same(givenInitialDto))).thenReturn(givenInitialEntity);

        final TestPersonEntity givenUpdatedEntity = new TestPersonEntity(256L);
        when(mockedRepository.save(same(givenInitialEntity))).thenReturn(givenUpdatedEntity);

        final TestPersonDto givenUpdatedDto = new TestPersonDto(256L);
        when(mockedMapper.mapToDto(same(givenUpdatedEntity))).thenReturn(givenUpdatedDto);

        final TestPersonDto actual = service.update(givenInitialDto);
        assertSame(givenUpdatedDto, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void personWithNullIdShouldNotBeUpdated() {
        final TestPersonDto givenInitialDto = new TestPersonDto(null);

        service.update(givenInitialDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void personWithZeroIdShouldNotBeUpdated() {
        final TestPersonDto givenInitialDto = new TestPersonDto(0L);

        service.update(givenInitialDto);
    }

    @Test
    public void personShouldBeDeletedById() {
        final Long givenId = 255L;

        service.delete(givenId);

        verify(mockedRepository, times(1)).deleteById(same(givenId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void personShouldNotBeDeletedByNullId() {
        final Long givenId = null;

        service.delete(givenId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void personShouldNotBeDeletedByZeroId() {
        final Long givenId = 0L;

        service.delete(givenId);
    }

    @Test
    public void personShouldBeSaved() {
        final TestPersonDto givenDto = new TestPersonDto(0L);

        final TestPersonEntity givenEntity = new TestPersonEntity(0L);
        when(mockedMapper.mapToEntity(same(givenDto))).thenReturn(givenEntity);

        final TestPersonEntity givenSavedEntity = new TestPersonEntity(256L);
        when(mockedRepository.save(same(givenEntity))).thenReturn(givenSavedEntity);

        final TestPersonDto givenSavedDto = new TestPersonDto(256L);
        when(mockedMapper.mapToDto(same(givenSavedEntity))).thenReturn(givenSavedDto);

        final TestPersonDto actual = service.save(givenDto);
        assertSame(givenSavedDto, actual);

        final List<TestPersonEntity> expectedConfiguredEntities = List.of(givenEntity);
        assertEquals(expectedConfiguredEntities, service.configuredBeforeSavingEntities);
    }

    @Test
    public void dtosShouldBeSaved() {
        final TestPersonDto firstGivenDto = new TestPersonDto(0L);
        final TestPersonDto secondGivenDto = new TestPersonDto(1L);
        final List<TestPersonDto> givenDtos = List.of(firstGivenDto, secondGivenDto);

        final TestPersonEntity firstGivenEntity = new TestPersonEntity(0L);
        when(mockedMapper.mapToEntity(same(firstGivenDto))).thenReturn(firstGivenEntity);

        final TestPersonEntity secondGivenEntity = new TestPersonEntity(1L);
        when(mockedMapper.mapToEntity(same(secondGivenDto))).thenReturn(secondGivenEntity);

        final List<TestPersonEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);

        final TestPersonEntity firstGivenSavedEntity = new TestPersonEntity(256L);
        final TestPersonEntity secondGivenSavedEntity = new TestPersonEntity(257L);
        final List<TestPersonEntity> givenSavedEntities = List.of(firstGivenSavedEntity, secondGivenSavedEntity);
        when(mockedRepository.saveAll(eq(givenEntities))).thenReturn(givenSavedEntities);

        final TestPersonDto firstGivenSavedDto = new TestPersonDto(256L);
        when(mockedMapper.mapToDto(same(firstGivenSavedEntity))).thenReturn(firstGivenSavedDto);

        final TestPersonDto secondGivenSavedDto = new TestPersonDto(257L);
        when(mockedMapper.mapToDto(same(secondGivenSavedEntity))).thenReturn(secondGivenSavedDto);

        final List<TestPersonDto> actual = service.saveAll(givenDtos);
        final List<TestPersonDto> expected = List.of(firstGivenSavedDto, secondGivenSavedDto);
        assertEquals(expected, actual);

        assertEquals(givenEntities, service.configuredBeforeSavingEntities);
    }

    @Test
    public void uniquePersonShouldBeFound() {
        final Long givenId = 255L;
        final Function<PersonRepository, Optional<TestPersonEntity>> givenOperation = repository -> repository.findById(
                givenId
        );

        final TestPersonEntity givenEntity = new TestPersonEntity(givenId);
        when(mockedRepository.findById(same(givenId))).thenReturn(Optional.of(givenEntity));

        final TestPersonDto givenDto = new TestPersonDto(givenId);
        when(mockedMapper.mapToDto(same(givenEntity))).thenReturn(givenDto);

        final Optional<TestPersonDto> optionalActual = service.findUniqueDto(givenOperation);
        assertTrue(optionalActual.isPresent());
        final TestPersonDto actual = optionalActual.get();
        assertSame(givenDto, actual);
    }

    @Test
    public void uniquePersonShouldNotBeFound() {
        final Long givenId = 255L;
        final Function<PersonRepository, Optional<TestPersonEntity>> givenOperation = repository -> repository.findById(
                givenId
        );

        when(mockedRepository.findById(same(givenId))).thenReturn(empty());

        final Optional<TestPersonDto> optionalActual = service.findUniqueDto(givenOperation);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(mockedMapper);
    }

    @Test
    public void personNameShouldBeFoundById() {
        final Long givenId = 255L;
        final Function<PersonRepository, Optional<String>> givenOperation = repository -> repository.findNameById(
                givenId
        );

        final String expectedName = "Ivan";
        when(mockedRepository.findNameById(same(givenId))).thenReturn(Optional.of(expectedName));

        final Optional<String> optionalActual = service.execute(givenOperation);
        assertTrue(optionalActual.isPresent());
        final String actual = optionalActual.get();
        assertSame(expectedName, actual);

        verifyNoInteractions(mockedMapper);
    }

    @Test
    public void personNameShouldNotBeFoundById() {
        final Long givenId = 255L;
        final Function<PersonRepository, Optional<String>> givenOperation = repository -> repository.findNameById(
                givenId
        );

        when(mockedRepository.findNameById(same(givenId))).thenReturn(empty());

        final Optional<String> optionalActual = service.execute(givenOperation);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void booleanShouldBeFound() {
        final Long givenId = 255L;
        final Predicate<PersonRepository> givenOperation = repository -> repository.existsById(givenId);

        final boolean givenValue = true;
        when(mockedRepository.existsById(same(givenId))).thenReturn(givenValue);

        final boolean actual = service.findBoolean(givenOperation);
        assertEquals(givenValue, actual);
    }

    @Test
    public void intShouldBeFound() {
        final String givenNewName = "new-name";
        final ToIntFunction<PersonRepository> givenOperation = repository -> repository.updateName(givenNewName);

        final int givenInt = 5;
        when(mockedRepository.updateName(same(givenNewName))).thenReturn(givenInt);

        final int actual = service.findInt(givenOperation);
        assertEquals(givenInt, actual);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void entityStreamShouldBeFoundAndCollect() {
        final String givenName = "name";
        final Function<PersonRepository, Stream<TestPersonEntity>> givenOperation = repository -> repository.findByName(
                givenName
        );
        final Collector<TestPersonEntity, ?, List<TestPersonEntity>> givenCollector = toList();

        final Stream<TestPersonEntity> givenStream = mock(Stream.class);
        when(mockedRepository.findByName(same(givenName))).thenReturn(givenStream);

        final TestPersonEntity firstGivenEntity = new TestPersonEntity(255L);
        final TestPersonEntity secondGivenEntity = new TestPersonEntity(256L);
        final List<TestPersonEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
        when(givenStream.collect(same(givenCollector))).thenReturn(givenEntities);

        final List<TestPersonEntity> actual = service.findEntityStreamAndCollect(givenOperation, givenCollector);
        assertSame(givenEntities, actual);

        verify(givenStream, times(1)).close();
    }

    @Test
    public void dtoStreamShouldBeFound() {
        final String givenName = "name";
        final Function<PersonRepository, Stream<TestPersonEntity>> givenOperation = repository -> repository.findByName(
                givenName
        );

        final TestPersonEntity firstGivenEntity = new TestPersonEntity(255L);
        final TestPersonEntity secondGivenEntity = new TestPersonEntity(256L);
        final Stream<TestPersonEntity> givenEntities = Stream.of(firstGivenEntity, secondGivenEntity);
        when(mockedRepository.findByName(same(givenName))).thenReturn(givenEntities);

        final TestPersonDto firstGivenDto = new TestPersonDto(255L);
        when(mockedMapper.mapToDto(same(firstGivenEntity))).thenReturn(firstGivenDto);

        final TestPersonDto secondGivenDto = new TestPersonDto(256L);
        when(mockedMapper.mapToDto(same(secondGivenEntity))).thenReturn(secondGivenDto);

        final Stream<TestPersonDto> actual = service.findDtoStream(givenOperation);
        final Set<TestPersonDto> actualAsSet = actual.collect(toUnmodifiableSet());
        final Set<TestPersonDto> expectedAsSet = Set.of(firstGivenDto, secondGivenDto);
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void dtoPageShouldBeFound() {
        final String givenSurnameName = "surname";
        final Function<PersonRepository, Page<TestPersonEntity>> givenOperation = repository -> repository.findBySurname(
                givenSurnameName
        );

        final TestPersonEntity firstGivenEntity = new TestPersonEntity(255L);
        final TestPersonEntity secondGivenEntity = new TestPersonEntity(256L);
        final List<TestPersonEntity> givenEntities = List.of(firstGivenEntity, secondGivenEntity);
        final Page<TestPersonEntity> givenPage = new PageImpl<>(givenEntities);
        when(mockedRepository.findBySurname(same(givenSurnameName))).thenReturn(givenPage);

        final TestPersonDto firstGivenDto = new TestPersonDto(255L);
        when(mockedMapper.mapToDto(same(firstGivenEntity))).thenReturn(firstGivenDto);

        final TestPersonDto secondGivenDto = new TestPersonDto(256L);
        when(mockedMapper.mapToDto(same(secondGivenEntity))).thenReturn(secondGivenDto);

        final Page<TestPersonDto> actual = service.findDtoPage(givenOperation);
        final List<TestPersonDto> actualAsList = actual.toList();
        final List<TestPersonDto> expectedAsList = List.of(firstGivenDto, secondGivenDto);
        assertEquals(expectedAsList, actualAsList);
    }

    private static final class TestPersonService extends CRUDService<
            Long,
            TestPersonEntity,
            TestPersonDto,
            Mapper<TestPersonEntity, TestPersonDto>,
            PersonRepository
            > {
        private final List<TestPersonEntity> configuredBeforeSavingEntities;

        public TestPersonService(final Mapper<TestPersonEntity, TestPersonDto> mapper, final PersonRepository repository) {
            super(mapper, repository);
            configuredBeforeSavingEntities = new ArrayList<>();
        }

        @Override
        protected void configureBeforeSave(final TestPersonEntity entity) {
            configuredBeforeSavingEntities.add(entity);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    private static final class TestPersonEntity extends Entity<Long> {
        private Long id;
    }

    @Value
    private static class TestPersonDto implements Dto<Long> {
        Long id;
    }

    private interface PersonRepository extends JpaRepository<TestPersonEntity, Long> {
        int updateName(final String newName);

        Stream<TestPersonEntity> findByName(final String name);

        Page<TestPersonEntity> findBySurname(final String surname);

        Optional<String> findNameById(final Long id);
    }
}
