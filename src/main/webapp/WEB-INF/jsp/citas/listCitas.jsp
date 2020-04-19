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
            <th style="width: 150px;">Informe</th>
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
	                <c:choose>
	                    <c:when test="${cita.informe != null}">
	                    	<spring:url value="{citaId}/informes/{informeId}" var="informeDetailsUrl">
								<spring:param name="citaId" value="${cita.id}" />
								<spring:param name="informeId" value="${cita.informe.id}" />
						    </spring:url>
							<a href="${fn:escapeXml(informeDetailsUrl)}">Ver Informe</a>	   
	                    </c:when>
	                   	<c:when test="${cita.fecha == dateOfToday}">
							<spring:url value="{citaId}/informes/new" var="informeUrl">
								<spring:param name="citaId" value="${cita.id}" />
						    </spring:url>
							<a href="${fn:escapeXml(informeUrl)}">Crear Informe</a>	                    
						</c:when>
						<c:otherwise>
							<c:out value="No tiene informe"></c:out>                    
						</c:otherwise>
                </c:choose>
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
