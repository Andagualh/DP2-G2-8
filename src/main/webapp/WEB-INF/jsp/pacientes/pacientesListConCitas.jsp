<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pacientes">
    <h2>Pacientes</h2>

    <table id="pacientesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Address</th>
            <th>City</th>
            <th style="width: 120px">Telephone</th>
            <th>Citas</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="paciente">
            <tr>
                <td>
                    <spring:url value="/pacientes/{pacienteId}" var="pacienteUrl">
                        <spring:param name="pacienteId" value="${paciente.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(pacienteUrl)}"><c:out value="${paciente.nombre} ${paciente.apellidos}"/></a>
                </td>
                <td>
                    <c:out value="${paciente.domicilio}"/>
                </td>
                <td>
                    <c:out value="${paciente.domicilio}"/>
                </td>
                <td>
                    <c:out value="${paciente.n_telefono}"/>
                </td>
                <td>
                    <c:forEach var="cita" items="${paciente.citas}">
                        <c:out value="${cita.fecha} "/>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
