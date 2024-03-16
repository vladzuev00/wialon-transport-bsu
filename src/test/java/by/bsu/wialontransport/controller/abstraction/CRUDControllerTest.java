package by.bsu.wialontransport.controller.abstraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public final class CRUDControllerTest {

    @Mock
    private CRUDService<Long, ?, TestDto, ?, ?> mockedService;

    private TestCRUDController controller;

    @Before
    public void initializeController() {
        controller = new TestCRUDController(mockedService);
    }

    @Test
    public void entityShouldBeFoundById() {
        final Long givenId = 255L;
        final String givenName = "name";

        final TestDto givenDto = new TestDto(givenId, givenName, "password");
        when(mockedService.findById(same(givenId))).thenReturn(Optional.of(givenDto));

        final ResponseEntity<TestResponseView> actual = controller.findById(givenId);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final TestResponseView actualBody = actual.getBody();
        final TestResponseView expectedBody = new TestResponseView(givenId, givenName);
        assertEquals(expectedBody, actualBody);
    }

    @Test(expected = NoSuchEntityException.class)
    public void entityShouldNotBeFoundById() {
        final Long givenId = 255L;

        when(mockedService.findById(same(givenId))).thenReturn(empty());

        controller.findById(givenId);
    }

    @Test
    public void entityShouldBeSaved() {
        final String givenName = "name";
        final String givenPassword = "password";
        final TestSaveView givenView = new TestSaveView(givenName, givenPassword);

        final TestDto expectedDto = createDto(givenName, givenPassword);

        final Long givenId = 255L;
        final TestDto givenSavedDto = new TestDto(givenId, givenName, givenPassword);

        when(mockedService.save(eq(expectedDto))).thenReturn(givenSavedDto);

        final ResponseEntity<TestResponseView> actual = controller.save(givenView);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final TestResponseView actualBody = actual.getBody();
        final TestResponseView expectedBody = new TestResponseView(givenId, givenName);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void entitiesShouldBeSavedAll() {
        final String givenFirstName = "first-name";
        final String givenFirstPassword = "first-password";

        final String givenSecondName = "second-name";
        final String givenSecondPassword = "second-password";

        final List<TestSaveView> givenViews = List.of(
                new TestSaveView(givenFirstName, givenFirstPassword),
                new TestSaveView(givenSecondName, givenSecondPassword)
        );

        final List<TestDto> expectedDtos = List.of(
                createDto(givenFirstName, givenFirstPassword),
                createDto(givenSecondName, givenSecondPassword)
        );

        final Long givenFirstId = 255L;
        final Long givenSecondId = 256L;
        final List<TestDto> givenSavedDtos = List.of(
                new TestDto(givenFirstId, givenFirstName, givenFirstPassword),
                new TestDto(givenSecondId, givenSecondName, givenSecondPassword)
        );

        when(mockedService.saveAll(eq(expectedDtos))).thenReturn(givenSavedDtos);

        final ResponseEntity<List<TestResponseView>> actual = controller.saveAll(givenViews);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final List<TestResponseView> actualBody = actual.getBody();
        final List<TestResponseView> expectedBody = List.of(
                new TestResponseView(givenFirstId, givenFirstName),
                new TestResponseView(givenSecondId, givenSecondName)
        );
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void entityShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenName = "name";
        final String givenPassword = "password";
        final TestUpdateView givenView = new TestUpdateView(givenId, givenName, givenPassword);

        final TestDto expectedDto = new TestDto(givenId, givenName, givenPassword);
        final TestDto givenUpdatedDto = new TestDto(givenId, givenName, givenPassword);
        when(mockedService.update(eq(expectedDto))).thenReturn(givenUpdatedDto);

        final ResponseEntity<TestResponseView> actual = controller.update(givenView);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final TestResponseView actualBody = actual.getBody();
        final TestResponseView expectedBody = new TestResponseView(givenId, givenName);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void entityShouldBeDeletedById() {
        final Long givenId = 255L;

        final ResponseEntity<?> actual = controller.delete(givenId);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(NO_CONTENT, actualStatus);

        verify(mockedService, times(1)).delete(same(givenId));
    }

    private static TestDto createDto(final String name, final String password) {
        return TestDto.builder()
                .name(name)
                .password(password)
                .build();
    }

    private static final class TestCRUDController extends CRUDController<
            Long,
            TestDto,
            CRUDService<Long, ?, TestDto, ?, ?>,
            TestResponseView,
            TestSaveView,
            TestUpdateView
            > {

        public TestCRUDController(final CRUDService<Long, ?, TestDto, ?, ?> service) {
            super(service);
        }

        @Override
        protected TestResponseView createResponseView(final TestDto dto) {
            return new TestResponseView(dto.id, dto.name);
        }
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestDto implements Dto<Long> {
        Long id;
        String name;
        String password;
    }

    @Value
    private static class TestResponseView {
        Long id;
        String name;
    }

    @Value
    private static class TestSaveView implements DtoRequestView<TestDto> {
        String name;
        String password;

        @Override
        public TestDto createDto() {
            return TestDto.builder()
                    .name(name)
                    .password(password)
                    .build();
        }
    }

    @Value
    private static class TestUpdateView implements DtoRequestView<TestDto> {
        Long id;
        String name;
        String password;

        @Override
        public TestDto createDto() {
            return TestDto.builder()
                    .id(id)
                    .name(name)
                    .password(password)
                    .build();
        }
    }
}
