package by.bsu.wialontransport.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Size of executor service should be equal or more than 2 - one thread to start main task, another threads to start subtasks.
 * If size is 1, then main task will be run and then blocked until subtasks will be finished but subtasks won't be run
 */
@Configuration
public class SearchingCitiesConfiguration {
    private static final int MINIMAL_ALLOWABLE_AMOUNT_OF_THREADS_TO_SEARCH_CITIES = 2;
    private static final int MAXIMAL_ALLOWABLE_AMOUNT_OF_THREAD_TO_SEARCH_CITIES = 10;

    @Bean
    public ExecutorService executorServiceToSearchCities(
            @Value("${search-cities.amount-of-threads-to-search-cities}") final int amountOfThreadsToSearchCities
    ) {
        if (!isValidAmountOfThreadsToSearchCities(amountOfThreadsToSearchCities)) {
            throw new IllegalArgumentException(findExceptionMessageNotValidAmountOfThreadsToSearchCities());
        }
        return newFixedThreadPool(amountOfThreadsToSearchCities);
    }

    private static boolean isValidAmountOfThreadsToSearchCities(final int amountOfThreadsToSearchCities) {
        return MINIMAL_ALLOWABLE_AMOUNT_OF_THREADS_TO_SEARCH_CITIES <= amountOfThreadsToSearchCities
                && amountOfThreadsToSearchCities <= MAXIMAL_ALLOWABLE_AMOUNT_OF_THREAD_TO_SEARCH_CITIES;
    }

    private static String findExceptionMessageNotValidAmountOfThreadsToSearchCities() {
        return "Amount of threads to search cities should be from %d to %d"
                .formatted(
                        MINIMAL_ALLOWABLE_AMOUNT_OF_THREADS_TO_SEARCH_CITIES,
                        MAXIMAL_ALLOWABLE_AMOUNT_OF_THREAD_TO_SEARCH_CITIES
                );
    }
}
