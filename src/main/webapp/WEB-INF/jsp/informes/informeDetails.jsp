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

	<c:if test="${cannotbedeleted==false}">
		<spring:url value="/citas/{citaId}/informes/delete/{informeId}" var="deleteUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Informe</a>
	</c:if>
	
	<c:if test="${informe.historiaClinica == null}">
		<spring:url value="/citas/{citaId}/informes/{informeId}/addtohistoriaclinica" var="addToHistoriaClinicaUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(addToHistoriaClinicaUrl)}" class="btn btn-default">Añadir Informe a Historia Clinica</a>
	</c:if>
	<c:if test="${informe.historiaClinica != null}">
		<spring:url value="/citas/{citaId}/informes/{informeId}/detelefromhistoriaclinica" var="deleteFromHistoriaClinicaUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(deleteFromHistoriaClinicaUrl)}" class="btn btn-default">Eliminar Informe de Historia Clinica</a>
	</c:if>

	<c:if test="${!tratamientos.isEmpty()}">
    <br/>
    <br/>
    <br/>
    <h2>Tratamiento</h2>
        <table class="table table-striped">
        <c:forEach var="tratamiento" items="${tratamientos}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Medicamento</dt>
                        <dd><c:out value="${tratamiento.medicamento}"/></dd>
                        <dt>Dosis</dt>
                        <dd><c:out value="${tratamiento.dosis}"/></dd>
                        <dt>Fecha de Inicio</dt>
                        <dd><petclinic:localDate date="${tratamiento.f_inicio_tratamiento}" pattern="dd-MM-yyyy"/></dd>
                    	<dt>Fecha de Finalizacion</dt>
                        <dd><petclinic:localDate date="${tratamiento.f_fin_tratamiento}" pattern="dd-MM-yyyy"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Editar Tratamiento</th>
                            <th>Borrar Tratamiento</th>
                        </tr>
                        </thead>
                        <tr>
                            <td>
								<c:out value="TODO Editar tratamiento"/>
                            </td>
                            <td>
								<c:out value="TODO Borrar tratamiento"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
    </c:if>
</petclinic:layout>
