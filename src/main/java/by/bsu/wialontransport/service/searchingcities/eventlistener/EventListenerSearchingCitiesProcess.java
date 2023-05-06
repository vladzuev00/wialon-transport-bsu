package by.bsu.wialontransport.service.searchingcities.eventlistener;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.service.searchingcities.SearchingCitiesProcessFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public final class EventListenerSearchingCitiesProcess {
    private static final String LOG_START_PROCESS_SEARCHING_CITIES = "Process searching cities has been started.";

    private static final String LOG_SUCCESS_SUBTASK_SEARCHING_CITIES
            = "Subtask searching cities has been finished successfully.";
    private static final String LOG_TEMPLATE_FAILURE_SUBTASK_SEARCHING_CITIES
            = "Subtask searching cities has been failed. Exception: {}.";

    private static final String LOG_SUCCESS_PROCESS_SEARCHING_CITIES
            = "Process searching all cities has been finished successfully.";
    private static final String LOG_TEMPLATE_FAILURE_PROCESS_SEARCHING_CITIES
            = "Process searching all cities has been failed. Exception: {}.";

    private final SearchingCitiesProcessFactory searchingCitiesProcessFactory;
    private final SearchingCitiesProcessService searchingCitiesProcessService;
    private final CityService cityService;
    private final AddressService addressService;

    public SearchingCitiesProcess onStartSearchCities(final AreaCoordinate areaCoordinate, final double searchStep) {
        final SearchingCitiesProcess processToBeSaved = this.searchingCitiesProcessFactory.create(
                areaCoordinate, searchStep
        );
        final SearchingCitiesProcess savedProcess = this.searchingCitiesProcessService.save(processToBeSaved);
        log.info(LOG_START_PROCESS_SEARCHING_CITIES);
        return savedProcess;
    }

    public void onSuccessFindCitiesBySubtask(final SearchingCitiesProcess process, final long amountHandledPoints) {
        this.searchingCitiesProcessService.increaseHandledPoints(process, amountHandledPoints);
        log.info(LOG_SUCCESS_SUBTASK_SEARCHING_CITIES);
    }

    public void onFailedFindCitiesBySubtask(final Exception exception) {
        log.error(LOG_TEMPLATE_FAILURE_SUBTASK_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
    }

    @Transactional
    public void onSuccessFindAllCities(final SearchingCitiesProcess process, final Collection<City> foundCities) {
        final List<City> citiesWithNotExistGeometries = this.findCitiesWithNotExistGeometries(foundCities);
        this.cityService.saveAll(citiesWithNotExistGeometries);
        this.searchingCitiesProcessService.updateStatus(process, SUCCESS);
        log.info(LOG_SUCCESS_PROCESS_SEARCHING_CITIES);
    }

    public void onFailedFindAllCities(final SearchingCitiesProcess process, final Exception exception) {
        this.searchingCitiesProcessService.updateStatus(process, ERROR);
        log.error(LOG_TEMPLATE_FAILURE_PROCESS_SEARCHING_CITIES, exception.getMessage());
        exception.printStackTrace();
    }

    private List<City> findCitiesWithNotExistGeometries(final Collection<City> foundCities) {
        return foundCities.stream()
                .filter(city -> !this.addressService.isExistByGeometry(city.getGeometry()))
                .collect(toList());
    }
}
