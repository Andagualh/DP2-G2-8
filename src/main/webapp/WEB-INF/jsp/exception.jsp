<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/skull.jpg" var="petsImage"/>
    <img src="${petsImage}" width="300"/>

    <h2>Something happened...</h2>

    <p>${exception.message}</p>

</petclinic:layout>
