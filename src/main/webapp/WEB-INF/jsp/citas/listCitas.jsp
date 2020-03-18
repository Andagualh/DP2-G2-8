<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Owners</h2>

    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Paciente</th>
            <th style="width: 150px;">Fecha</th>
            <th style="width: 150px;">Lugar</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="cita">
            <tr>
                <td>
                    <spring:url value="/cita/{citaId}" var="citaUrl">
                        <spring:param name="citaId" value="${cita.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(citaUrl)}"><c:out value="${cita.fecha} ${cita.lugar}"/></a>
                </td>
                <td>
                    <c:out value="${cita.paciente}"/>
                </td>
                <td>
                    <c:out value="${cita.fecha}"/>
                </td>
                <td>
                    <c:out value="${cita.lugar}"/>
                </td>            
 
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
