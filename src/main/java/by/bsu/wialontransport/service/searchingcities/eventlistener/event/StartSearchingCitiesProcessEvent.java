package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

@Getter
public final class StartSearchingCitiesProcessEvent extends SearchingCitiesProcessEvent {
    private final SearchingCitiesProcess process;

    public StartSearchingCitiesProcessEvent(final StartingSearchingCitiesProcessService service,
                                            final SearchingCitiesProcess process) {
        super(service);
        this.process = process;
    }
}
