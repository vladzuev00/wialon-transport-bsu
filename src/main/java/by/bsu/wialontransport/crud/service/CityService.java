package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.mapper.CityMapper;
import by.bsu.wialontransport.crud.repository.CityRepository;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService extends AbstractCRUDService<Long, CityEntity, City, CityMapper, CityRepository> {

    public CityService(final CityMapper mapper, final CityRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public boolean isExistByGeometry(final Geometry geometry) {
        return super.repository.isExistByGeometry(geometry);
    }

    //TODO: test
    @Transactional(readOnly = true)
    public List<PreparedGeometry> findPreparedGeometriesIntersectedByLineString(final LineString lineString) {
        final List<Geometry> foundGeometries = super.repository.findGeometriesIntersectedByLineString(lineString);
        return mapToPreparedGeometries(foundGeometries);
    }

    private static List<PreparedGeometry> mapToPreparedGeometries(final List<Geometry> geometries) {
        return geometries.stream()
                .map(PreparedGeometryFactory::prepare)
                .toList();
    }
}
