package by.bsu.wialontransport.service.searchingcities.threadpoolexecutor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SearchingCitiesThreadPoolExecutorTest {

    @Test
    public void executorShouldBeCreated() {
        final int givenThreadCount = 2;

        final SearchingCitiesThreadPoolExecutor actual = new SearchingCitiesThreadPoolExecutor(givenThreadCount);

        final int actualCorePoolSize = actual.getCorePoolSize();
        assertEquals(givenThreadCount, actualCorePoolSize);

        final int actualMaximumPoolSize = actual.getMaximumPoolSize();
        assertEquals(givenThreadCount, actualMaximumPoolSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executorShouldNotBeCreatedBecauseOfNotValidThreadCount() {
        final int givenThreadCount = 1;

        new SearchingCitiesThreadPoolExecutor(givenThreadCount);
    }
}
