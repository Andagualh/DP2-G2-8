<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="Tratamiento">

	<jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#f_inicio_tratamiento").datepicker({dateFormat: 'yy-mm-dd'});
                $("#f_fin_tratamiento").datepicker({dateFormat: 'yy-mm-dd'});
            });
        </script>
    </jsp:attribute>
	
	<jsp:body>
    <h2>
        <c:if test="${tratamiento['new']}">Nuevo </c:if> Tratamiento
    </h2>
    <form:form modelAttribute="tratamiento" class="form-horizontal" id="add-tratamiento-form" action="/tratamientos/save">
    
        <div class="form-group has-feedback">
        	<input type="hidden" name="tratamiento id" value="${id}"/>
        	<input type="hidden" name="informe" value="${informe.id}"/>
        	<petclinic:inputField label="Medicamento" name="medicamento"/>
            <petclinic:inputField label="Dosis de Medicamento" name="dosis"/>
            <petclinic:inputField label="Fecha de Inicio de Tratamiento" name="f_inicio_tratamiento"/>
            <petclinic:inputField label="Fecha de Fin de Tratamiento" name="f_fin_tratamiento"/>
        </div>
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${tratamiento['new']}">
                    	<input type="hidden" name="id" value="${tratamiento.id}">
                        <button class="btn btn-default" type="submit">Añadir Tratamiento</button>
                    </c:when>
                    <c:otherwise>
                    	<input type="hidden" name="id" value="${tratamiento.id}">
                        <button class="btn btn-default" type="submit">Editar Tratamiento</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
    </form:form>
    
    </jsp:body>
    
</petclinic:layout>
