package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

import java.util.Collection;

@Getter
public final class SuccessSearchingAllCitiesEvent extends SearchingCitiesProcessEvent {
    private final SearchingCitiesProcess process;
    private final Collection<City> foundCities;

    public SuccessSearchingAllCitiesEvent(final StartingSearchingCitiesProcessService service,
                                          final SearchingCitiesProcess process,
                                          final Collection<City> foundCities) {
        super(service);
        this.process = process;
        this.foundCities = foundCities;
    }
}
