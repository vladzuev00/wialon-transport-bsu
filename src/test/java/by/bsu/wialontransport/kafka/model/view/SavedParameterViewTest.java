package by.bsu.wialontransport.kafka.model.view;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.kafka.model.view.SavedParameterView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.junit.Assert.assertEquals;

public final class SavedParameterViewTest extends AbstractContextTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void viewShouldBeConvertedToJson()
            throws Exception {
        final SavedParameterView givenView = SavedParameterView.builder()
                .name("parameter-name")
                .type(INTEGER)
                .value("1")
                .id(255L)
                .build();

        final String actual = objectMapper.writeValueAsString(givenView);
        final String expected = """
                {
                  "name": "parameter-name",
                  "type": "INTEGER",
                  "value": "1",
                  "id": 255
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
                  "value": "1",
                  "id": 255
                }""";

        final SavedParameterView actual = objectMapper.readValue(givenJson, SavedParameterView.class);
        final SavedParameterView expected = SavedParameterView.builder()
                .name("parameter-name")
                .type(INTEGER)
                .value("1")
                .id(255L)
                .build();
        assertEquals(expected, actual);
    }
}
