package by.vladzuev.locationreceiver.service.searchingcities.eventlistener.event;

import by.vladzuev.locationreceiver.service.searchingcities.StartingSearchingCitiesProcessService;
import org.springframework.context.ApplicationEvent;

public abstract class SearchingCitiesProcessEvent extends ApplicationEvent {

    public SearchingCitiesProcessEvent(final StartingSearchingCitiesProcessService service) {
        super(service);
    }

}
