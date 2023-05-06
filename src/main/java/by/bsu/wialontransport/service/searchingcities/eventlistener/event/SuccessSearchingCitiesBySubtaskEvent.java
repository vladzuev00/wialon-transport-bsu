package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

@Getter
public final class SuccessSearchingCitiesBySubtaskEvent extends SearchingCitiesProcessEvent {
    private final SearchingCitiesProcess process;
    private final long amountHandledPoints;

    public SuccessSearchingCitiesBySubtaskEvent(final StartingSearchingCitiesProcessService service,
                                                final SearchingCitiesProcess process,
                                                final long amountHandledPoints) {
        super(service);
        this.process = process;
        this.amountHandledPoints = amountHandledPoints;
    }

}
