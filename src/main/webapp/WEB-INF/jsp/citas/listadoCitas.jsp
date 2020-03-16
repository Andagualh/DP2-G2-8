<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="citas">
    <h2>Citas</h2>

    <table id="citasTable" class="table table-striped">
        <thead>
        <tr>
        	<th>Paciente</th>
            <th style="width: 150px;">Fecha</th>
            <th style="width: 200px;">Lugar</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${citas}" var="cita">
            <tr>
             	<td>
                    <c:out value="${cita.paciente}"/>
                </td>
                <td>
                   <c:out value="${cita.fecha}"/></a>
                </td>
                <td>
                    <c:out value="${cita.lugar}"/>
                </td>               
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
