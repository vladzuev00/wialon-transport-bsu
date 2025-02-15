package by.vladzuev.locationreceiver.config;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
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
