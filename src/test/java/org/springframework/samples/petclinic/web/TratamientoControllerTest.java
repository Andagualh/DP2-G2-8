package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.Tratamiento;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TratamientoController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class),excludeAutoConfiguration= SecurityConfiguration.class)
public class TratamientoControllerTest {
	
	private static final int TEST_MEDICO_ID = 1; 
    private static final int TEST_PACIENTE_ID = 1;
    private static final String TEST_USER_ID = "1";
    private static final int TEST_CITA_ID = 1;
    private static final String TEST_MEDICOUSER_ID = "medico1";
    private static final int TEST_INFORME_ID = 1;
	private static final int TEST_TRATAMIENTO_ID = 1;
	private static final int TEST_TRATAMIENTO_ID2 = 2;
	
	@Autowired
	private TratamientoController tratamientoController;
	
    @MockBean
    private CitaService citaService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthoritiesService authoritiesService;
    @MockBean
    private MedicoService medicoService;
    @MockBean
    private PacienteService pacienteService;
    @MockBean
	private InformeService informeService;
	@MockBean
	private TratamientoService tratamientoService;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	private Paciente            javier;

    private Medico              medico1;

    private User                medico1User;

    private Authorities         authorities;
    
    private Cita                cita1;
    
    private Informe             informe1;
	
	private Tratamiento         tratamiento;
	
	private Tratamiento         tratamiento2;
	
	

	@BeforeEach
	void setup() {
		
		this.medico1 = new Medico();
        this.medico1.setId(TEST_MEDICO_ID);
        this.medico1.setNombre("Medico 2");
        this.medico1.setApellidos("Apellidos");
        this.medico1.setDNI("12345678Z");
        this.medico1.setN_telefono("123456789");
        this.medico1.setDomicilio("Domicilio");
        
        this.medico1User = new User();
        this.medico1User.setUsername(TEST_MEDICOUSER_ID);
        this.medico1User.setPassword("medico1");
        this.medico1User.setEnabled(true);
        
        this.medico1.setUser(this.medico1User);
        this.medico1.getUser().setEnabled(true);
        
        this.authorities = new Authorities();
        this.authorities.setUsername(TEST_MEDICOUSER_ID);
        this.authorities.setAuthority("medico");

        this.javier = new Paciente();
        this.javier.setId(TEST_PACIENTE_ID);
        this.javier.setNombre("Javier");
        this.javier.setApellidos("Silva");
        this.javier.setF_nacimiento(LocalDate.of(1997, 6, 8));
        this.javier.setDNI("12345678Z");
        this.javier.setDomicilio("Ecija");
        this.javier.setN_telefono(612345987);
        this.javier.setEmail("javier_silva@gmail.com");
        this.javier.setF_alta(LocalDate.now());
        this.javier.setMedico(this.medico1);
        
        this.cita1 = new Cita();
        this.cita1.setId(TEST_CITA_ID);
        this.cita1.setName("nombreTest");
        this.cita1.setLugar("lugarTest");
        this.cita1.setFecha(LocalDate.parse("2020-06-15"));
        this.cita1.setPaciente(javier);
        
        this.informe1 = new Informe();
        this.informe1.setId(TEST_INFORME_ID);
        this.informe1.setDiagnostico("diagnostico test");
        this.informe1.setHistoriaClinica(null);
        this.informe1.setMotivo_consulta("motivo test");
        this.informe1.setCita(cita1);
        

		this.tratamiento = new Tratamiento();
		this.tratamiento.setId(TEST_TRATAMIENTO_ID);
		this.tratamiento.setMedicamento("medicamento de prueba");
		this.tratamiento.setDosis("dosis de prueba");
		this.tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		this.tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));
		this.tratamiento.setInforme(informe1);
		
		this.tratamiento2 = new Tratamiento();
		this.tratamiento2.setId(TEST_TRATAMIENTO_ID);
		this.tratamiento2.setMedicamento("medicamento de prueba 2");
		this.tratamiento2.setDosis("dosis de prueba 2");
		this.tratamiento2.setF_inicio_tratamiento(LocalDate.parse("2020-01-22"));
		this.tratamiento2.setF_fin_tratamiento(LocalDate.parse("2020-02-22"));
		this.tratamiento2.setInforme(informe1);
		
		BDDMockito.given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(this.medico1);
		BDDMockito.given(this.userService.findUserByUsername(TEST_USER_ID)).willReturn(Optional.of(this.medico1User));
		BDDMockito.given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
		BDDMockito.given(this.citaService.findCitaById(TEST_CITA_ID)).willReturn(Optional.of(this.cita1));
		BDDMockito.given(this.informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(this.informe1));
		BDDMockito.given(this.tratamientoService.findTratamientoById(TEST_TRATAMIENTO_ID)).willReturn(Optional.of(tratamiento));
		BDDMockito.given(this.tratamientoService.findTratamientoById(TEST_TRATAMIENTO_ID2)).willReturn(Optional.of(tratamiento2));
		BDDMockito.given(this.userService.getCurrentMedico()).willReturn(this.medico1);
	}
	 
	@WithMockUser(value = "spring")
	@Test
	void testInit() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", TEST_TRATAMIENTO_ID))
				.andExpect(status().isOk());
	}
	
	@WithMockUser(username="medico1",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoForm() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", TEST_TRATAMIENTO_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("tratamiento"))
				.andExpect(model().attribute("tratamiento", hasProperty("medicamento", is("medicamento de prueba"))))
				.andExpect(model().attribute("tratamiento", hasProperty("dosis", is("dosis de prueba"))))
				.andExpect(model().attribute("tratamiento", hasProperty("f_inicio_tratamiento", is(LocalDate.parse("2020-04-22")))))
				.andExpect(model().attribute("tratamiento", hasProperty("f_fin_tratamiento", is(LocalDate.parse("2020-10-22")))))
				.andExpect(model().attribute("tratamiento", hasProperty("informe", is(this.informe1))))
				.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
	}
	
	@WithMockUser(username="medico1",authorities= {"medico"})
	@Test
	void testInitCreateTratamientoFormSuccess() throws Exception {
		mockMvc.perform(get("/tratamientos/new/{informeId}", TEST_INFORME_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("tratamiento"))
				.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
		
	}
	
	@WithMockUser(username="medico1",authorities= {"medico"})
	@Test
	void testEditTratamientoNoVigente() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", TEST_TRATAMIENTO_ID2))
				//.andExpect(status().isOk())   Redirige bien pero no es ok
				.andExpect(view().name("redirect:/"));
	}
	
	// En este test no coincide la view que deberia devolverse
    @WithMockUser(value = "spring")
	@Test
	void testSaveSuccessTratamiento() throws Exception {
        
    	 mockMvc.perform(post("/tratamientos/save")
				 .with(csrf())
				 .param("dosis", "medicamento de prueba")
				 .param("medicamento", "dosis de prueba")
				 .param("f_inicio_tratamiento", "2020-04-22")
				 .param("f_fin_tratamiento", "2020-10-22")
				 
    	 		 .param("informe.id",Integer.toString(TEST_INFORME_ID))
    	 		 .param("informe.motivo_consulta","motivoTest")
    	 		 .param("informe.diagnostico","diagTest")
    	 		 
    	 		 .param("informe.cita.id",Integer.toString(TEST_CITA_ID))
    	 		 .param("informe.cita.fecha","2020-08-10")
    	 		 .param("informe.cita.lugar","lugarTest")
    	 		 
    	 		 .param("informe.cita.paciente.id", Integer.toString(TEST_PACIENTE_ID))
    	 		 .param("informe.cita.paciente.nombre", "test")
		         .param("informe.cita.paciente.apellidos", "test")
		         .param("informe.cita.paciente.f_nacimiento", "1997/09/09")
		         .param("informe.cita.paciente.f_alta", "2020/08/08")
		         .param("informe.cita.paciente.DNI", "12345689Q")
		         .param("informe.cita.paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
		         .param("informe.cita.paciente.medico.nombre", "test")
		         .param("informe.cita.paciente.medico.apellidos", "test")
		         .param("informe.cita.paciente.medico.domicilio", "test")
		         .param("informe.cita.paciente.medico.user.username", "test")
		         .param("informe.cita.paciente.medico.user.password", "test"))
		//.andExpect(status().isOk())    redirije pero no es ok
		//.andExpect(status().is2xxSuccessful());
		.andExpect(view().name("redirect:/citas/1/informes/1"));	
	}
    
    @WithMockUser(value = "spring")
	@Test
    void testSaveTratamientoNullFecha() throws Exception{
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        		.param("dosis", "medicamento de prueba")
        		.param("medicamento", "dosis de prueba")
        		.param("f_inicio_tratamiento", "2020-04-22")
        		
        		.param("informe.id",Integer.toString(TEST_INFORME_ID))
   	 		 	.param("informe.motivo_consulta","motivoTest")
   	 		 	.param("informe.diagnostico","diagTest")
   	 		 
   	 		 	.param("informe.cita.id",Integer.toString(TEST_CITA_ID))
   	 		 	.param("informe.cita.fecha","2020-08-10")
   	 		 	.param("informe.cita.lugar","lugarTest")
   	 		 
   	 		 	.param("informe.cita.paciente.id", Integer.toString(TEST_PACIENTE_ID))
   	 		 	.param("informe.cita.paciente.nombre", "test")
		         .param("informe.cita.paciente.apellidos", "test")
		         .param("informe.cita.paciente.f_nacimiento", "1997/09/09")
		         .param("informe.cita.paciente.f_alta", "2020/08/08")
		         .param("informe.cita.paciente.DNI", "12345689Q")
		         .param("informe.cita.paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
		         .param("informe.cita.paciente.medico.nombre", "test")
		         .param("informe.cita.paciente.medico.apellidos", "test")
		         .param("informe.cita.paciente.medico.domicilio", "test")
		         .param("informe.cita.paciente.medico.user.username", "test")
		         .param("informe.cita.paciente.medico.user.password", "test"))
        
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
        .andExpect(status().isOk())
        .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm")
        ); 
    	}
        
        @WithMockUser(value = "spring")
    	@Test
        void testSaveTratamientoFechaFinPasado() throws Exception{
            mockMvc.perform(post("/tratamientos/save")
            		.with(csrf())
            		.param("dosis", "medicamento de prueba")
            		.param("medicamento", "dosis de prueba")
            		.param("f_inicio_tratamiento", "2020-04-22")
            		.param("f_fin_tratamiento", "2020-01-10")
            		
            		.param("informe.id",Integer.toString(TEST_INFORME_ID))
       	 		 	.param("informe.motivo_consulta","motivoTest")
       	 		 	.param("informe.diagnostico","diagTest")
       	 		 
       	 		 	.param("informe.cita.id",Integer.toString(TEST_CITA_ID))
       	 		 	.param("informe.cita.fecha","2020-08-10")
       	 		 	.param("informe.cita.lugar","lugarTest")
       	 		 
       	 		 	.param("informe.cita.paciente.id", Integer.toString(TEST_PACIENTE_ID))
       	 		 	.param("informe.cita.paciente.nombre", "test")
    		        .param("informe.cita.paciente.apellidos", "test")
    		        .param("informe.cita.paciente.f_nacimiento", "1997/09/09")
    		        .param("informe.cita.paciente.f_alta", "2020/08/08")
    		        .param("informe.cita.paciente.DNI", "12345689Q")
    		        .param("informe.cita.paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
    		        .param("informe.cita.paciente.medico.nombre", "test")
    		        .param("informe.cita.paciente.medico.apellidos", "test")
    		        .param("informe.cita.paciente.medico.domicilio", "test")
    		        .param("informe.cita.paciente.medico.user.username", "test")
    		        .param("informe.cita.paciente.medico.user.password", "test"))
            
            //La fecha esta en pasado
            .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
            .andExpect(status().isOk())
            .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm")
            );
        }
        
        @WithMockUser(value = "spring")
    	@Test
        void testSaveTratamientoFechaInicioFuturo() throws Exception{
            mockMvc.perform(post("/tratamientos/save")
            		.with(csrf())
            		.param("dosis", "")
            		.param("medicamento", "")
            		.param("f_inicio_tratamiento", "")
            		.param("f_fin_tratamiento", "")
            		
            		.param("informe.id",Integer.toString(TEST_INFORME_ID))
       	 		 	.param("informe.motivo_consulta","motivoTest")
       	 		 	.param("informe.diagnostico","diagTest")
       	 		 
       	 		 	.param("informe.cita.id",Integer.toString(TEST_CITA_ID))
       	 		 	.param("informe.cita.fecha","2020-08-10")
       	 		 	.param("informe.cita.lugar","lugarTest")
       	 		 
       	 		 	.param("informe.cita.paciente.id", Integer.toString(TEST_PACIENTE_ID))
       	 		 	.param("informe.cita.paciente.nombre", "test")
    		        .param("informe.cita.paciente.apellidos", "test")
    		        .param("informe.cita.paciente.f_nacimiento", "1997/09/09")
    		        .param("informe.cita.paciente.f_alta", "2020/08/08")
    		        .param("informe.cita.paciente.DNI", "12345689Q")
    		        .param("informe.cita.paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
    		        .param("informe.cita.paciente.medico.nombre", "test")
    		        .param("informe.cita.paciente.medico.apellidos", "test")
    		        .param("informe.cita.paciente.medico.domicilio", "test")
    		        .param("informe.cita.paciente.medico.user.username", "test")
    		        .param("informe.cita.paciente.medico.user.password", "test"))
            
            .andExpect(model().attributeHasFieldErrors("tratamiento","f_inicio_tratamiento"))
            .andExpect(status().isOk())
            .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
        }
        
        @WithMockUser(value = "spring")
    	@Test
        void testSaveTratamientoNull() throws Exception{
            mockMvc.perform(post("/tratamientos/save")
            		.with(csrf())
            		.param("dosis", "")
            		.param("medicamento", "")
            		.param("f_inicio_tratamiento", "")
            		.param("f_fin_tratamiento", "")
            		
            		.param("informe.id",Integer.toString(TEST_INFORME_ID))
       	 		 	.param("informe.motivo_consulta","motivoTest")
       	 		 	.param("informe.diagnostico","diagTest")
       	 		 
       	 		 	.param("informe.cita.id",Integer.toString(TEST_CITA_ID))
       	 		 	.param("informe.cita.fecha","2020-08-10")
       	 		 	.param("informe.cita.lugar","lugarTest")
       	 		 
       	 		 	.param("informe.cita.paciente.id", Integer.toString(TEST_PACIENTE_ID))
       	 		 	.param("informe.cita.paciente.nombre", "test")
    		        .param("informe.cita.paciente.apellidos", "test")
    		        .param("informe.cita.paciente.f_nacimiento", "1997/09/09")
    		        .param("informe.cita.paciente.f_alta", "2020/08/08")
    		        .param("informe.cita.paciente.DNI", "12345689Q")
    		        .param("informe.cita.paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
    		        .param("informe.cita.paciente.medico.nombre", "test")
    		        .param("informe.cita.paciente.medico.apellidos", "test")
    		        .param("informe.cita.paciente.medico.domicilio", "test")
    		        .param("informe.cita.paciente.medico.user.username", "test")
    		        .param("informe.cita.paciente.medico.user.password", "test"))
            
            .andExpect(model().attributeHasFieldErrors("tratamiento","dosis"))
            .andExpect(model().attributeHasFieldErrors("tratamiento","medicamento"))
            .andExpect(model().attributeHasFieldErrors("tratamiento","f_inicio_tratamiento"))
            .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
            .andExpect(status().isOk())
            .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
        }

}
