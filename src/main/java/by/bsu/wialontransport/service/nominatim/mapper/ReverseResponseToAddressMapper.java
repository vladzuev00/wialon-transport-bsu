package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.crud.dto.Address;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

@Component
public final class ReverseResponseToAddressMapper extends AbstractReverseResponseToAddressMapper<Address> {

    public ReverseResponseToAddressMapper(final GeometryFactory geometryFactory, final GeoJSONReader geoJSONReader) {
        super(geometryFactory, geoJSONReader);
    }

    @Override
    protected Address createAddress(final Geometry boundingBox,
                                    final Point center,
                                    final String cityName,
                                    final String countryName,
                                    final Geometry geometry) {
        //TODO
//        return Address.builder()
//                .boundingBox(boundingBox)
//                .center(center)
//                .cityName(cityName)
//                .countryName(countryName)
//                .geometry(geometry)
//                .build();
        return null;
    }
}
