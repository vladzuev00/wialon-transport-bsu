<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <title>Report</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css"/>
    <link rel="stylesheet" href="report-page.css"/>
    <style>
        .map {
            position: absolute;
            width: 100%;
            height: 100%;
        }
    </style>
</head>

<body>

    <jsp:useBean id="tracker" scope="request" type="by.bsu.wialontransport.crud.dto.Tracker" />
    <jsp:useBean id="dateTimeRange" scope="request" type="by.bsu.wialontransport.crud.dto.DateTimeRange" />

    <table>
        <tbody>
            <tr>
                <td>Tracker's imei:</td>
                <td>${tracker.imei}</td>
            </tr>
            <tr>
                <td>Tracker's phone number:</td>
                <td>${tracker.phoneNumber}</td>
            </tr>
            <tr>
                <td>Datetime range</td>
                <td>${dateTimeRange.fromDateTime} - ${dateTimeRange.toDateTime}</td>
            </tr>
        </tbody>
    </table>

    <div id="map" class="map"></div>

    <table>
        <thead>Available reports</thead>
        <tbody>
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/TODO">
                        Average speed per day
                    </a>
                    <a href="${pageContext.request.contextPath}/TODO">
                        Average amount of satellites per day
                    </a>
                    <a href="${pageContext.request.contextPath}/TODO">
                        Intersected addresses
                    </a>
                </td>
            </tr>
        </tbody>
    </table>

</body>

</html>
