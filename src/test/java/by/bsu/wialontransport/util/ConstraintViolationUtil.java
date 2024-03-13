package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;

import javax.validation.ConstraintViolation;
import java.util.Set;

//TODO: use everywhere
@UtilityClass
public final class ConstraintViolationUtil {

    public static <T> String findFirstMessage(final Set<ConstraintViolation<T>> violations) {
        return violations.iterator().next().getMessage();
    }
}
