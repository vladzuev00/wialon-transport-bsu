package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import by.vladzuev.locationreceiver.util.entity.AddressEntityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AddressRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void addressShouldBeFoundById() {
        final Long givenId = 255L;

        final Optional<AddressEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final AddressEntity actual = optionalActual.get();
        final AddressEntity expected = new AddressEntity(
                givenId,
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(1, 2),
                                new Coordinate(2, 2),
                                new Coordinate(2, 1),
                                new Coordinate(1, 1)
                        }
                ),
                geometryFactory.createPoint(new Coordinate(1.5, 1.5)),
                "first-city",
                "first-country",
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(1, 2),
                                new Coordinate(2, 2),
                                new Coordinate(2, 1),
                                new Coordinate(1, 1)
                        }
                )
        );
        AddressEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void addressShouldBeSaved() {
        final AddressEntity givenAddress = AddressEntity.builder()
                .boundingBox(
                        geometryFactory.createPolygon(
                                new Coordinate[]{
                                        new Coordinate(2, 3),
                                        new Coordinate(4, 5),
                                        new Coordinate(6, 7),
                                        new Coordinate(8, 9),
                                        new Coordinate(2, 3)
                                }
                        )
                )
                .center(geometryFactory.createPoint(new Coordinate(53.050287, 24.873636)))
                .cityName("test-city")
                .countryName("test-country")
                .geometry(
                        geometryFactory.createPolygon(
                                new Coordinate[]{
                                        new Coordinate(1, 1),
                                        new Coordinate(1, 2),
                                        new Coordinate(2, 2),
                                        new Coordinate(2, 1),
                                        new Coordinate(1, 1)
                                }
                        )
                )
                .build();

        final AddressEntity actual = repository.save(givenAddress);
        final AddressEntity expected = new AddressEntity(
                1L,
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new Coordinate(2, 3),
                                new Coordinate(4, 5),
                                new Coordinate(6, 7),
                                new Coordinate(8, 9),
                                new Coordinate(2, 3)
                        }
                ),
                geometryFactory.createPoint(new Coordinate(53.050287, 24.873636)),
                "test-city",
                "test-country",
                geometryFactory.createPolygon(
                        new Coordinate[]{
                                new Coordinate(1, 1),
                                new Coordinate(1, 2),
                                new Coordinate(2, 2),
                                new Coordinate(2, 1),
                                new Coordinate(1, 1)
                        }
                )
        );
        AddressEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void addressShouldBeFoundByPoint() {
        final Point givenPoint = geometryFactory.createPoint(new Coordinate(1, 1));

        final Optional<AddressEntity> optionalActual = repository.findByPoint(givenPoint);
        assertTrue(optionalActual.isPresent());
        final AddressEntity actual = optionalActual.get();
        final AddressEntity expected = AddressEntity.builder().id(255L).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void addressShouldNotBeFoundByPoint() {
        final Point givenPoint = geometryFactory.createPoint(new Coordinate(20, 20));

        final Optional<AddressEntity> optionalActual = repository.findByPoint(givenPoint);
        assertTrue(optionalActual.isEmpty());
    }
}
