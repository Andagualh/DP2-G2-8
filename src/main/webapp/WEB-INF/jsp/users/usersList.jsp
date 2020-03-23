<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="users">
	<h2>Users</h2>

	<table id="usersTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Username</th>
				<th style="width: 200px;">Status</th>
				<th style="width: 120px">Medico</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${selections}" var="user">
				<tr>
					<td>
					<c:out value="${user.username}" />
					</td>
					<td><c:out value="${user.enabled}" /></td>
					<td><c:out value="${user.enabled}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</petclinic:layout>
