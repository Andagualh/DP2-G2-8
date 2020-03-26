<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pacientes">

    <h2>Paciente Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><c:out value="${paciente.nombre} ${paciente.apellidos}"/></td>
        </tr>

        <tr>
            <th>F_nacimiento</th>
            <td><c:out value="${paciente.f_nacimiento}"/></td>
        </tr>

        <tr>
            <th>F_alta</th>
            <td><c:out value="${paciente.f_alta}"/></td>
        </tr>

        <tr>
            <th>Address</th>
            <td><c:out value="${paciente.domicilio}"/></td>
        </tr>
        <tr>
            <th>DNI</th>
            <td><c:out value="${paciente.DNI}"/></td>
        </tr>
        <tr>
            <th>Telefono</th>
            <td><c:out value="${paciente.n_telefono}"/></td>
        </tr>

        <tr>
            <th>Email</th>
            <td><c:out value="${paciente.email}"/></td>
        </tr>
        
        <tr>
                <%--Must be fixed to properly work, the url is not working as intended--%>
                <th>Medico Asignado</th>
                <spring:url value="/medicos/{medico.id}" var="medicoUrl">
                    <spring:param name="medicoId" value="${paciente.medico.id}"/>
                </spring:url>
            <td>    
                <a href="${fn:escapeXml(medicoUrl)}">${paciente.medico.nombre}&ensp;${paciente.medico.apellidos}</a>
            </td>
        </tr>
    </table>
    
    <spring:url value="/pacientes/{pacienteId}/edit" var="editUrl">
   		<spring:param name="pacienteId" value="${paciente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Paciente</a>
    
    <spring:url value="/pacientes/{pacienteId}/delete" var="deleteUrl">
   		<spring:param name="pacienteId" value="${paciente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Paciente</a>
    
    <spring:url value="/citas/new/{pacienteId}" var="createCitaUrl">
   		<spring:param name="pacienteId" value="${paciente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(createCitaUrl)}" class="btn btn-default">Crear Cita</a>
    
</petclinic:layout>
