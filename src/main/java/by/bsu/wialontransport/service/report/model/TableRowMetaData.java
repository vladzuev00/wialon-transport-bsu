package by.bsu.wialontransport.service.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.vandeseer.easytable.settings.HorizontalAlignment;

import java.awt.*;

@Value
@AllArgsConstructor
@Builder
public class TableRowMetaData {
    Color backgroundColor;
    Color textColor;
    Integer fontSize;
    HorizontalAlignment horizontalAlignment;
    float minHeight;
}
