<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl-core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="spring-form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>

<head>
    <title>Main page</title>
</head>

<body>

    <table width="50%">
        <tr>
            <td><h4><a href="/logout">Log out</a></h4></td>
            <td><h4><a href="/user/changeEmail">Change email</a></h4></td>
            <td><h4><a href="/user/changePassword">Change password</a></h4></td>
        </tr>
    </table>

    <div>
        <h2>Your trackers</h2>
        <div>

            <input type="button" value="add new" onclick="window.location.href='/user/addTracker'" />

            <table border="1" width="33%">

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

                    <jstl-core:url var="link_to_delete_tracker" value="/user/deleteTracker">
                        <jstl-core:param name="trackerId" value="${listed_tracker.id}" />
                    </jstl-core:url>

                    <tr align="center">
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
