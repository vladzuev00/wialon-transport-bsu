package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.crud.dto.City;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.wololo.jts2geojson.GeoJSONReader;

@Component
public final class ReverseResponseToCityMapper /*extends AbstractReverseResponseToAddressMapper<City>*/ {

    public ReverseResponseToCityMapper(final GeometryFactory geometryFactory, final GeoJSONReader geoJSONReader) {
//        super(geometryFactory, geoJSONReader);
    }

//    @Override
    protected City createAddress(final Geometry boundingBox,
                                 final Point center,
                                 final String cityName,
                                 final String countryName,
                                 final Geometry geometry) {
        return null;
        //TODO
//        return City.cityBuilder()
//                .boundingBox(boundingBox)
//                .center(center)
//                .cityName(cityName)
//                .countryName(countryName)
//                .geometry(geometry)
//                .build();
    }
}
