package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.checkEquals;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.isDataFetched;
import static org.junit.Assert.assertFalse;

public final class ParameterRepositoryTest extends AbstractContextTest {

    @Autowired
    private ParameterRepository repository;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void parameterShouldBeFoundById() {
        startQueryCount();
        final ParameterEntity actual = repository.findById(257L).orElseThrow();
        checkQueryCount(1);

        assertFalse(isDataFetched(actual));

        final ParameterEntity expected = ParameterEntity.builder()
                .id(257L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(entityManager.getReference(DataEntity.class, 256L))
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
                .data(entityManager.getReference(DataEntity.class, 256L))
                .build();

        startQueryCount();
        repository.save(givenParameter);
        checkQueryCount(2);
    }
}
