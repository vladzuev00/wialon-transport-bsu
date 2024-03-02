package by.bsu.wialontransport.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class WebMvcConfigTest {
    private final WebMvcConfig config = new WebMvcConfig();

    @Mock
    private ViewControllerRegistry mockedRegistry;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    public void viewControllersShouldBeAdded() {
        final ViewControllerRegistration givenControllerRegistration = mock(ViewControllerRegistration.class);
        when(mockedRegistry.addViewController(anyString())).thenReturn(givenControllerRegistration);

        config.addViewControllers(mockedRegistry);

        verify(mockedRegistry, times(1)).addViewController(stringArgumentCaptor.capture());
        verify(givenControllerRegistration, times(1)).setViewName(stringArgumentCaptor.capture());

        final List<String> actualCapturedStrings = stringArgumentCaptor.getAllValues();
        final List<String> expectedCapturedStrings = List.of("/login", "login");
        assertEquals(expectedCapturedStrings, actualCapturedStrings);
    }
}
