package by.bsu.wialontransport.configuration;

import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertNotNull;

public final class SearchingCitiesConfigurationTest {

    private final SearchingCitiesConfiguration configuration = new SearchingCitiesConfiguration();

    @Test
    public void executorServiceToSearchCitiesShouldBeCreated() {
        final ExecutorService actual = this.configuration.executorServiceToSearchCities(5);
        assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executorServiceToSearchCitiesShouldNotBeCreatedBecauseOfAmountOfThreadsIsLessThanMinimalAllowable() {
        this.configuration.executorServiceToSearchCities(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executorServiceToSearchCitiesShouldNotBeCreatedBecauseOfAmountOfThreadsIsMoreThanMaximaAllowable() {
        this.configuration.executorServiceToSearchCities(11);
    }
}
