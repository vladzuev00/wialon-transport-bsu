package by.bsu.wialontransport.service.searchingcities.eventlistener;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.service.searchingcities.eventlistener.event.*;
import by.bsu.wialontransport.service.searchingcities.threadpoolexecutor.SearchingCitiesThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.dto.City.copyWithAddressAndProcess;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.SUCCESS;

/*
 * events are handled synchronously
 */
@Slf4j
@Component
public class EventListenerSearchingCitiesProcess {
    private static final String LOG_TEMPLATE_START_PROCESS_SEARCHING_CITIES
            = "Process searching cities has been started. Process: {}";

    private static final String LOG_SUCCESS_SUBTASK_SEARCHING_CITIES
            = "Subtask searching cities has been finished successfully";
    private static final String LOG_TEMPLATE_FAILED_SUBTASK_SEARCHING_CITIES
            = "Subtask searching cities has been failed. Exception: {}";

    private static final String LOG_SUCCESS_PROCESS_SEARCHING_CITIES
            = "Process searching all cities has been finished successfully";
    private static final String LOG_TEMPLATE_FAILED_PROCESS_SEARCHING_CITIES
            = "Process searching all cities has been failed. Exception: {}";

    private final SearchingCitiesProcessService searchingCitiesProcessService;
    private final CityService cityService;
    private final AddressService addressService;
    private final SearchingCitiesThreadPoolExecutor threadPoolExecutor;

    public EventListenerSearchingCitiesProcess(final SearchingCitiesProcessService searchingCitiesProcessService,
                                               final CityService cityService,
                                               final AddressService addressService,
                                               final SearchingCitiesThreadPoolExecutor threadPoolExecutor) {
        this.searchingCitiesProcessService = searchingCitiesProcessService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @EventListener
    public void onStartSearchingCities(final StartSearchingCitiesProcessEvent event) {
        log.info(LOG_TEMPLATE_START_PROCESS_SEARCHING_CITIES, event.getProcess());
    }

    @EventListener
    public void onSuccessSearchingCitiesBySubtask(final SuccessSearchingCitiesBySubtaskEvent event) {
        searchingCitiesProcessService.increaseHandledPoints(event.getProcess(), event.getCountHandledPoints());
        log.info(LOG_SUCCESS_SUBTASK_SEARCHING_CITIES);
    }

    @EventListener
    public void onFailedSearchingCitiesBySubtask(final FailedSearchingCitiesBySubtaskEvent event) {
        final Exception exception = event.getException();
        log.error(LOG_TEMPLATE_FAILED_SUBTASK_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
        threadPoolExecutor.shutdownNow();
    }

    @EventListener
    @Transactional
    public void onSuccessSearchingAllCities(final SuccessSearchingAllCitiesEvent event) {
        final SearchingCitiesProcess process = event.getProcess();
        saveNotExistingCities(event.getFoundCities(), process);
        searchingCitiesProcessService.updateStatus(process, SUCCESS);
        log.info(LOG_SUCCESS_PROCESS_SEARCHING_CITIES);
    }

    @EventListener
    public void onFailedSearchingAllCities(final FailedSearchingAllCitiesEvent event) {
        final Exception exception = event.getException();
        searchingCitiesProcessService.updateStatus(event.getProcess(), ERROR);
        log.error(LOG_TEMPLATE_FAILED_PROCESS_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
    }

    private void saveNotExistingCities(final Collection<City> cities, final SearchingCitiesProcess process) {
        final List<City> notExistingCities = findNotExistingCitiesWithSavedAddresses(cities, process);
        cityService.saveAll(notExistingCities);
    }

    private List<City> findNotExistingCitiesWithSavedAddresses(final Collection<City> cities,
                                                               final SearchingCitiesProcess process) {
        return cities.stream()
                .filter(this::isNotExist)
                .map(city -> injectSavedAddressAndProcess(city, process))
                .toList();
    }

    private boolean isNotExist(final City city) {
        return !cityService.isExistByGeometry(city.findGeometry());
    }

    private City injectSavedAddressAndProcess(final City city, final SearchingCitiesProcess process) {
        final Address savedAddress = findSavedAddress(city);
        return copyWithAddressAndProcess(city, savedAddress, process);
    }

    private Address findSavedAddress(final City city) {
        final Optional<Address> optionalAddress = addressService.findByGeometry(city.findGeometry());
        return optionalAddress.orElseGet(() -> addressService.save(city.getAddress()));
    }
}
