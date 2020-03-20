<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pacientes">
    <h2>
        <c:if test="${paciente['new']}">New </c:if> paciente
    </h2>
    <form:form modelAttribute="paciente" class="form-horizontal" id="add-paciente-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="nombre"/>
            <petclinic:inputField label="Last Name" name="apellidos"/>
            <petclinic:inputField label="Address" name="domicilio"/>
            <petclinic:inputField label="Telephone" name="n_telefono"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${paciente['new']}">
                        <button class="btn btn-default" type="submit">Add paciente</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update paciente</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
