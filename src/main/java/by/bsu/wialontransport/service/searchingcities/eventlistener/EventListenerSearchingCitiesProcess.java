package by.bsu.wialontransport.service.searchingcities.eventlistener;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static by.bsu.wialontransport.crud.dto.City.copyWithAddressAndProcess;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;


/**
 * events are handled synchronously
 */
@Slf4j
@Component
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
    private final ExecutorService executorService;

    public EventListenerSearchingCitiesProcess(final SearchingCitiesProcessService searchingCitiesProcessService,
                                               final CityService cityService,
                                               final AddressService addressService,
                                               @Qualifier("executorServiceToSearchCities") final ExecutorService executorService) {
        this.searchingCitiesProcessService = searchingCitiesProcessService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.executorService = executorService;
    }

    @EventListener
    public void onStartSearchingCities(final StartSearchingCitiesProcessEvent event) {
        log.info(LOG_TEMPLATE_START_PROCESS_SEARCHING_CITIES, event.getProcess());
    }

    @EventListener
    public void onSuccessSearchingCitiesBySubtask(final SuccessSearchingCitiesBySubtaskEvent event) {
        this.searchingCitiesProcessService.increaseHandledPoints(
                event.getProcess(), event.getAmountHandledPoints()
        );
        log.info(LOG_SUCCESS_SUBTASK_SEARCHING_CITIES);
    }

    @EventListener
    public void onFailedSearchingCitiesBySubtask(final FailedSearchingCitiesBySubtaskEvent event) {
        final Exception exception = event.getException();
        log.error(LOG_TEMPLATE_FAILURE_SUBTASK_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
        this.executorService.shutdownNow();
    }

    @EventListener
    @Transactional(isolation = REPEATABLE_READ)
    public void onSuccessSearchingAllCities(final SuccessSearchingAllCitiesEvent event) {
        final SearchingCitiesProcess process = event.getProcess();
        this.saveCitiesIfNotExist(event.getFoundCities(), process);
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

    private void saveCitiesIfNotExist(final Collection<City> foundCities,
                                      final SearchingCitiesProcess process) {
        final List<City> notExistCitiesWithSavedAddresses = this.findNotExistCitiesWithSavedAddresses(
                foundCities, process
        );
        this.cityService.saveAll(notExistCitiesWithSavedAddresses);
    }

    private List<City> findNotExistCitiesWithSavedAddresses(final Collection<City> foundCities,
                                                            final SearchingCitiesProcess process) {
        return foundCities.stream()
                .filter(this::isCityNotExist)
                .map(notExistCity -> this.mapToCityWithSavedAddressAndProcess(notExistCity, process))
                .toList();
    }

    private boolean isCityNotExist(final City city) {
        return !this.cityService.isExistByGeometry(city.findGeometry());
    }

    private City mapToCityWithSavedAddressAndProcess(final City city, final SearchingCitiesProcess process) {
        final Address savedAddress = this.findSavedAddressOfCity(city);
        return copyWithAddressAndProcess(city, savedAddress, process);
    }

    private Address findSavedAddressOfCity(final City city) {
        final Optional<Address> optionalAddress = this.addressService.findByGeometry(city.findGeometry());
        return optionalAddress.orElseGet(() -> this.addressService.save(city.getAddress()));
    }
}
