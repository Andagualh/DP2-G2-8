<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="medicos">
	<h2>Medicos</h2>

	<table id="medicosTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Name</th>
				<th style="width: 200px;">Address</th>
				<th style="width: 120px;">Telephone</th>
				<sec:authorize url="/users/accept/">
					<th style="width: 100px">Enabled</th>
				</sec:authorize>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="medico">
				<tr>
					<td><spring:url value="/medicos/{medicoId}" var="medicoUrl">
							<spring:param name="medicoId" value="${medico.id}" />
						</spring:url> <a href="${fn:escapeXml(medicoUrl)}"><c:out value="${medico.nombre} ${medico.apellidos}" /></a></td>
					<td><c:out value="${medico.domicilio}" /></td>
					<td><c:out value="${medico.n_telefono}" /></td>
					<sec:authorize url="/users/accept/">
						<td><c:out value="${medico.user.enabled}" /></td>
					</sec:authorize>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
