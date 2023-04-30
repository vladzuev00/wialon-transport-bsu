package by.bsu.wialontransport.crud.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CityEntity extends AddressEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "searching_cities_process_id")
    @ToString.Exclude
    private SearchingCitiesProcessEntity searchingCitiesProcess;
}
