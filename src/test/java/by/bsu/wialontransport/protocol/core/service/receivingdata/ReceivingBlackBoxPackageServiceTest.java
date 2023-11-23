//package by.bsu.wialontransport.protocol.core.service.receivingdata;
//
//import by.bsu.wialontransport.base.AbstractContextTest;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseBlackBoxPackage;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public final class ReceivingBlackBoxPackageServiceTest extends AbstractContextTest {
//
//    @Autowired
//    private ReceivingBlackBoxPackageService receivingBlackBoxPackageService;
//
//    @Test
//    public void responseShouldBeCreated() {
//        final List<Data> givenData = List.of(
//                createData(), createData(), createData()
//        );
//
//        final WialonResponseBlackBoxPackage actual = this.receivingBlackBoxPackageService.createResponse(givenData);
//        final WialonResponseBlackBoxPackage expected = new WialonResponseBlackBoxPackage(3);
//        assertEquals(expected, actual);
//    }
//
//    private static Data createData() {
//        return Data.builder()
//                .build();
//    }
//}
