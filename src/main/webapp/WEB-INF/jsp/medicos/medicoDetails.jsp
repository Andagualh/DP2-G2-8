<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="medicos">

	<h2>Informaci�n M�dico</h2>


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
			<th>N�mero de Tel�fono</th>
			<td><c:out value="${medico.n_telefono}" /></td>
		</tr>

		<tr>
			<th>Direcci�n</th>
			<td><c:out value="${medico.domicilio}" /></td>
		</tr>
	</table>

</petclinic:layout>