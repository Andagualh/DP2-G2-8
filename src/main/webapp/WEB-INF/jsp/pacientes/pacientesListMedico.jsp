<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pacientes">
    <h2>Pacientes Personales</h2>
    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Nombre</th>
            <th>DNI</th>
            <th style="width: 120px">Fecha de Alta</th>
            <th style="width: 150px;">Medico</th>
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
                    <c:out value="${paciente.DNI}"/>
                </td>
                <td>
                    <c:out value="${paciente.f_alta}"/>
                </td>
                <td>
                    <spring:url value="/medicos/{medicoId}" var="medicoUrl">
                        <spring:param name="medicoId" value="${paciente.medico.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(medicoUrl)}"><c:out value="${paciente.medico.nombre} ${paciente.medico.apellidos}"/></a>
                </td>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
