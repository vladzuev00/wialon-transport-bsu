package by.vladzuev.locationreceiver.model.sortingkey;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
@Getter
public enum TrackerSortingKey {
    IMEI(comparing(Tracker::getImei)),
    PHONE_NUMBER(comparing(Tracker::getPhoneNumber));

    private final Comparator<Tracker> comparator;
}
