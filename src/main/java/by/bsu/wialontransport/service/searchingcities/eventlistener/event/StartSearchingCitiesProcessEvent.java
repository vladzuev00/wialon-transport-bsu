package by.bsu.wialontransport.service.searchingcities.eventlistener.event;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import lombok.Getter;

@Getter
public final class StartSearchingCitiesProcessEvent extends SearchingCitiesProcessEvent {
    private final AreaCoordinate areaCoordinate;
    private final double searchStep;

    public StartSearchingCitiesProcessEvent(final StartingSearchingCitiesProcessService service,
                                            final AreaCoordinate areaCoordinate,
                                            final double searchStep) {
        super(service);
        this.areaCoordinate = areaCoordinate;
        this.searchStep = searchStep;
    }
}
