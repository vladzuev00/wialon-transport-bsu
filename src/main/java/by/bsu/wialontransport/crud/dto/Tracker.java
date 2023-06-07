package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.service.encrypting.model.Encryptable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Tracker implements AbstractDto<Long>, Encryptable {
    Long id;
    String imei;
    String password;
    String phoneNumber;
    User user;
}
