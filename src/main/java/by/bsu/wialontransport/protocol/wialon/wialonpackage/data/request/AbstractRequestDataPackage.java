package by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class AbstractRequestDataPackage implements Package {
    private final List<Data> data;
}
