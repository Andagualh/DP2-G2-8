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
			<th>Paciente Asignado</th>
			<spring:url value="/pacientes/{paciente.id}" var="pacienteUrl">
				<spring:param name="pacienteId" value="${paciente.id}" />
			</spring:url>
			<td><a href="${fn:escapeXml(pacienteUrl)}">${paciente.nombre}&ensp;${paciente.apellidos}</a></td>
		</tr>
	</table>

	<c:if test="${canbedeleted==false}">
		<spring:url value="citas/{citaId}/informes/{informeId}" var="deleteUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Informe</a>
	</c:if>

</petclinic:layout>
