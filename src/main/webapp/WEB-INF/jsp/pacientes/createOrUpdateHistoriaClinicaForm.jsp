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
			<c:if test="${not historiaclinica['new']}">
						<input type="hidden" name="paciente" value="${paciente.id}"/>
			 </c:if>
			
			<c:if test="${valid}">
							<span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
						</c:if>
						<c:if test="${status.error}">
							<span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
							<span class="help-inline">${status.errorMessage}</span>
						</c:if>
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
</petclinic:layout>