package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.mapper.TrackerMapper;
import by.bsu.wialontransport.crud.repository.TrackerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class TrackerService
        extends AbstractCRUDEncryptingPasswordService<Long, TrackerEntity, Tracker, TrackerMapper, TrackerRepository> {

    public TrackerService(final TrackerMapper mapper,
                          final TrackerRepository repository,
                          final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository, passwordEncoder);
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByImei(final String imei) {
        final Optional<TrackerEntity> optionalFoundEntity = super.repository.findByImei(imei);
        return optionalFoundEntity.map(super.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<Tracker> findByUser(final User user) {
        return this.findByUser(
                user,
                Pageable::unpaged
        );
    }

    @Transactional(readOnly = true)
    public List<Tracker> findByUser(final User user, final int pageNumber, final int pageSize) {
        return this.findByUser(
                user,
                () -> PageRequest.of(pageNumber, pageSize)
        );
    }

    @Transactional(readOnly = true)
    public List<Tracker> findByUser(final User user,
                                    final int pageNumber,
                                    final int pageSize,
                                    final Comparator<Tracker> comparator) {
        final List<Tracker> foundTrackers = this.findByUser(user, pageNumber, pageSize);
        foundTrackers.sort(comparator);
        return foundTrackers;
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByIdWithUser(final Long id) {
        final Optional<TrackerEntity> optionalEntity = super.repository.findByIdWithUser(id);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<Tracker> findByPhoneNumber(final String phoneNumber) {
        final Optional<TrackerEntity> optionalEntity = super.repository.findByPhoneNumber(phoneNumber);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    private List<Tracker> findByUser(final User user, final Supplier<Pageable> pageableSupplier) {
        final Long userId = user.getId();
        final Pageable pageable = pageableSupplier.get();
        final List<TrackerEntity> foundEntities = super.repository.findByUserId(userId, pageable);
        return super.mapper.mapToDtos(foundEntities);
    }
}
