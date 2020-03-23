<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Citas</h2>

    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 300px;">Paciente</th>
            <th style="width: 300px;">Fecha</th>
            <th style="width: 300px;">Lugar</th>
            <th style="width: 300px;"> </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${citas}" var="citas">
            
                    <spring:url value="/citas/{citaId}" var="citaUrl">
                        <spring:param name="citaId" value="${cita.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(citaUrl)}"><c:out value="${cita.paciente.nombre} ${cita.fecha} ${cita.lugar}"/></a>
                
                
             <tr>
                <td>
                    <c:out value="${citas.paciente.nombre}"/><h> </h><c:out value="${citas.paciente.apellidos}"/>
                </td>
                <td>
                    <c:out value="${citas.fecha}"/>
                </td>
                <td>
                    <c:out value="${citas.lugar}"/>
                </td> 
                <td>
                	<spring:url value="/citas/delete/{citaId}" var="citaUrlDelete">
                        <spring:param name="citaId" value="${citas.id}"/>
                    </spring:url>
        
                 	<a href="${fn:escapeXml(citaUrl)}">Editar Cita</a><p>   </p>
                 	<a href="${fn:escapeXml(citaUrlDelete)}"/>Borrar Cita</a>
                </td>            
 
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
