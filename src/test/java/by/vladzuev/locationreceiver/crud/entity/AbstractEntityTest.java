package by.vladzuev.locationreceiver.crud.entity;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import lombok.Builder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class AbstractEntityTest extends AbstractSpringBootTest {

    @Test
    public void entitiesShouldBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = TestEntity.builder().id(255L).build();
        final AbstractEntity<Long> secondGivenEntity = TestEntity.builder().id(255L).build();

        assertEquals(firstGivenEntity, secondGivenEntity);
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    public void sameEntitiesShouldBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = TestEntity.builder().id(255L).build();

        final boolean actual = firstGivenEntity.equals(firstGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void notProxyEntityShouldBeEqualProxyEntity() {
        final AbstractEntity<Long> firstGivenEntity = createTracker(255L);
        final AbstractEntity<Long> secondGivenEntity = entityManager.find(TrackerEntity.class, 255L);

        assertEquals(firstGivenEntity, secondGivenEntity);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfOtherEntityIsNull() {
        final AbstractEntity<Long> firstGivenEntity = TestEntity.builder().id(255L).build();
        final AbstractEntity<Long> secondGivenEntity = null;

        assertNotEquals(firstGivenEntity, secondGivenEntity);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfDifferentNotProxyTypes() {
        final AbstractEntity<Long> firstGivenEntity = TestEntity.builder().id(255L).build();
        final AbstractEntity<Long> secondGivenEntity = entityManager.find(TrackerEntity.class, 255L);

        assertNotEquals(firstGivenEntity, secondGivenEntity);
    }

    @Test
    public void entitiesShouldNotBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = TestEntity.builder().id(255L).build();
        final AbstractEntity<Long> secondGivenEntity = TestEntity.builder().id(256L).build();

        assertNotEquals(firstGivenEntity, secondGivenEntity);
    }

    @Test
    public void hashCodeShouldBeFound() {
        final AbstractEntity<Long> givenEntity = TestEntity.builder().id(255L).build();

        final int actual = givenEntity.hashCode();
        final int expected = 286;
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerEntity createTracker(final Long id) {
        return TrackerEntity.builder()
                .id(id)
                .build();
    }

    @Builder
    private static final class TestEntity extends AbstractEntity<Long> {
        private Long id;

        @Override
        public void setId(final Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }
    }

}
