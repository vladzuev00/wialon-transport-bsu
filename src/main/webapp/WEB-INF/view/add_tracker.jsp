<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Add tracker</title>
    </head>

    <body>

        <div>
            <div>
                <h2>Add new tracker</h2>
            </div>
        </div>

        <div>
                <spring-form:form acceptCharset="UTF-8" modelAttribute="added_tracker_form" method="POST" action="/user/addTracker">
                            <table>
                                <tbody>
                                    <tr>
                                        <td><spring-form:label path="imei">Imei:</spring-form:label></td>
                                        <td>
                                            <spring-form:input path="imei" />
                                            <spring-form:errors path="imei" />
                                            ${imeiAlreadyExistsError}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><spring-form:label path="password">Password:</spring-form:label></td>
                                        <td>
                                            <spring-form:input path="password" />
                                            <spring-form:errors path="password" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><spring-form:label path="phoneNumber">Phone number:</spring-form:label></td>
                                        <td>
                                            <spring-form:input path="phoneNumber" />
                                            <spring-form:errors path="phoneNumber" />
                                            ${phoneNumberAlreadyExistsError}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label></label></td>
                                        <td><input type="submit" value="Add"/></td>
                                    </tr>
                                </tbody>
                            </table>
               </spring-form:form>
        </div>

        <br />

        <p>
              <a href="${pageContext.request.contextPath}/user/profile">
                   Cancel
              </a>
        </p>

    </body>

</html>