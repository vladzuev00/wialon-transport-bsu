package by.vladzuev.locationreceiver.crud.entity;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import org.junit.Test;

import static org.junit.Assert.*;

public final class EntityTest extends AbstractSpringBootTest {

    @Test
    public void entitiesShouldBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);
        final AbstractEntity<Long> secondGivenEntity = createEntity(255L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertTrue(actual);
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    public void sameEntitiesShouldBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);

        final boolean actual = firstGivenEntity.equals(firstGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void notProxyEntityShouldBeEqualProxyEntity() {
        final AbstractEntity<Long> firstGivenEntity = createTracker(255L);
        final AbstractEntity<Long> secondGivenEntity = entityManager.find(TrackerEntity.class, 255L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfOtherEntityIsNull() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);
        final AbstractEntity<Long> secondGivenEntity = null;

        @SuppressWarnings("all") final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfDifferentNotProxyTypes() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);
        final AbstractEntity<Long> secondGivenEntity = entityManager.find(TrackerEntity.class, 255L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void entitiesShouldNotBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);
        final AbstractEntity<Long> secondGivenEntity = createEntity(256L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void hashCodeShouldBeFound() {
        final AbstractEntity<Long> givenEntity = createEntity(255L);

        final int actual = givenEntity.hashCode();
        final int expected = 286;
        assertEquals(expected, actual);
    }

    private static AbstractEntity<Long> createEntity(final Long id) {
        final AbstractEntity<Long> entity = new TestEntity();
        entity.setId(id);
        return entity;
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerEntity createTracker(final Long id) {
        return TrackerEntity.builder()
                .id(id)
                .build();
    }

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
