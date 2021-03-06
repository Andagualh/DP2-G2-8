<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="pacientes">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#f_nacimiento").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
									
									$(function() {
										$("#f_alta").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
	<h2>
		<c:if test="${isNewPaciente}">Nuevo </c:if>
		Paciente
	</h2>
	<form:form modelAttribute="paciente" class="form-horizontal" id="add-paciente-form">
		<div class="form-group has-feedback">
			<petclinic:inputField label="Nombre completo" name="nombre" />
			<petclinic:inputField label="Apellidos" name="apellidos" />
			<petclinic:customDateInput label="Fecha de nacimiento" name="f_nacimiento"/>
			<petclinic:inputField label="DNI" name="DNI" />
			<petclinic:inputField label="Direcci�n del domicilio" name="domicilio" />
			<petclinic:inputField label="Tel�fono" name="n_telefono" />
			<petclinic:inputField label="Email" name="email" />
			<petclinic:customDateInput label="Fecha alta" name="f_alta"/>
						
			<spring:bind path="medico">
				<c:set var="cssGroup" value="form-group ${status.error ? 'error' : '' }" />
				<c:set var="valid" value="${not status.error and not empty status.actualValue}" />
				<div class="${cssGroup}">
					<label class="col-sm-2 control-label">M�dico Asignado</label>

					<div class="col-sm-10">
						<form:select class="form-control" path="medico">
						<c:forEach var="m" items="${medicoList}">
						    <c:choose>
						        <c:when test="${paciente.medico.id==m.id}">
						            <form:option selected="true" value="${m.id}" label="${m.nombre} ${m.apellidos}" />
						        </c:when>
						        <c:otherwise>
						            <form:option value="${m.id}" label="${m.nombre} ${m.apellidos}" />
						        </c:otherwise>
						    </c:choose>
						</c:forEach>
						</form:select>
						<c:if test="${valid}">
							<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
						</c:if>
						<c:if test="${status.error}">
							<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
							<span class="help-inline">${status.errorMessage}</span>
						</c:if>
					</div>
				</div>
			</spring:bind>

			<input type="hidden" name="citas" value="${citas}" />
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<c:choose>
					<c:when test="${isNewPaciente}">
						<button class="btn btn-default" type="submit">Add paciente</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-default" type="submit">Update paciente</button>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form:form>
	</jsp:body>

</petclinic:layout>
