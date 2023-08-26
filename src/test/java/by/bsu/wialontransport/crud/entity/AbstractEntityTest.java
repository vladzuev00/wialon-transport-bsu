package by.bsu.wialontransport.crud.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public final class AbstractEntityTest {

    @Test
    public void entitiesShouldBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);
        final AbstractEntity<Long> secondGivenEntity = createEntity(255L);
        assertEquals(firstGivenEntity, secondGivenEntity);
    }

    @Test
    public void entitiesShouldNotBeEqual() {
        final AbstractEntity<Long> firstGivenEntity = createEntity(255L);
        final AbstractEntity<Long> secondGivenEntity = createEntity(256L);
        assertNotEquals(firstGivenEntity, secondGivenEntity);
    }

    private static AbstractEntity<Long> createEntity(final Long id) {
        final AbstractEntity<Long> entity = new TestEntity();
        entity.setId(id);
        return entity;
    }

    private static final class TestEntity extends AbstractEntity<Long> {
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
