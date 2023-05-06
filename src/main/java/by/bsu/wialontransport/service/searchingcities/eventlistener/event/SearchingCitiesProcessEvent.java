package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import org.springframework.context.ApplicationEvent;

public abstract class SearchingCitiesProcessEvent extends ApplicationEvent {

    public SearchingCitiesProcessEvent(final StartingSearchingCitiesProcessService service) {
        super(service);
    }

}
