<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="Cita">

	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#fecha").datepicker({
											dateFormat : 'yy-mm-dd'
										});
									});
								</script>
    </jsp:attribute>

	<jsp:body>
    <h2>
        <c:if test="${cita['new']}">New </c:if> Cita
    </h2>
    <form:form modelAttribute="cita" class="form-horizontal" id="add-cita-form" action="/citas/save">
    
        <div class="form-group has-feedback">
            <petclinic:inputField label="Lugar" name="lugar" />
            
            <spring:bind path="fecha">
			    <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }" />
			    <c:set var="valid" value="${not status.error and not empty status.actualValue}" />
			    <div class="${cssGroup}">
			        <label class="col-sm-2 control-label">Fecha</label>
			
			        <div class="col-sm-10">
                   		<form:input type="date" class="form-control" path="fecha" size="30" maxlength="80"/>
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
            
            <input type="hidden" name="paciente" value="${paciente.id}" />
        </div>
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${cita['new']}">
                    	<input type="hidden" name="id" value="${cita.id}">
                        <button class="btn btn-default" type="submit">A�adir Cita</button>
                    </c:when>
                    <c:otherwise>
                    	<input type="hidden" name="id" value="${cita.id}">
                        <button class="btn btn-default" type="submit">Editar Cita</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
    </form:form>
    
    </jsp:body>

</petclinic:layout>
