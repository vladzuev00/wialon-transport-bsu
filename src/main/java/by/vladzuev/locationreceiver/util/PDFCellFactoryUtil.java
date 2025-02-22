package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import static by.vladzuev.locationreceiver.util.NumberUtil.round;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.function.Function.identity;

@UtilityClass
public final class PDFCellFactoryUtil {
    private static final float CELL_BORDER_WIDTH = 1;
    private static final Integer CELL_FONT_SIZE = 8;
    private static final short CELL_PRECISION = 5;

    private static final String CELL_DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
    private static final DateTimeFormatter CELL_DATE_TIME_FORMATTER = ofPattern(CELL_DATE_TIME_PATTERN);

    public static TextCell createTextCell(final int content) {
        return createTextCell(content, value -> Integer.toString(value));
    }

    public static TextCell createTextCell(final LocalDateTime content) {
        return createTextCell(content, CELL_DATE_TIME_FORMATTER::format);
    }

    public static TextCell createTextCell(final double content) {
        return createTextCell(content, PDFCellFactoryUtil::transformToStringWithRounding);
    }

    public static TextCell createTextCell(final String content) {
        return createTextCell(content, identity());
    }

    public static <T> TextCell createTextCell(final T content, final Function<T, String> transformerContentToString) {
        final String contentAsString = transformerContentToString.apply(content);
        return TextCell.builder()
                .text(contentAsString)
                .borderWidth(CELL_BORDER_WIDTH)
                .fontSize(CELL_FONT_SIZE)
                .build();
    }

    private static String transformToStringWithRounding(final double content) {
        final double roundedContent = round(content, CELL_PRECISION);
        return Double.toString(roundedContent);
    }

}
