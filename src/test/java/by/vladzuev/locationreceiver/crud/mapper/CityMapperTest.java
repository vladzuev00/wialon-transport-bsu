//package by.vladzuev.locationreceiver.crud.mapper;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.crud.dto.Address;
//import by.vladzuev.locationreceiver.crud.dto.City;
//import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
//import by.vladzuev.locationreceiver.crud.entity.CityEntity;
//import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
//import by.vladzuev.locationreceiver.util.entity.CityEntityUtil;
//import org.hibernate.Hibernate;
//import org.junit.Test;
//import org.mockito.MockedStatic;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.hibernate.Hibernate.isInitialized;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mockStatic;
//
//public final class CityMapperTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private CityMapper mapper;
//
//    @Test
//    public void entityWithLoadedPropertiesShouldBeMappedToDto() {
//        final CityEntity givenEntity = CityEntity.builder()
//                .id(255L)
//                .address(createAddressEntity(256L))
//                .searchingCitiesProcess(createSearchingCitiesProcessEntity(257L))
//                .build();
//
//        final City actual = mapper.mapToDto(givenEntity);
//        final City expected = City.builder()
//                .id(255L)
//                .address(createAddressDto(256L))
//                .searchingCitiesProcess(createSearchingCitiesProcessDto(257L))
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void entityWithNotLoadedPropertiesShouldBeMappedToDto() {
//        final AddressEntity givenAddress = new AddressEntity();
//        final SearchingCitiesProcessEntity givenProcess = new SearchingCitiesProcessEntity();
//        try (final MockedStatic<Hibernate> mockedStatic = mockStatic(Hibernate.class)) {
//            mockedStatic
//                    .when(() -> isInitialized(same(givenAddress)))
//                    .thenReturn(false);
//            mockedStatic
//                    .when(() -> isInitialized(same(givenProcess)))
//                    .thenReturn(false);
//            final CityEntity givenEntity = CityEntity.builder()
//                    .id(255L)
//                    .address(givenAddress)
//                    .searchingCitiesProcess(givenProcess)
//                    .build();
//
//            final City actual = mapper.mapToDto(givenEntity);
//            final City expected = City.builder()
//                    .id(255L)
//                    .build();
//            assertEquals(expected, actual);
//        }
//    }
//
//    @Test
//    public void dtoShouldBeMappedToEntity() {
//        final City givenDto = City.builder()
//                .id(255L)
//                .address(createAddressDto(256L))
//                .searchingCitiesProcess(createSearchingCitiesProcessDto(257L))
//                .build();
//
//        final CityEntity actual = mapper.mapToEntity(givenDto);
//        final CityEntity expected = CityEntity.builder()
//                .id(255L)
//                .address(createAddressEntity(256L))
//                .searchingCitiesProcess(createSearchingCitiesProcessEntity(257L))
//                .build();
//        assertNotNull(actual);
//        CityEntityUtil.checkDeepEquals(expected, actual);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static Address createAddressDto(final Long id) {
//        return Address.builder()
//                .id(id)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static AddressEntity createAddressEntity(final Long id) {
//        return AddressEntity.builder()
//                .id(id)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static SearchingCitiesProcess createSearchingCitiesProcessDto(final Long id) {
//        return SearchingCitiesProcess.builder()
//                .id(id)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static SearchingCitiesProcessEntity createSearchingCitiesProcessEntity(final Long id) {
//        return SearchingCitiesProcessEntity.builder()
//                .id(id)
//                .build();
//    }
//}
