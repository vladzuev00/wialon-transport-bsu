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

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
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

        final ResponseEntity<TestDtoResponseView> actual = controller.findById(givenId);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final TestDtoResponseView actualBody = actual.getBody();
        final TestDtoResponseView expectedBody = new TestDtoResponseView(givenId, givenName);
        assertEquals(expectedBody, actualBody);
    }

    @Test(expected = NoSuchEntityException.class)
    public void entityShouldNotBeFoundById() {
        final Long givenId = 255L;

        when(mockedService.findById(same(givenId))).thenReturn(empty());

        controller.findById(givenId);
    }

    private static final class TestCRUDController extends CRUDController<
            Long,
            TestDto,
            CRUDService<Long, ?, TestDto, ?, ?>,
            TestDtoResponseView,
            TestSaveView,
            TestUpdateView
            > {

        public TestCRUDController(final CRUDService<Long, ?, TestDto, ?, ?> service) {
            super(service);
        }

        @Override
        protected TestDtoResponseView createResponseView(final TestDto dto) {
            return new TestDtoResponseView(dto.id, dto.name);
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
    private static class TestDtoResponseView {
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
