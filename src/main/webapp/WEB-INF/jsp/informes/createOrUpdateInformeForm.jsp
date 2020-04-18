<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="informes">
    <h2>
        <c:if test="${informe['new']}">New </c:if> Informe
    </h2>
    <form:form modelAttribute="informe" class="form-horizontal" id="add-informe-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Motivo de Consulta" name="motivo_consulta"/>
            <petclinic:inputField label="Diagnostico" name="diagnostico"/>
          
            <input type="hidden" name="cita" value="${cita.id}" />
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${informe['new']}">
                        <button class="btn btn-default" type="submit">Add Informe</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Informe</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
