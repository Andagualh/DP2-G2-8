<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="informes">

	<h2>Informe Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Motivo de la consulta</th>
			<td><c:out value="${informe.motivo_consulta}" /></td>
		</tr>

		<tr>
			<th>Diagnostico</th>
			<td><c:out value="${informe.diagnostico}" /></td>
		</tr>

		<tr>
			<%--Must be fixed to properly work, the url is not working as intended--%>
			<th>Paciente Asignado</th>
			<spring:url value="/pacientes/{paciente.id}" var="pacienteUrl">
				<spring:param name="pacienteId" value="${paciente.id}" />
			</spring:url>
			<td><a href="${fn:escapeXml(pacienteUrl)}">${paciente.nombre}&ensp;${paciente.apellidos}</a></td>
		</tr>
	</table>

	<c:if test="${canBeDeleted==true}">
		<spring:url value="/pacientes/{pacienteId}/delete" var="deleteUrl">
			<spring:param name="pacienteId" value="${paciente.id}" />
		</spring:url>
		<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Paciente</a>
	</c:if>

	<spring:url value="/citas/new/{pacienteId}" var="createCitaUrl">
		<spring:param name="pacienteId" value="${paciente.id}" />
	</spring:url>
	<a href="${fn:escapeXml(createCitaUrl)}" class="btn btn-default">Crear Cita</a>
    
    <spring:url value="/pacientes/{pacienteId}/historiaclinica" var="historiaClinicaUrl">
   		<spring:param name="pacienteId" value="${paciente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(historiaClinicaUrl)}" class="btn btn-default">Historia Clinica</a>
    

</petclinic:layout>
