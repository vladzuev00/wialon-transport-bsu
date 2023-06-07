package by.bsu.wialontransport.service.encrypting.crud;

import by.bsu.wialontransport.crud.dto.AbstractDto;

import java.util.Collection;
import java.util.List;

public interface PasswordEncryptingService<T extends AbstractDto<?>> {
    T save(final T source);
    List<T> saveAll(final Collection<T> source);
    T updatePassword(final T source, final String encryptedPassword);
}
