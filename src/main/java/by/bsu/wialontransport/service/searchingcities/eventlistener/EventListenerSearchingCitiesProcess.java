package by.bsu.wialontransport.service.searchingcities.eventlistener;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static by.bsu.wialontransport.crud.dto.City.createWithSearchingCitiesProcess;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;


/**
 * events are handled synchronously
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventListenerSearchingCitiesProcess {
    private static final String LOG_TEMPLATE_START_PROCESS_SEARCHING_CITIES
            = "Process searching cities has been started. Process: {}";

    private static final String LOG_SUCCESS_SUBTASK_SEARCHING_CITIES
            = "Subtask searching cities has been finished successfully.";
    private static final String LOG_TEMPLATE_FAILURE_SUBTASK_SEARCHING_CITIES
            = "Subtask searching cities has been failed. Exception: {}.";

    private static final String LOG_SUCCESS_PROCESS_SEARCHING_CITIES
            = "Process searching all cities has been finished successfully.";
    private static final String LOG_TEMPLATE_FAILURE_PROCESS_SEARCHING_CITIES
            = "Process searching all cities has been failed. Exception: {}.";

    private final SearchingCitiesProcessService searchingCitiesProcessService;
    private final CityService cityService;
    private final AddressService addressService;

    @EventListener
    public void onStartSearchingCities(final StartSearchingCitiesProcessEvent event) {
        log.info(LOG_TEMPLATE_START_PROCESS_SEARCHING_CITIES, event.getProcess());
    }

    @EventListener
    public void onSuccessSearchingCitiesBySubtask(final SuccessSearchingCitiesBySubtaskEvent event) {
        this.searchingCitiesProcessService.increaseHandledPoints(event.getProcess(), event.getAmountHandledPoints());
        log.info(LOG_SUCCESS_SUBTASK_SEARCHING_CITIES);
    }

    @EventListener
    public void onFailedSearchingCitiesBySubtask(final FailedSearchingCitiesBySubtaskEvent event) {
        final Exception exception = event.getException();
        log.error(LOG_TEMPLATE_FAILURE_SUBTASK_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
    }

    @EventListener
    @Transactional(isolation = READ_COMMITTED)
    public void onSuccessSearchingAllCities(final SuccessSearchingAllCitiesEvent event) {
        final SearchingCitiesProcess process = event.getProcess();
        final List<City> citiesToBeSaved = this.findCitiesWithNotExistGeometriesAndInjectedProcess(
                event.getFoundCities(), process
        );
        this.cityService.saveAll(citiesToBeSaved);
        this.searchingCitiesProcessService.updateStatus(process, SUCCESS);
        log.info(LOG_SUCCESS_PROCESS_SEARCHING_CITIES);
    }

    @EventListener
    public void onFailedSearchingAllCities(final FailedSearchingAllCitiesEvent event) {
        final Exception exception = event.getException();
        this.searchingCitiesProcessService.updateStatus(event.getProcess(), ERROR);
        log.error(LOG_TEMPLATE_FAILURE_PROCESS_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
    }

    private List<City> findCitiesWithNotExistGeometriesAndInjectedProcess(final Collection<City> foundCities,
                                                                          final SearchingCitiesProcess process) {
        return foundCities.stream()
                .filter(city -> !this.addressService.isExistByGeometry(city.getGeometry()))
                .map(cityWithoutProcess -> createWithSearchingCitiesProcess(cityWithoutProcess, process))
                .toList();
    }
}
