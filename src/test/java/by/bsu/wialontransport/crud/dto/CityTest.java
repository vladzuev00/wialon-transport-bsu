package by.bsu.wialontransport.crud.dto;

import org.junit.Test;

import static by.bsu.wialontransport.crud.dto.City.copyWithAddressAndProcess;
import static by.bsu.wialontransport.crud.dto.City.createWithAddress;
import static org.junit.Assert.assertEquals;

public final class CityTest {

    @Test
    public void cityShouldBeCopiedWithGivenAddressAndProcess() {
        final City givenSource = createCity(255L);
        final Address givenAddress = createAddress(256L);
        final SearchingCitiesProcess givenProcess = createProcess(257L);

        final City actual = copyWithAddressAndProcess(givenSource, givenAddress, givenProcess);
        final City expected = new City(255L, givenAddress, givenProcess);
        assertEquals(expected, actual);
    }

    @Test
    public void cityShouldBeCreatedByAddress() {
        final Address givenAddress = createAddress(256L);

        final City actual = createWithAddress(givenAddress);
        final City expected = createCity(givenAddress);
        assertEquals(expected, actual);
    }

    private static City createCity(final Long id) {
        return City.builder()
                .id(id)
                .build();
    }

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static SearchingCitiesProcess createProcess(final Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }

    private static City createCity(final Address address) {
        return City.builder()
                .address(address)
                .build();
    }
}