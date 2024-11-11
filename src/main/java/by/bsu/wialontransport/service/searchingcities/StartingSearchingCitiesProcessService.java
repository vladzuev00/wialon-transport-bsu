package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.service.searchingcities.areaiterator.AreaIterator;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import by.bsu.wialontransport.service.searchingcities.factory.SearchingCitiesProcessFactory;
import by.bsu.wialontransport.service.searchingcities.threadpoolexecutor.SearchingCitiesThreadPoolExecutor;
import by.bsu.wialontransport.util.CollectionUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CollectionUtil.collectToList;
import static java.lang.Math.ceil;
import static java.lang.Math.min;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static java.util.stream.IntStream.range;
import static lombok.AccessLevel.PACKAGE;

@Service
public final class StartingSearchingCitiesProcessService {
    private final SearchingCitiesProcessFactory processFactory;
    private final SearchingCitiesProcessService processService;
    private final ApplicationEventPublisher eventPublisher;
    private final SearchingCitiesService searchingCitiesService;
    private final SearchingCitiesThreadPoolExecutor threadPoolExecutor;
    private final int handledPointCountToSaveState;

    public StartingSearchingCitiesProcessService(final SearchingCitiesProcessFactory processFactory,
                                                 final SearchingCitiesProcessService processService,
                                                 final ApplicationEventPublisher eventPublisher,
                                                 final SearchingCitiesService searchingCitiesService,
                                                 final SearchingCitiesThreadPoolExecutor threadPoolExecutor,
                                                 @Value("${searching-cities.handled-point-count-to-save-state}") final int handledPointCountToSaveState) {
        this.processFactory = processFactory;
        this.processService = processService;
        this.eventPublisher = eventPublisher;
        this.searchingCitiesService = searchingCitiesService;
        this.threadPoolExecutor = threadPoolExecutor;
        this.handledPointCountToSaveState = handledPointCountToSaveState;
    }

    public SearchingCitiesProcess start(final AreaCoordinate areaCoordinate, final double searchStep) {
        final SearchingCitiesProcess process = processFactory.create(areaCoordinate, searchStep);
        final SearchingCitiesProcess savedProcess = processService.save(process);
        eventPublisher.publishEvent(new StartSearchingCitiesProcessEvent(this, savedProcess));
        runAsync(new TaskSearchingAllCities(areaCoordinate, searchStep, savedProcess), threadPoolExecutor);
        return savedProcess;
    }

    @RequiredArgsConstructor
    @Getter(PACKAGE)
    final class TaskSearchingAllCities implements Runnable {
        private final AreaCoordinate areaCoordinate;
        private final double searchStep;
        private final SearchingCitiesProcess process;

        @Override
        public void run() {
            try {
                final Set<Geometry> geometriesAccumulator = newKeySet();
                final CompletableFuture<List<City>> foundUniqueCitiesFuture = splitArea()
                        .map(this::createSubtask)
                        .map(this::run)
                        .map(future -> thenRemoveDuplicates(future, geometriesAccumulator))
                        .reduce(TaskSearchingAllCities::combine)
                        .orElseGet(() -> completedFuture(emptyList()));
                final List<City> foundUniqueCities = foundUniqueCitiesFuture.get();
                publishSuccessSearchingEvent(foundUniqueCities);
            } catch (final Exception exception) {
                publishFailedSearchingEvent(exception);
            }
        }

        private Stream<List<GpsCoordinate>> splitArea() {
            final List<GpsCoordinate> coordinates = findCoordinates();
            final int subAreaCount = findSubAreaCount();
            return range(0, subAreaCount).mapToObj(i -> extractSubAreaCoordinates(coordinates, i));
        }

        private int findSubAreaCount() {
            return (int) ceil(((double) process.getTotalPoints()) / handledPointCountToSaveState);
        }

        private List<GpsCoordinate> findCoordinates() {
            final AreaIterator areaIterator = new AreaIterator(areaCoordinate, searchStep);
            return collectToList(areaIterator);
        }

        private List<GpsCoordinate> extractSubAreaCoordinates(final List<GpsCoordinate> coordinates, final int subAreaIndex) {
            return coordinates.subList(
                    subAreaIndex * handledPointCountToSaveState,
                    min(handledPointCountToSaveState * (subAreaIndex + 1), coordinates.size())
            );
        }

        private SubtaskSearchingCities createSubtask(final List<GpsCoordinate> coordinates) {
            return new SubtaskSearchingCities(coordinates, process);
        }

        private CompletableFuture<List<City>> run(final SubtaskSearchingCities subtask) {
            return supplyAsync(subtask, threadPoolExecutor);
        }

        private static CompletableFuture<List<City>> thenRemoveDuplicates(final CompletableFuture<List<City>> future,
                                                                          final Set<Geometry> geometriesAccumulator) {
            return future.thenApply(cities -> removeDuplicates(cities, geometriesAccumulator));
        }

        private static List<City> removeDuplicates(final List<City> cities, final Set<Geometry> geometriesAccumulator) {
            return cities.stream()
                    .filter(city -> geometriesAccumulator.add(city.findGeometry()))
                    .toList();
        }

        private static CompletableFuture<List<City>> combine(final CompletableFuture<List<City>> first,
                                                             final CompletableFuture<List<City>> second) {
            return first.thenCombine(second, CollectionUtil::concat);
        }

        private void publishSuccessSearchingEvent(final List<City> foundCities) {
            final SearchingCitiesProcessEvent event = new SuccessSearchingAllCitiesEvent(
                    StartingSearchingCitiesProcessService.this,
                    process,
                    foundCities
            );
            eventPublisher.publishEvent(event);
        }

        private void publishFailedSearchingEvent(final Exception exception) {
            final SearchingCitiesProcessEvent event = new FailedSearchingAllCitiesEvent(
                    StartingSearchingCitiesProcessService.this,
                    process,
                    exception
            );
            eventPublisher.publishEvent(event);
        }
    }

    @RequiredArgsConstructor
    final class SubtaskSearchingCities implements Supplier<List<City>> {
        private final List<GpsCoordinate> coordinates;
        private final SearchingCitiesProcess process;

        @Override
        public List<City> get() {
            try {
                final List<City> cities = searchingCitiesService.findByCoordinates(coordinates);
                publishSuccessSearchingEvent();
                return cities;
            } catch (final Exception exception) {
                publishFailedSearchingEvent(exception);
                throw new SearchingCitiesException(exception);
            }
        }

        private void publishSuccessSearchingEvent() {
            final SearchingCitiesProcessEvent event = new SuccessSearchingCitiesBySubtaskEvent(
                    StartingSearchingCitiesProcessService.this,
                    process,
                    coordinates.size()
            );
            eventPublisher.publishEvent(event);
        }

        private void publishFailedSearchingEvent(final Exception exception) {
            final SearchingCitiesProcessEvent event = new FailedSearchingCitiesBySubtaskEvent(
                    StartingSearchingCitiesProcessService.this,
                    exception
            );
            eventPublisher.publishEvent(event);
        }
    }

    static final class SearchingCitiesException extends RuntimeException {

        @SuppressWarnings("unused")
        public SearchingCitiesException() {

        }

        @SuppressWarnings("unused")
        public SearchingCitiesException(final String description) {
            super(description);
        }

        public SearchingCitiesException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public SearchingCitiesException(final String description, final Exception cause) {
            super(description, cause);
        }

    }
}
