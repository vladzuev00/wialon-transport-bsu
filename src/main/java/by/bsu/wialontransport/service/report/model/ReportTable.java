package by.bsu.wialontransport.service.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Row;

import java.awt.*;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class ReportTable {
    List<Row> rows;
    float[] columnWidths;
    PDFont font;
    Integer fontSize;
    Color borderColor;
}
