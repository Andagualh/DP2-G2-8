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

	<c:if test="${cannotbedeleted==false && tratamientos.isEmpty()}">
		<spring:url value="/citas/{citaId}/informes/delete/{informeId}" var="deleteUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Informe</a>
	</c:if>
	
	<c:if test="${canbeedited==true}">
			<spring:url value="/citas/{citaId}/informes/{informeId}/edit" var="editUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Informe</a>
	</c:if>
	
	<c:if test="${informe.historiaClinica == null}">
		<spring:url value="/citas/{citaId}/informes/{informeId}/addtohistoriaclinica" var="addToHistoriaClinicaUrl">
			<spring:param name="informeId" value="${informe.id}" />
			<spring:param name="citaId" value="${informe.cita.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(addToHistoriaClinicaUrl)}" class="btn btn-default">A�adir Informe a Historia Clinica</a>
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
        <c:forEach var="tratamiento" items="${tratamientos}" varStatus = "index">

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
                            <th></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tr>
                            <td>
                            	<c:choose>
    								<c:when test="${editTratamientoOk}">
    									<spring:url value="/tratamientos/{tratamientoId}/edit" var="editUrl">
										<spring:param name="tratamientoId" value="${tratamiento.id}" />
										</spring:url>
										<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Tratamiento</a>
    								</c:when>    
   	 							<c:otherwise>
   	 								<dd><c:out value="No Editable"/></dd>
    							</c:otherwise>
								</c:choose>
                            </td>
                            <td>
								<c:choose>
    								<c:when test="${editTratamientoOk}">
    									<spring:url value="/tratamientos/delete/{tratamientoId}" var="deleteUrl">
										<spring:param name="tratamientoId" value="${tratamiento.id}" />
										</spring:url>
										<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Tratamiento</a>
    								</c:when>    
   	 							<c:otherwise>
   	 								<dd><c:out value="No Deleteable"/></dd>
    							</c:otherwise>
								</c:choose>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
	</c:if>
	
	<c:if test="${!tratamientos.isEmpty()}">
	<c:forEach begin="0" end="${tratapages}" var="current">
		<spring:url value="../informes/{informeId}?page=${current}" var="pageUrl">
		<spring:param name="informeId" value="${informe.id}" />
		</spring:url>
	<a href="${fn:escapeXml(pageUrl)}" class="btn btn-default">Pagina ${current+1}</a>
	</c:forEach>
	</c:if>
	<br>
	<br>
    <c:if test="${editTratamientoOk}">
    <spring:url value="/tratamientos/new/{informeId}" var="createTratUrl">
				<spring:param name="informeId" value="${informe.id}" />
	</spring:url>
	<a href="${fn:escapeXml(createTratUrl)}" class="btn btn-default">Crear Tratamiento</a>
	</c:if>

</petclinic:layout>
