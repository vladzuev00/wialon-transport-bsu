package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.Dto;
import by.vladzuev.locationreceiver.crud.entity.EntityWithPassword;
import by.vladzuev.locationreceiver.crud.mapper.Mapper;
import by.vladzuev.locationreceiver.crud.repository.EntityWithPasswordRepository;
import lombok.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class CRUDEncryptingPasswordServiceTest {

    @Mock
    private TestPersonRepository mockedRepository;

    @Mock
    private BCryptPasswordEncoder mockedPasswordEncoder;

    private TestPersonService service;

    @Before
    public void initializeService() {
        service = new TestPersonService(null, mockedRepository, mockedPasswordEncoder);
    }

    @Test
    public void passwordShouldBeUpdated() {
        final Long givenId = 255L;
        final TestPersonDto givenDto = new TestPersonDto(givenId);
        final String givenNewPassword = "new-password";

        final String givenEncryptedNewPassword = "a1gsa$dd3";
        when(mockedPasswordEncoder.encode(same(givenNewPassword))).thenReturn(givenEncryptedNewPassword);

        final int givenCountUpdatedRows = 1;
        when(mockedRepository.updatePassword(same(givenId), same(givenEncryptedNewPassword)))
                .thenReturn(givenCountUpdatedRows);

        final int actualCountUpdatedRows = service.updatePassword(givenDto, givenNewPassword);
        assertEquals(givenCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    public void entityShouldBeConfiguredBeforeSave() {
        final Long givenId = 255L;
        final String givenPassword = "password";
        final TestPersonEntity givenEntity = new TestPersonEntity(givenId, givenPassword);

        final String givenEncryptedPassword = "fs$fd$sf$1";
        when(mockedPasswordEncoder.encode(same(givenPassword))).thenReturn(givenEncryptedPassword);

        service.configureBeforeSave(givenEntity);

        final String actualNewPassword = givenEntity.getPassword();
        assertSame(givenEncryptedPassword, actualNewPassword);
    }

    private static final class TestPersonService extends CRUDEncryptingPasswordService<
            Long,
            TestPersonEntity,
            TestPersonDto,
            Mapper<TestPersonEntity, TestPersonDto>,
            TestPersonRepository
            > {

        public TestPersonService(final Mapper<TestPersonEntity, TestPersonDto> mapper,
                                 final TestPersonRepository repository,
                                 final BCryptPasswordEncoder passwordEncoder) {
            super(mapper, repository, passwordEncoder);
        }
    }

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString(callSuper = true)
    private static final class TestPersonEntity extends EntityWithPassword<Long> {
        private Long id;

        public TestPersonEntity(final Long id, final String password) {
            super(password);
            this.id = id;
        }
    }

    @Value
    private static class TestPersonDto implements Dto<Long> {
        Long id;
    }

    private interface TestPersonRepository extends EntityWithPasswordRepository<Long, TestPersonEntity> {

    }
}
