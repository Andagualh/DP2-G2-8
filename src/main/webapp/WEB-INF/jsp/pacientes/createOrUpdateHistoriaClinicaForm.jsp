<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="historiaclinica">
	<h2>
		<c:if test="${historiaclinica['new']}">New </c:if>
		Historia Clinica
	</h2>
	<form:form modelAttribute="historiaclinica" class="form-horizontal" id="add-historiaclinica-form">
	 
		<div class="form-group has-feedback">
		
		<input type="hidden" name="id" value="${historiaclinica.id}"/>
			<petclinic:inputField label="Description" name="descripcion" />
			<p align="center"><c:out value="${errord}"/></p>
			<c:if test="${not historiaclinica['new']}">
						<input type="hidden" name="paciente" value="${paciente.id}"/>
			 </c:if>
			 
			 
				<c:set var="cssGroup" value="form-group ${status.error ? 'error' : '' }" />
				<c:set var="valid" value="${not status.error and not empty status.actualValue}" />
			 <div class="col-sm-10">
			
			<c:if test="${valid}">
							<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
						</c:if>
						<c:if test="${status.error}">
							<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
							<span class="help-inline">${status.errorMessage}</span>
						</c:if>
					</div>
			
			
					</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${historiaclinica['new']}">
						<button class="btn btn-default" type="submit">Add Historia Clinica</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default" type="submit">Update Historia Clinica</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form:form>
	
	<h2>Paciente Information</h2>
	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><c:out value="${paciente.nombre} ${paciente.apellidos}" /></td>
		</tr>

		<tr>
			<th>F_nacimiento</th>
			<td><c:out value="${paciente.f_nacimiento}" /></td>
		</tr>

		<tr>
			<th>F_alta</th>
			<td><c:out value="${paciente.f_alta}" /></td>
		</tr>

		<tr>
			<th>Address</th>
			<td><c:out value="${paciente.domicilio}" /></td>
		</tr>
		<tr>
			<th>DNI</th>
			<td><c:out value="${paciente.DNI}" /></td>
		</tr>
		<tr>
			<th>Telefono</th>
			<td><c:out value="${paciente.n_telefono}" /></td>
		</tr>

		<tr>
			<th>Email</th>
			<td><c:out value="${paciente.email}" /></td>
		</tr>

		<tr>
			<%--Must be fixed to properly work, the url is not working as intended--%>
			<th>Medico Asignado</th>
			<spring:url value="/medicos/{medico.id}" var="medicoUrl">
				<spring:param name="medicoId" value="${paciente.medico.id}" />
			</spring:url>
			<td><a href="${fn:escapeXml(medicoUrl)}">${paciente.medico.nombre}&ensp;${paciente.medico.apellidos}</a></td>
		</tr>
	</table>
</petclinic:layout>