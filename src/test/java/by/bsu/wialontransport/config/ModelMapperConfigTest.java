package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public final class ModelMapperConfigTest extends AbstractSpringBootTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void modelMapperShouldBeCreated() {
        assertNotNull(modelMapper);
    }
}
