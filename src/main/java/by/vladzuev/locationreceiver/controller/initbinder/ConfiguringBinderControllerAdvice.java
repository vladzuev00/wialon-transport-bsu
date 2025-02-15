package by.vladzuev.locationreceiver.controller.initbinder;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public final class ConfiguringBinderControllerAdvice {

    @InitBinder
    public void configureBinder(final WebDataBinder binder) {
        final StringTrimmerEditor editor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, editor);
    }
}
