package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

@Getter
public final class FailedSearchingAllCitiesEvent extends SearchingCitiesProcessEvent {
    private final SearchingCitiesProcess process;
    private final Exception exception;

    public FailedSearchingAllCitiesEvent(final StartingSearchingCitiesProcessService service,
                                         final SearchingCitiesProcess process,
                                         final Exception exception) {
        super(service);
        this.process = process;
        this.exception = exception;
    }

}
