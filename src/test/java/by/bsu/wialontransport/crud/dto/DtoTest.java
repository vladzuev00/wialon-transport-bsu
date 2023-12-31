package by.bsu.wialontransport.crud.dto;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DtoTest {

    @Test
    public void dtoShouldBeNew() {
        final Dto<Long> givenDto = createDto();
        assertTrue(givenDto.isNew());
    }

    @Test
    public void dtoShouldNotBeNew() {
        final Dto<Long> givenDto = createDto(255L);
        assertFalse(givenDto.isNew());
    }

    private static Dto<Long> createDto(final Long id) {
        return () -> id;
    }

    private static Dto<Long> createDto() {
        return createDto(null);
    }
}
