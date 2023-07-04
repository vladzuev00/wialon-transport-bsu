package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.mapper.DataMapper;
import by.bsu.wialontransport.crud.repository.DataRepository;
import by.bsu.wialontransport.model.DateInterval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DataService extends AbstractCRUDService<Long, DataEntity, Data, DataMapper, DataRepository> {

    public DataService(final DataMapper mapper, final DataRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<Data> findTrackerLastDataByTrackerId(final Long trackerId) {
        final Optional<DataEntity> optionalEntity = super.repository.findTrackerLastDataByTrackerId(trackerId);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    //TODO: test
    @Transactional(readOnly = true)
    public List<Data> findDataWithTrackerAndAddress(final User user, final DateInterval dateInterval) {
        final List<DataEntity> foundEntities = super.repository.findDataWithTrackerAndAddressOfUser(
                user.getId(), dateInterval.getStart(), dateInterval.getEnd()
        );
        return super.mapper.mapToDto(foundEntities);
    }
}
