<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring-form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>

<head>
    <title>Main page</title>
</head>

<body>

    <div>
        <div>
            <h2><a href="/logout">Log out</a></h2>
        </div>
    </div>

    <div>
        <div>
            <h2>Your trackers</h2>
        </div>
    </div>

    <div>
        <div>

            <input type="button" value="add new" onclick="window.location.href=''" />

            <table border="1">

                <jstl-core:url var="link_to_sort_by_imei" value="/user/profile">
                      <jstl-core:param name="trackerSortingKey" value="IMEI" />
                </jstl-core:url>

                <jstl-core:url var="link_to_sort_by_phone_number" value="/user/profile">
                      <jstl-core:param name="trackerSortingKey" value="PHONE_NUMBER" />
                </jstl-core:url>

                <tr>
                    <th><a href="${link_to_sort_by_imei}">Imei</a></th>
                    <th><a href="${link_to_sort_by_phone_number}">Phone number</a></th>
                    <th>Operation</th>
                </tr>

                <jsp:useBean id="listed_trackers" scope="request" type="java.util.Collection" />
                <jstl-core:forEach var="listed_tracker" items="${listed_trackers}">

                    <jstl-core:url var="link_to_update_tracker" value="/user/updateTracker">
                        <jstl-core:param name="trackerId" value="${listed_tracker.id}" />
                    </jstl-core:url>

                    <jstl-core:url var="link_to_delete_tracker" value="">
                        <jstl-core:param name="" value="${listed_tracker.id}" />
                    </jstl-core:url>

                    <tr>
                        <td>${listed_tracker.imei}</td>
                        <td>${listed_tracker.phoneNumber}</td>
                        <td>
                            <a href="${link_to_update_tracker}">Update</a>
                            |
                            <a href="${link_to_delete_tracker}" onclick="return (confirm('Are you sure you want to delete this tracker'))">
                                     Delete
                            </a>
                        </td>
                    </tr>

                </jstl-core:forEach>

            </table>

        </div>
    </div>

</body>

</html>
