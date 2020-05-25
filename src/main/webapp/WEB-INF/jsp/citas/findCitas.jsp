<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="citas">

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
    <h2>Buscar Citas</h2>

    <form:form modelAttribute="cita" action="/citas/porfecha" method="get" class="form-horizontal"
               id="search-owner-form">
        <div class="form-group">
            <div class="form-group has-feedback">
			<petclinic:customDateInput label="Fecha" name="fecha"/>
            <input type="hidden" name="paciente" value="${paciente.id}" />
        </div>

                </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Buscar citas</button>
            </div>
        </div>

    </form:form>

    <br/> 
    <sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default" href='<spring:url value="/citas/new" htmlEscape="true"/>'>A�adir cita</a>
	</sec:authorize>
	</jsp:body>
	
</petclinic:layout>