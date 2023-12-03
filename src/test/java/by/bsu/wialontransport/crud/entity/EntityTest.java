package by.bsu.wialontransport.crud.entity;

import by.bsu.wialontransport.base.AbstractContextTest;
import org.junit.Test;

import static org.junit.Assert.*;

public final class EntityTest extends AbstractContextTest {

    @Test
    public void entitiesShouldBeEqual() {
        final Entity<Long> firstGivenEntity = createEntity(255L);
        final Entity<Long> secondGivenEntity = createEntity(255L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertTrue(actual);
    }

    @Test
    @SuppressWarnings("EqualsWithItself")
    public void sameEntitiesShouldBeEqual() {
        final Entity<Long> firstGivenEntity = createEntity(255L);

        final boolean actual = firstGivenEntity.equals(firstGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void notProxyEntityShouldBeEqualProxyEntity() {
        final Entity<Long> firstGivenEntity = createTracker(255L);
        final Entity<Long> secondGivenEntity = super.entityManager.find(TrackerEntity.class, 255L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertTrue(actual);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfOtherEntityIsNull() {
        final Entity<Long> firstGivenEntity = createEntity(255L);
        final Entity<Long> secondGivenEntity = null;

        @SuppressWarnings("all") final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void entitiesShouldNotBeEqualBecauseOfDifferentNotProxyTypes() {
        final Entity<Long> firstGivenEntity = createEntity(255L);
        final Entity<Long> secondGivenEntity = super.entityManager.find(TrackerEntity.class, 255L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void entitiesShouldNotBeEqual() {
        final Entity<Long> firstGivenEntity = createEntity(255L);
        final Entity<Long> secondGivenEntity = createEntity(256L);

        final boolean actual = firstGivenEntity.equals(secondGivenEntity);
        assertFalse(actual);
    }

    @Test
    public void hashCodeShouldBeFound() {
        final Entity<Long> givenEntity = createEntity(255L);

        final int actual = givenEntity.hashCode();
        final int expected = 286;
        assertEquals(expected, actual);
    }

    private static Entity<Long> createEntity(final Long id) {
        final Entity<Long> entity = new TestEntity();
        entity.setId(id);
        return entity;
    }

    @SuppressWarnings("SameParameterValue")
    private static TrackerEntity createTracker(final Long id) {
        return TrackerEntity.builder()
                .id(id)
                .build();
    }

    private static final class TestEntity extends Entity<Long> {
        private Long id;

        @Override
        public void setId(final Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return this.id;
        }
    }

}
