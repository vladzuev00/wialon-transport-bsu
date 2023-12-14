package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.checkEquals;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.isDataLoaded;
import static org.junit.Assert.assertFalse;

public final class ParameterRepositoryTest extends AbstractContextTest {

    @Autowired
    private ParameterRepository repository;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void parameterShouldBeFoundById() {
        super.startQueryCount();
        final ParameterEntity actual = this.repository.findById(257L).orElseThrow();
        super.checkQueryCount(1);

        assertFalse(isDataLoaded(actual));

        final ParameterEntity expected = ParameterEntity.builder()
                .id(257L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void parameterShouldBeInserted() {
        final ParameterEntity givenParameter = ParameterEntity.builder()
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(super.entityManager.getReference(DataEntity.class, 256L))
                .build();

        super.startQueryCount();
        this.repository.save(givenParameter);
        super.checkQueryCount(2);
    }
}
