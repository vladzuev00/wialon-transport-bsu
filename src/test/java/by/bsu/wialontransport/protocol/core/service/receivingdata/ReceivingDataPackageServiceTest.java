package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertEquals;

public final class ReceivingDataPackageServiceTest extends AbstractContextTest {

    @Autowired
    private ReceivingDataPackageService receivingDataPackageService;

    @Test
    public void successResponseShouldBeCreated() {
        final List<Data> givenData = List.of(createData());

        final ResponseDataPackage actual = this.receivingDataPackageService.createResponse(givenData);
        final ResponseDataPackage expected = new ResponseDataPackage(PACKAGE_FIX_SUCCESS);
        assertEquals(expected, actual);
    }

    private static Data createData() {
        return Data.builder()
                .build();
    }
}
