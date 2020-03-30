<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true" description="Name of the active menu: home, owners, vets or error"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/" title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>
				
				<li><a href="#" class="dropdown-toggle hvr-bounce-to-bottom" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Pacientes<span class="caret"></span></a>
						<ul class="dropdown-menu">
						
							<li><petclinic:menuItem active="${name eq 'pacientes'}" url="/pacientes"
					title="listar pacientes">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Listar Pacientes</span>
				</petclinic:menuItem></li>
				
				<li><petclinic:menuItem active="${name eq 'pacientes'}" url="/pacientes/findByMedico"
					title="listar pacientes personales">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Listar Pacientes Personales</span>
				</petclinic:menuItem></li>
				
				<li><petclinic:menuItem active="${name eq 'pacientes'}" url="/pacientes/crearpaciente"
					title="crear paciente">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Crear Paciente</span>
				</petclinic:menuItem></li>
				
				<li><petclinic:menuItem active="${name eq 'pacientes'}" url="/pacientes/find"
					title="buscar paciente">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Buscar Paciente</span>
				</petclinic:menuItem></li>
							
						</ul></li>
						
				<li><a href="#" class="dropdown-toggle hvr-bounce-to-bottom" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Citas<span class="caret"></span></a>
						<ul class="dropdown-menu">
						
							<li><petclinic:menuItem active="${name eq 'citas'}" url="/citas"
					title="listar citas">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Listar Citas Personales</span>
				</petclinic:menuItem></li>
				
				<li><petclinic:menuItem active="${name eq 'citas'}" url="/citas/find"
					title="listar citas por fecha">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Listar Citas por Fecha</span>
				</petclinic:menuItem></li>
							
						</ul></li>

				<petclinic:menuItem active="${name eq 'error'}" url="/oups" title="trigger a RuntimeException to see how it is handled">
					<span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
					<span>Error</span>
				</petclinic:menuItem>

			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <span
							class="glyphicon glyphicon-user"></span> <strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />" class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
							<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
