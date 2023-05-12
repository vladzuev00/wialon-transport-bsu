package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.areaiterator.AreaIterator;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import by.bsu.wialontransport.service.searchingcities.exception.SearchingCitiesException;
import by.bsu.wialontransport.service.searchingcities.factory.SearchingCitiesProcessFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static java.lang.Math.ceil;
import static java.lang.Math.min;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static java.util.stream.IntStream.range;

@Service
public final class StartingSearchingCitiesProcessService {
    private final SearchingCitiesProcessFactory processFactory;
    private final SearchingCitiesProcessService searchingCitiesProcessService;
    private final ApplicationEventPublisher eventPublisher;
    private final SearchingCitiesService searchingCitiesService;
    private final ExecutorService executorService;
    private final int amountHandledPointsToSaveState;

    public StartingSearchingCitiesProcessService(final SearchingCitiesProcessFactory processFactory,
                                                 final SearchingCitiesProcessService searchingCitiesProcessService,
                                                 final ApplicationEventPublisher eventPublisher,
                                                 final SearchingCitiesService searchingCitiesService,
                                                 @Qualifier("executorServiceToSearchCities") final ExecutorService executorService,
                                                 @Value("${search-cities.amount-handled-points-to-save-state}") final int amountHandledPointsToSaveState) {
        this.processFactory = processFactory;
        this.searchingCitiesProcessService = searchingCitiesProcessService;
        this.eventPublisher = eventPublisher;
        this.searchingCitiesService = searchingCitiesService;
        this.executorService = executorService;
        this.amountHandledPointsToSaveState = amountHandledPointsToSaveState;
    }

    public SearchingCitiesProcess start(final AreaCoordinate areaCoordinate, final double searchStep) {
        final SearchingCitiesProcess process = this.processFactory.create(areaCoordinate, searchStep);
        final SearchingCitiesProcess savedProcess = this.searchingCitiesProcessService.save(process);
        this.eventPublisher.publishEvent(new StartSearchingCitiesProcessEvent(this, savedProcess));
        runAsync(new TaskSearchingAllCities(areaCoordinate, searchStep, savedProcess), this.executorService);
        return savedProcess;
    }

    @RequiredArgsConstructor
    private final class TaskSearchingAllCities implements Runnable {
        private final AreaCoordinate areaCoordinate;
        private final double searchStep;
        private final SearchingCitiesProcess process;

        @Override
        public void run() {
            try {
                final Set<String> namesAlreadyFoundCities = newKeySet();
                final List<Coordinate> coordinates = this.findCoordinates();
                final int amountOfSubAreas = this.findAmountOfSubAreas();
                final List<City> foundUniqueCities = range(0, amountOfSubAreas)
                        .parallel()
                        .mapToObj(i -> this.extractSubAreaCoordinatesByItsIndex(coordinates, i))
                        .map(subAreaCoordinates -> new SubtaskSearchingCities(subAreaCoordinates, this.process))
                        .map(subtask -> supplyAsync(subtask::search, executorService))
                        .map(CompletableFuture::join)
                        .flatMap(Collection::stream)
                        .filter(city -> namesAlreadyFoundCities.add(city.getCityName()))
                        .toList();
                this.publishSuccessSearchingEvent(foundUniqueCities);
            } catch (final Exception exception) {
                this.publishFailedSearchingEvent(exception);
            }
        }

        private int findAmountOfSubAreas() {
            return (int) ceil(((double) this.process.getTotalPoints()) / amountHandledPointsToSaveState);
        }

        private List<Coordinate> findCoordinates() {
            final List<Coordinate> coordinates = new ArrayList<>();
            final AreaIterator areaIterator = new AreaIterator(this.areaCoordinate, this.searchStep);
            areaIterator.forEachRemaining(coordinates::add);
            return coordinates;
        }

        private List<Coordinate> extractSubAreaCoordinatesByItsIndex(final List<Coordinate> coordinates,
                                                                     final int subAreaIndex) {
            return coordinates.subList(
                    subAreaIndex * amountHandledPointsToSaveState,
                    min(amountHandledPointsToSaveState * (subAreaIndex + 1), coordinates.size())
            );
        }

        private void publishSuccessSearchingEvent(final List<City> foundCities) {
            final SearchingCitiesProcessEvent event = new SuccessSearchingAllCitiesEvent(
                    StartingSearchingCitiesProcessService.this,
                    this.process,
                    foundCities
            );
            eventPublisher.publishEvent(event);
        }

        private void publishFailedSearchingEvent(final Exception exception) {
            final SearchingCitiesProcessEvent event = new FailedSearchingAllCitiesEvent(
                    StartingSearchingCitiesProcessService.this,
                    this.process,
                    exception
            );
            eventPublisher.publishEvent(event);
        }
    }

    @RequiredArgsConstructor
    private final class SubtaskSearchingCities {
        private final List<Coordinate> coordinates;
        private final SearchingCitiesProcess process;

        public Collection<City> search() {
            try {
                final Collection<City> foundCities = searchingCitiesService.findByCoordinates(this.coordinates);
                this.publishSuccessSearchingEvent();
                return foundCities;
            } catch (final Exception exception) {
                this.publishFailedSearchingEvent(exception);
                throw new SearchingCitiesException(exception);
            }
        }

        private void publishSuccessSearchingEvent() {
            final SearchingCitiesProcessEvent event = new SuccessSearchingCitiesBySubtaskEvent(
                    StartingSearchingCitiesProcessService.this,
                    this.process,
                    this.coordinates.size()
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
}
