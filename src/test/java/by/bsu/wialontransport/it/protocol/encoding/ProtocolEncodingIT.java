package by.bsu.wialontransport.it.protocol.encoding;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.protocol.core.encoder.ProtocolEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@RequiredArgsConstructor
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public abstract class ProtocolEncodingIT extends AbstractSpringBootTest {
    private final ProtocolEncoder encoder;


}
