<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="historiaclinica">

    <h2>Historia clinica</h2>


    <table class="table table-striped">
        <tr>
            <th>Descripcion</th>
            <td><c:out value="${historiaclinica.descripcion}"/></td>
        </tr>
        
     </table>
        
    <c:if test="${not empty historiaclinica.paciente}">
         <spring:url value="/pacientes/{pacienteId}/historiaclinica/edit" var="editUrl">
   		<spring:param name="pacienteId" value="${paciente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Historia Clinica</a>
    </c:if>	
    
    <c:if test="${empty historiaclinica.paciente}">			
					  <spring:url value="/pacientes/{pacienteId}/historiaclinica/new" var="newHistoriaUrl">
   		<spring:param name="pacienteId" value="${paciente.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(newHistoriaUrl)}" class="btn btn-default">Crear Historia Clinica</a>
	</c:if>					
    
    </petclinic:layout>