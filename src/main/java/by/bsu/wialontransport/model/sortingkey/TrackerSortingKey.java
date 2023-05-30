package by.bsu.wialontransport.model.sortingkey;

import by.bsu.wialontransport.crud.dto.Tracker;

import java.util.Comparator;

import static java.util.Comparator.comparing;

public enum TrackerSortingKey {
    IMEI(comparing(Tracker::getImei)),
    PHONE_NUMBER(comparing(Tracker::getPhoneNumber));

    private final Comparator<Tracker> comparator;

    TrackerSortingKey(final Comparator<Tracker> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Tracker> getComparator() {
        return this.comparator;
    }
}
