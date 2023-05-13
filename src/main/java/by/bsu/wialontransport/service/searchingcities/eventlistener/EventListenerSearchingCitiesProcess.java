package by.bsu.wialontransport.service.searchingcities.eventlistener;

import by.bsu.wialontransport.crud.dto.Address;
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
import java.util.Optional;

import static by.bsu.wialontransport.crud.dto.City.createWithAddressAndProcess;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;


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

    //TODO: refactor test
    @EventListener
    @Transactional(isolation = SERIALIZABLE)
    public void onSuccessSearchingAllCities(final SuccessSearchingAllCitiesEvent event) {
        final SearchingCitiesProcess process = event.getProcess();
        this.saveCities(event.getFoundCities(), process);
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

    private void saveCities(final Collection<City> foundCities,
                                  final SearchingCitiesProcess process) {
        foundCities.stream()
                .filter(this::isCityNotExist)
                .forEach(notExistingCity -> this.saveCityAndAddressIfNotExist(notExistingCity, process));
    }

    private boolean isCityNotExist(final City city) {
        return !this.cityService.isExistByGeometry(city.getGeometry());
    }

    private void saveCityAndAddressIfNotExist(final City city, SearchingCitiesProcess process) {
        final Address savedAddress = this.findSavedAddressOfCity(city);
        final City cityToBeSaved = createWithAddressAndProcess(city, savedAddress, process);
        this.cityService.save(cityToBeSaved);
    }

    private Address findSavedAddressOfCity(final City city) {
        final Optional<Address> optionalAddress = this.addressService.findByGeometry(city.getGeometry());
        return optionalAddress.orElseGet(() -> this.addressService.save(city.getAddress()));
    }
}
