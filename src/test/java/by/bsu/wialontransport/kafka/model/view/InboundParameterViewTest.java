package by.bsu.wialontransport.kafka.model.view;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.junit.Assert.assertEquals;

public final class InboundParameterViewTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void viewShouldBeConvertedToJson()
            throws Exception {
        final InboundParameterView givenView = InboundParameterView.builder()
                .name("parameter-name")
                .type(INTEGER)
                .value("1")
                .build();

        final String actual = objectMapper.writeValueAsString(givenView);
        final String expected = """
                {
                  "name": "parameter-name",
                  "type": "INTEGER",
                  "value": "1"
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToView()
            throws Exception {
        final String givenJson = """
                {
                  "name": "parameter-name",
                  "type": "INTEGER",
                  "value": "1"
                }""";

        final InboundParameterView actual = objectMapper.readValue(givenJson, InboundParameterView.class);
        final InboundParameterView expected = InboundParameterView.builder()
                .name("parameter-name")
                .type(INTEGER)
                .value("1")
                .build();
        assertEquals(expected, actual);
    }
}
