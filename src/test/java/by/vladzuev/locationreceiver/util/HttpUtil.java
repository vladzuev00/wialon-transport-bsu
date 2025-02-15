package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static java.util.Collections.singletonList;
import static org.skyscreamer.jsonassert.Customization.customization;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

//TODO: use everywhere
@UtilityClass
public final class HttpUtil {
    private static final String JSON_PROPERTY_NAME_DATE_TIME = "dateTime";

    public static final CustomComparator JSON_COMPARATOR_IGNORING_DATE_TIME = new CustomComparator(
            STRICT,
            customization(JSON_PROPERTY_NAME_DATE_TIME, (first, second) -> true)
    );

    public static <T> HttpEntity<T> createHttpEntity(final T body) {
        final HttpHeaders headers = createHttpHeaders();
        return new HttpEntity<>(body, headers);
    }

    private static HttpHeaders createHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(singletonList(APPLICATION_JSON));
        return headers;
    }
}
