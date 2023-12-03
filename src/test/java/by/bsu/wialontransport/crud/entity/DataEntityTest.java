//package by.bsu.wialontransport.crud.entity;
//
//import by.bsu.wialontransport.crud.entity.DataEntity.Latitude;
//import by.bsu.wialontransport.crud.entity.DataEntity.Longitude;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static java.util.stream.IntStream.range;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//
//public final class DataEntityTest {
//
//    @Test
//    public void latitudeTypeShouldBeFoundByValue() {
//        final char givenValue = 'N';
//
//        final Latitude.Type actual = Latitude.Type.findByValue(givenValue);
//        assertSame(NORTH, actual);
//    }
//
//    @Test
//    public void latitudeTypeShouldNotBeFoundByValue() {
//        final char givenValue = 'n';
//
//        final Latitude.Type actual = Latitude.Type.findByValue(givenValue);
//        assertSame(Latitude.Type.NOT_DEFINED, actual);
//    }
//
//    @Test
//    public void longitudeTypeShouldBeFoundByValue() {
//        final char givenValue = 'E';
//
//        final Longitude.Type actual = Longitude.Type.findByValue(givenValue);
//        assertSame(EAST, actual);
//    }
//
//    @Test
//    public void longitudeTypeShouldNotBeFoundByValue() {
//        final char givenValue = 'e';
//
//        final Longitude.Type actual = Longitude.Type.findByValue(givenValue);
//        assertSame(Longitude.Type.NOT_DEFINED, actual);
//    }
//
//    @Test
//    public void parametersShouldBeSet() {
//        final DataEntity givenData = createData(255L);
//        final List<ParameterEntity> givenParameters = List.of(
//                createParameter(256L),
//                createParameter(257L),
//                createParameter(258L)
//        );
//
//        givenData.setParameters(givenParameters);
//
//        assertSame(givenParameters, givenData.getParameters());
//        range(0, givenParameters.size()).forEach(i -> assertSame(givenParameters.get(i).getData(), givenData));
//    }
//
//    @Test
//    public void parameterShouldBeAdd() {
//        final DataEntity givenData = createData(256L);
//        final ParameterEntity givenParameter = createParameter(257L);
//
//        givenData.addParameter(givenParameter);
//
//        assertSame(givenData, givenParameter.getData());
//        assertTrue(givenData.getParameters().stream().anyMatch(parameter -> parameter == givenParameter));
//    }
//
//    private static DataEntity createData(final Long id) {
//        return DataEntity.builder()
//                .id(id)
//                .parameters(new ArrayList<>())
//                .build();
//    }
//
//    private static ParameterEntity createParameter(final Long id) {
//        return ParameterEntity.builder()
//                .id(id)
//                .build();
//    }
//}
