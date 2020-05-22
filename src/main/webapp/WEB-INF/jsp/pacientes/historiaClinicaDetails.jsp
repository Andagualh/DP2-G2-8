<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="historiaclinica">
	<h2>Datos del Paciente</h2>

	<table class="table table-striped">
		<tr>
			<th>Nombre completo</th>
			<td><c:out value="${paciente.nombre} ${paciente.apellidos}" /></td>
		</tr>

		<tr>
			<th>Fecha de nacimiento</th>
			<td><c:out value="${paciente.f_nacimiento}" /></td>
		</tr>

		<tr>
			<%--Must be fixed to properly work, the url is not working as intended--%>
			<th>Médico Asignado</th>
			<td><c:out
					value="${paciente.medico.nombre} ${paciente.medico.apellidos}" /></td>
		</tr>
	</table>

<h2>Historia Clinica</h2>
	<table class="table table-striped">
		<tr>
			<th>Descripción</th>
		</tr>
		<tr>
			<td><c:out value="${historiaclinica.descripcion}" /></td>
		</tr>
	</table>
	<c:if test="${medicoOk}">
		<c:if test="${not empty historiaclinica.paciente}">
			<spring:url value="/pacientes/{pacienteId}/historiaclinica/edit"
				var="editUrl">
				<spring:param name="pacienteId" value="${paciente.id}" />
			</spring:url>
			<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar
				Historia Clinica</a>
		</c:if>

		<c:if test="${empty historiaclinica.paciente}">
			<spring:url value="/pacientes/{pacienteId}/historiaclinica/new"
				var="newHistoriaUrl">
				<spring:param name="pacienteId" value="${paciente.id}" />
			</spring:url>
			<a href="${fn:escapeXml(newHistoriaUrl)}" class="btn btn-default">Crear
				Historia Clinica</a>
		</c:if>
		<br />
		<br />
	</c:if>

	<c:if test="${!historiaclinica.informes.isEmpty() && historiaclinica.informes != null}">

		<h2>Informes Relevantes</h2>
		<c:forEach var="informe" items="${historiaclinica.informes}"
			varStatus="index">
			<br />
			<div style="border-style: ridge; padding: 10px;">
				<h3>
					Informe del
					<c:out value="${informe.cita.fecha}" />
				</h3>
				<table class="table table-striped">
					<tr>
						<th>Motivo de la consulta</th>
						<td><c:out value="${informe.motivo_consulta}" /></td>
					</tr>
					<tr>
						<th>Diagnostico</th>
						<td><c:out value="${informe.diagnostico}" /></td>
					</tr>
				</table>
				<c:if test="${!informe.tratamientos.isEmpty()}">
					<h3>Tratamiento</h3>
					<table class="table table-striped">
						<c:forEach var="tratamiento" items="${informe.tratamientos}"
							varStatus="index">
							<td>
								<dl class="dl-horizontal">
									<dt>Medicamento</dt>
									<dd>
										<c:out value="${tratamiento.medicamento}" />
									</dd>
									<dt>Dosis</dt>
									<dd>
										<c:out value="${tratamiento.dosis}" />
									</dd>
									<dt>Fecha de Inicio</dt>
									<dd>
										<petclinic:localDate
											date="${tratamiento.f_inicio_tratamiento}"
											pattern="dd-MM-yyyy" />
									</dd>
									<dt>Fecha de Finalizacion</dt>
									<dd>
										<petclinic:localDate date="${tratamiento.f_fin_tratamiento}"
											pattern="dd-MM-yyyy" />
									</dd>
								</dl>
							</td>
						</c:forEach>
					</table>
				</c:if>
			</div>
		</c:forEach>
	</c:if>

</petclinic:layout>