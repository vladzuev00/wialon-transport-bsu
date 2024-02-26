package by.bsu.wialontransport.controller.initbinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;

import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ConfiguringBinderControllerAdviceTest {
    private static final String FIELD_NAME_EMPTY_AS_NULL = "emptyAsNull";

    private final ConfiguringBinderControllerAdvice advice = new ConfiguringBinderControllerAdvice();

    @Captor
    private ArgumentCaptor<StringTrimmerEditor> editorArgumentCaptor;

    @Test
    public void binderShouldBeConfigured() {
        final WebDataBinder givenBinder = mock(WebDataBinder.class);

        advice.configureBinder(givenBinder);

        verify(givenBinder, times(1)).registerCustomEditor(
                same(String.class),
                editorArgumentCaptor.capture()
        );

        final StringTrimmerEditor capturedEditor = editorArgumentCaptor.getValue();
        assertTrue(isEmptyAsNull(capturedEditor));
    }

    private static boolean isEmptyAsNull(final StringTrimmerEditor editor) {
        return findProperty(editor, FIELD_NAME_EMPTY_AS_NULL, Boolean.class);
    }

}
