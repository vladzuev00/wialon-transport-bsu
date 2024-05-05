//package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;
//
//import by.bsu.wialontransport.protocol.newwing.model.NewWingData;
//import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingDataPackage;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public final class NewWingDataPackageBuilderTest {
//
//    @Test
//    public void packageShouldBeBuilt() {
//        final NewWingDataPackageBuilder givenBuilder = new NewWingDataPackageBuilder();
//
//        final List<NewWingData> givenData = List.of(
//                createData(3, 11, 23),
//                createData(4, 10, 24),
//                createData(3, 11, 25)
//        );
//        givenBuilder.setData(givenData);
//
//        final int givenChecksum = 12345;
//
//        final NewWingDataPackage actual = givenBuilder.build(givenChecksum);
//        final NewWingDataPackage expected = new NewWingDataPackage(givenChecksum, givenData);
//        assertEquals(expected, actual);
//    }
//
//    private static NewWingData createData(final int day, final int month, final int year) {
//        return NewWingData.builder()
//                .day((byte) day)
//                .month((byte) month)
//                .year((byte) year)
//                .build();
//    }
//}
