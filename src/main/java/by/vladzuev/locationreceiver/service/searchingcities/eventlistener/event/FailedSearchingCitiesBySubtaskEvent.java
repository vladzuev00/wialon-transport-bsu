package by.vladzuev.locationreceiver.service.searchingcities.eventlistener.event;

import by.vladzuev.locationreceiver.service.searchingcities.StartingSearchingCitiesProcessService;
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
