<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicos">
    <h2>
        <c:if test="${medico['new']}">New </c:if> Medico
    </h2>
    <form:form modelAttribute="medico" class="form-horizontal" id="add-medico-form">
        <div class="form-group has-feedback">

            <fmt:message var="nombre" key="nombre"/>
        	<fmt:message var="apellidos" key="apellidos"/>
        	<fmt:message var="domicilio" key="domicilio"/>
        	<fmt:message var="n_telefono" key="n_telefono"/>
            <fmt:message var="DNI" key="DNI"/>
           
            <fmt:message var="Username" key="Username"/>
            <fmt:message var="Password" key="Password"/>

            <petclinic:inputField label="${nombre}" name="nombre"/>
            <petclinic:inputField label="${apellidos}" name="apellidos"/>
            <petclinic:inputField label="${domicilio}" name="domicilio"/>
            <petclinic:inputField label="${n_telefono}" name="n_telefono"/>
            <petclinic:inputField label="${DNI}" name="DNI"/>

            <petclinic:inputField label="Username" name="user.username"/>
            <petclinic:inputField label="Password" name="user.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                
                <c:choose>
                    <c:when test="${medico['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addMedico"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="updateMedico"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
