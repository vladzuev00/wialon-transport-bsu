package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

@Getter
public final class FailedSearchingCitiesBySubtaskEvent extends SearchingCitiesProcessEvent {
    private final Exception exception;

    public FailedSearchingCitiesBySubtaskEvent(final StartingSearchingCitiesProcessService service,
                                               final Exception exception) {
        super(service);
        this.exception = exception;
    }

}
