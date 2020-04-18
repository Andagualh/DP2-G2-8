<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="informes">

    <h2>Informe Information</h2>
    <table class="table table-striped">
        <tr>
            <th>Paciente</th>
            <td><b><c:out value="${informe.cita.paciente.apellidos}, ${informe.cita.paciente.nombre}"/></b></td>
        </tr>
        <tr>
            <th>Motivo Consulta</th>
            <td><b><c:out value="${informe.motivo_consulta}"/></b></td>
        </tr>
        <tr>
            <th>Diagnostico</th>
            <td><c:out value="${informe.diagnostico}"/></td>
        </tr>
        <tr>
            <th>Tratamiento</th>
            <td><c:out value="TODO Listar tratamiento"/></td>
        </tr>
    </table>

</petclinic:layout>
