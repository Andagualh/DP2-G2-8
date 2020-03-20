<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>
        <c:if test="${cita['new']}">New </c:if> Cita
    </h2>
    <form:form modelAttribute="cita" class="form-horizontal" id="add-cita-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Fecha" name="fecha"/>
            <petclinic:inputField label="Lugar" name="lugar"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${cita['new']}">
                        <button class="btn btn-default" type="submit">Añadir Cita</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Editar Cita</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
