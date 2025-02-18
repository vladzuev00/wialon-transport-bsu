package by.vladzuev.locationreceiver.service.searchingcities.eventlistener.event;

import by.vladzuev.locationreceiver.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

@Getter
public final class SuccessSearchingCitiesBySubtaskEvent extends SearchingCitiesProcessEvent {
    private final SearchingCitiesProcess process;
    private final long countHandledPoints;

    public SuccessSearchingCitiesBySubtaskEvent(final StartingSearchingCitiesProcessService service,
                                                final SearchingCitiesProcess process,
                                                final long countHandledPoints) {
        super(service);
        this.process = process;
        this.countHandledPoints = countHandledPoints;
    }

}
