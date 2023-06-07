package by.bsu.wialontransport.service.encrypting.crud;

import by.bsu.wialontransport.crud.dto.AbstractDto;

@FunctionalInterface
public interface PasswordEncryptingService<T extends AbstractDto<?>> {
    T updatePassword(final T source, final String encryptedPassword);
}
