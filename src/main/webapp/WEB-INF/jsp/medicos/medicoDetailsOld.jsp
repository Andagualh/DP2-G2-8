<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="medicos">

	<h2>Información Médico</h2>


	<table class="table table-striped">
		<tr>
			<th>Nombre</th>
			<td><c:out value="${medico.nombre} ${medico.apellidos}" /></td>
		</tr>

		<tr>
			<th>DNI</th>
			<td><c:out value="${medico.DNI}" /></td>
		</tr>

		<tr>
			<th>Número de Teléfono</th>
			<td><c:out value="${medico.n_telefono}" /></td>
		</tr>

		<tr>
			<th>Dirección</th>
			<td><c:out value="${medico.domicilio}" /></td>
		</tr>
		<sec:authorize url="/users/accept/">

			<tr>
				<th>Username</th>
				<td><c:out value="${medico.user.username}" /></td>
			</tr>
			<tr>
				<th>Status</th>
				<td><c:out value="${medico.user.enabled}" /></td>
			</tr>


			<c:if test="${medico.user.enabled.equals(false)}">
				<spring:url value="/users/accept/{username}" var="acceptUrl">
					<spring:param name="username" value="${medico.user.username}" />
				</spring:url>
				<a href="${fn:escapeXml(acceptUrl)}" class="btn btn-default">Accept</a>
			</c:if>
		</sec:authorize>
	</table>
	<sec:authorize url="/users/deny/">
		<c:if test="${medico.user.enabled.equals(true)}">
			<spring:url value="/users/deny/{username}" var="denyUrl">
				<spring:param name="username" value="${medico.user.username}" />
			</spring:url>
			<a href="${fn:escapeXml(denyUrl)}" class="btn btn-default">Deny</a>
		</c:if>
	</sec:authorize>

</petclinic:layout>