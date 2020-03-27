<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Citas</h2>

    <table id="citasTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 200px;">Fecha</th>
        	<th style="width: 200px;">Lugar</th>
            <th style="width: 200px;">Paciente</th>
            <th style="width: 150px;">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="cita">
            <tr>
                <td>
                    <c:out value="${cita.fecha}"/>
                </td>
                <td>
                	<c:out value="${cita.lugar}"/>
                </td>
                <td>
                	<spring:url value="/pacientes/{pacienteId}" var="pacienteUrl">
                        <spring:param name="pacienteId" value="${cita.paciente.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(pacienteUrl)}"><c:out value="${cita.paciente.nombre} ${cita.paciente.apellidos}"/></a>
                </td>
                <td>
                	<spring:url value="/citas/delete/{citaId}" var="citaDeleteUrl">
                        <spring:param name="citaId" value="${cita.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(citaDeleteUrl)}">Borrar Cita</a>
                </td>         
 
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
