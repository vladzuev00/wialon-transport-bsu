package by.bsu.wialontransport.service.report.model;

import lombok.Value;
import org.vandeseer.easytable.structure.Row;

import java.util.List;

@Value
public class ReportTable {
    List<Row> rows;
    float width;
}
