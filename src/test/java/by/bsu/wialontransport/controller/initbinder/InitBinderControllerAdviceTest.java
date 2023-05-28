package by.bsu.wialontransport.controller.initbinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;

import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class InitBinderControllerAdviceTest {
    private static final String FIELD_NAME_EMPTY_AS_NULL = "emptyAsNull";

    private final InitBinderControllerAdvice advice = new InitBinderControllerAdvice();

    @Captor
    private ArgumentCaptor<StringTrimmerEditor> trimmerEditorArgumentCaptor;

    @Test
    public void initBinderShouldBeConfigured()
            throws Exception {
        final WebDataBinder givenBinder = mock(WebDataBinder.class);

        this.advice.configureInitBinder(givenBinder);

        verify(givenBinder, times(1)).registerCustomEditor(
                same(String.class), this.trimmerEditorArgumentCaptor.capture()
        );

        final StringTrimmerEditor capturedTrimmerEditor = this.trimmerEditorArgumentCaptor.getValue();
        assertTrue(isEmptyAsNull(capturedTrimmerEditor));
    }

    private static boolean isEmptyAsNull(final StringTrimmerEditor trimmerEditor)
            throws Exception {
        final Field field = StringTrimmerEditor.class.getDeclaredField(FIELD_NAME_EMPTY_AS_NULL);
        field.setAccessible(true);
        try {
            return (boolean) field.get(trimmerEditor);
        } finally {
            field.setAccessible(false);
        }
    }

}
