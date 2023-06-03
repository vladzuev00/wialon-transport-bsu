<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
          <title>Update tracker</title>
   </head>

     <div id="container">
                <spring-form:form acceptCharset="UTF-8" modelAttribute="updated_tracker_form" method="PUT" action="/user/updateTracker">
                <spring-form:hidden path="id" value="${updated_tracker.id}" />
                    <table>
                        <tbody>
                            <tr>
                                <td><spring-form:label path="imei">New imei:</spring-form:label></td>
                                <td>
                                    <spring-form:input path="imei" />
                                    <spring-form:errors path="imei" />
                                </td>
                                ${imeiAlreadyExistsError}
                            </tr>
                            <tr>
                                <td><spring-form:label path="password">New password:</spring-form:label></td>
                                <td>
                                    <spring-form:input path="password" />
                                    <spring-form:errors path="password" />
                                </td>
                            </tr>
                            <tr>
                                <td><spring-form:label path="phoneNumber">New phone number:</spring-form:label></td>
                                <td>
                                    <spring-form:input path="phoneNumber" />
                                    <spring-form:errors path="phoneNumber" />
                                </td>
                            </tr>
                            <tr>
                                <td><label></label></td>
                                <td><input type="submit" value="Update" /></td>
                            </tr>
                        </tbody>
                    </table>
                </spring-form:form>
     </div>

     <br />

     <p>
            <a href="${pageContext.request.contextPath}">
                Cancel
            </a>
     </p>

</html>