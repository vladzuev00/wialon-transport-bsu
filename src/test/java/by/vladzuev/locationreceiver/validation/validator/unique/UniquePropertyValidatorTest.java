//package by.vladzuev.locationreceiver.validation.validator.unique;
//
//import by.vladzuev.locationreceiver.controller.abstraction.View;
//import by.vladzuev.locationreceiver.crud.dto.Dto;
//import by.vladzuev.locationreceiver.crud.entity.AbstractEntity;
//import by.vladzuev.locationreceiver.crud.mapper.Mapper;
//import by.vladzuev.locationreceiver.crud.service.CRUDService;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Value;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import javax.validation.ConstraintValidatorContext;
//import java.lang.annotation.Annotation;
//import java.util.Optional;
//
//import static java.util.Optional.empty;
//import static java.util.Optional.ofNullable;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class UniquePropertyValidatorTest {
//
//    @Mock
//    private TestCRUDService mockedCRUDService;
//
//    private TestUniquePropertyValidator validator;
//
//    @Before
//    public void initializeValidator() {
//        validator = new TestUniquePropertyValidator(mockedCRUDService);
//    }
//
//    @Test
//    public void valueShouldBeValidInCaseExistingDtoWithSamePropertyAndId() {
//        final Long givenId = 255L;
//        final String givenName = "name";
//        final TestView givenView = new TestView(givenId, givenName);
//
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        final TestDto givenDto = new TestDto(givenId, givenName);
//        when(mockedCRUDService.findByName(same(givenName))).thenReturn(Optional.of(givenDto));
//
//        final boolean actual = validator.isValid(givenView, givenContext);
//        final boolean expected = true;
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    @Test
//    public void valueShouldBeValidBecauseOfDtoWithSamePropertyNotExist() {
//        final String givenName = "name";
//        final TestView givenView = new TestView(255L, givenName);
//
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        when(mockedCRUDService.findByName(same(givenName))).thenReturn(empty());
//
//        final boolean actual = validator.isValid(givenView, givenContext);
//        final boolean expected = true;
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    @Test
//    public void valueShouldNotBeValidBecauseViewWithoutIdDuplicateDtoWithSameProperty() {
//        final String givenName = "name";
//        final TestView givenView = createView(givenName);
//
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        final TestDto givenDto = new TestDto(255L, givenName);
//        when(mockedCRUDService.findByName(same(givenName))).thenReturn(Optional.of(givenDto));
//
//        final boolean actual = validator.isValid(givenView, givenContext);
//        final boolean expected = false;
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    @Test
//    public void valueShouldNotBeValidBecauseOfViewWithDefinedIdDuplicateDto() {
//        final String givenName = "name";
//        final TestView givenView = new TestView(255L, givenName);
//
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        final TestDto givenDto = new TestDto(256L, givenName);
//        when(mockedCRUDService.findByName(same(givenName))).thenReturn(Optional.of(givenDto));
//
//        final boolean actual = validator.isValid(givenView, givenContext);
//        final boolean expected = false;
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    @Test
//    public void valueShouldNotBeValidBecauseOfViewWithNotDefinedIdDuplicateDto() {
//        final String givenName = "name";
//        final TestView givenView = createView(givenName);
//
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        final TestDto givenDto = new TestDto(255L, givenName);
//        when(mockedCRUDService.findByName(same(givenName))).thenReturn(Optional.of(givenDto));
//
//        final boolean actual = validator.isValid(givenView, givenContext);
//        final boolean expected = false;
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static TestView createView(final String name) {
//        return TestView.builder()
//                .name(name)
//                .build();
//    }
//
//    @Value
//    private static class TestDto implements Dto<Long> {
//        Long id;
//        String name;
//    }
//
//    @Value
//    @AllArgsConstructor
//    @Builder
//    private static class TestView implements View<Long> {
//        Long id;
//        String name;
//
//        @Override
//        public Optional<Long> findId() {
//            return ofNullable(id);
//        }
//    }
//
//    private static final class TestCRUDService extends CRUDService<Long, AbstractEntity<Long>, TestDto, Mapper<AbstractEntity<Long>, TestDto>, JpaRepository<AbstractEntity<Long>, Long>> {
//
//        public TestCRUDService() {
//            super(null, null);
//        }
//
//        public Optional<TestDto> findByName(final String ignored) {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        protected void configureBeforeSave(final AbstractEntity<Long> entity) {
//            throw new UnsupportedOperationException();
//        }
//    }
//
//    private static final class TestUniquePropertyValidator extends UniquePropertyValidator<Annotation, TestDto, TestView, String, TestCRUDService> {
//
//        public TestUniquePropertyValidator(final TestCRUDService service) {
//            super(service);
//        }
//
//        @Override
//        protected String getProperty(final TestView view) {
//            return view.getName();
//        }
//
//        @Override
//        protected Optional<TestDto> findByProperty(final String name, final TestCRUDService service) {
//            return service.findByName(name);
//        }
//    }
//}
