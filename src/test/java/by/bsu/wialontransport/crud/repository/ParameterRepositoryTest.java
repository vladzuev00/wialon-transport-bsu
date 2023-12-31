package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.checkEquals;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.isDataFetched;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class ParameterRepositoryTest extends AbstractContextTest {

    @Autowired
    private ParameterRepository repository;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void parameterShouldBeFoundById() {
        final Long givenId = 257L;

        startQueryCount();
        final Optional<ParameterEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final ParameterEntity actual = optionalActual.get();
        assertFalse(isDataFetched(actual));

        final ParameterEntity expected = ParameterEntity.builder()
                .id(givenId)
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
