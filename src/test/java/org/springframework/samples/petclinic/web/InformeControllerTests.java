package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.model.Medico;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.InformeRepository;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;

@WebMvcTest(value = InformeController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
        excludeAutoConfiguration= SecurityConfiguration.class)
class InformeControllerTests {

    @Autowired
    private InformeController informeController;
    @MockBean
    private InformeService informeService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthoritiesService authoritiesService;
    @MockBean
    private MedicoService medicoService;
    @MockBean
    private PacienteService pacienteService;
    @MockBean
    private CitaController citaController;
    @MockBean
    private CitaService citaService;
    @MockBean
    private HistoriaClinicaService historiaClinicaService;
    @MockBean
    private TratamientoService tratamientoService;

    @Autowired
    private MockMvc mockMvc;
    
    private static final int TEST_MEDICO_ID = 1; 
    private static final int TEST_PACIENTE_ID = 1;
    private static final int TEST_PACIENTE_ID_2 = 2;
    private static final String TEST_USER_ID = "1";
    private static final int TEST_CITA_ID = 1;
    private static final String TEST_MEDICOUSER_ID = "medico1";
    private static final String TEST_MEDICOUSER_ID_2 = "medico2";
    private static final int TEST_CITA2_ID = 2;
    private static final int TEST_CITA3_ID = 3;
    private static final int TEST_CITA4_ID = 4;
    private static final int TEST_INFORME_ID = 1;


    private Paciente            javier;
    
    private Paciente            pepe;

    private Medico                medico1;

    private User                medico1User;
    
    private Medico                medico2;

    private User                medico2User;

    private Authorities            authorities;

    private Cita    cita1;
    private Cita    cita2;
    private Cita    cita3;
    private Cita    cita4;

    @BeforeEach
    void setup(){
    
        this.medico1 = new Medico();
        this.medico1.setId(TEST_MEDICO_ID);
        this.medico1.setNombre("Medico 1");
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
        
        this.medico2 = new Medico();
        this.medico2.setId(TEST_MEDICO_ID);
        this.medico2.setNombre("Medico 2");
        this.medico2.setApellidos("Apellidos");
        this.medico2.setDNI("12345678Z");
        this.medico2.setN_telefono("123456789");
        this.medico2.setDomicilio("Domicilio");
        
        this.medico2User = new User();
        this.medico2User.setUsername(TEST_MEDICOUSER_ID_2);
        this.medico2User.setPassword("medico2");
        this.medico2User.setEnabled(true);
        
        this.medico2.setUser(this.medico2User);
        this.medico2.getUser().setEnabled(true);
        
        this.authorities = new Authorities();
        this.authorities.setUsername(TEST_MEDICOUSER_ID_2);
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
        
        this.pepe = new Paciente();
        this.pepe.setId(TEST_PACIENTE_ID_2);
        this.pepe.setNombre("Pepe");
        this.pepe.setApellidos("Moreno");
        this.pepe.setF_nacimiento(LocalDate.of(1995, 9, 15));
        this.pepe.setDNI("53279183M");
        this.pepe.setDomicilio("Dos Hermanas");
        this.pepe.setN_telefono(644090431);
        this.pepe.setEmail("pepe_moreno@gmail.com");
        this.pepe.setF_alta(LocalDate.now());
        this.pepe.setMedico(this.medico2);

        this.cita1 = new Cita();
        this.cita1.setFecha(LocalDate.now());
        this.cita1.setPaciente(this.javier);
        this.cita1.setLugar("Lugar");
        this.cita1.setId(TEST_CITA_ID);

        this.cita2 = new Cita();
        this.cita2.setFecha(LocalDate.now().plusDays(1));
        this.cita2.setPaciente(this.javier);
        this.cita2.setLugar("Lugar");
        this.cita2.setId(TEST_CITA2_ID);

        this.cita3 = new Cita();
        this.cita3.setFecha(LocalDate.now().minusDays(2));
        this.cita3.setPaciente(this.javier);
        this.cita3.setLugar("Lugar");
        this.cita3.setId(TEST_CITA3_ID);
        
        this.cita4 = new Cita();
        this.cita4.setFecha(LocalDate.now());
        this.cita4.setPaciente(this.pepe);
        this.cita4.setLugar("Lugar");
        this.cita4.setId(TEST_CITA4_ID);

        
        given(this.medicoService.getMedicoById(TEST_MEDICO_ID)).willReturn(this.medico1);
        given(this.userService.findUserByUsername(TEST_USER_ID)).willReturn(Optional.of(this.medico1User));
        given(this.pacienteService.findPacienteById(TEST_PACIENTE_ID)).willReturn(Optional.of(this.javier));
        given(this.citaService.findCitaById(TEST_CITA_ID)).willReturn(Optional.of(this.cita1));
        given(this.citaService.findCitaById(TEST_CITA2_ID)).willReturn(Optional.of(this.cita2));
        given(this.citaService.findCitaById(TEST_CITA3_ID)).willReturn(Optional.of(this.cita3));
        given(this.informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(new Informe()));
        given(this.userService.getCurrentMedico()).willReturn(this.medico1);
    }

    @WithMockUser(value = "spring")
        @Test
    void testCreateInforme() throws Exception{
        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/createOrUpdateInformeForm"))
        .andExpect(model().attributeExists("informe"));
    }

    @WithMockUser(value = "spring")
        @Test
    void testCreateInformeForCitaWithInforme() throws Exception{

    	Informe informeCita2 = new Informe();
    	informeCita2.setId(2);
    	informeCita2.setMotivo_consulta("Resfriado");
    	informeCita2.setDiagnostico("Reposo");
    	informeCita2.setCita(cita2);
    	cita2.setInforme(informeCita2);
        given(informeService.citaHasInforme(cita2)).willReturn(true);

        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA2_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_CITA2_ID+"/informes/"+cita2.getInforme().getId())
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testCreateInformeForFutureDateForCitaWithInforme() throws Exception{

    	Informe informeCita1 = new Informe();
    	informeCita1.setId(1);
    	informeCita1.setMotivo_consulta("Resfriado");
    	informeCita1.setDiagnostico("Reposo");
    	informeCita1.setCita(cita1);
    	cita1.setInforme(informeCita1);
        given(informeService.citaHasInforme(cita1)).willReturn(true);

        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_CITA_ID+"/informes/"+cita1.getInforme().getId())
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testCreateInformeForFutureDate() throws Exception{
        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA2_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
    );
}

    /*TODO: Comprobar que esta forma de hacer
    el test de Controlador es correcta
    (Usa flashAttr en lugar de introducir los param)*/

    @WithMockUser(value = "spring")
        @Test
    void testSaveInformeSuccess() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");

        mockMvc.perform(post("/citas/{citaId}/informes/new", TEST_CITA_ID)
        .with(csrf())
        /*.param("id", Integer.toString(TEST_INFORME_ID))
        .param("motivo_consulta", "Consulta")
        .param("diagnostico", "diagnostico")
        .param("cita", "${cita}")*/
        .flashAttr("informe", informe)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
        );

    }

    /*TODO: Mismo caso que el anterior
    comprobar que el uso de FlashAttr es
    aceptable para este caso */

    @WithMockUser(value = "spring")
        @Test
    void testSaveInformeHasErrors() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setMotivo_consulta("motivo");
        
        mockMvc.perform(post("/citas/{citaId}/informes/new", TEST_CITA_ID)
        .with(csrf())
        /*.param("id", Integer.toString(TEST_INFORME_ID))
        .param("motivo_consulta", "Consulta")
        .param("diagnostico", "diagnostico")
        .param("cita", "${cita}")*/
        .flashAttr("informe", informe)
        )
        .andExpect(status().isOk())
        .andExpect(view().name("informes/createOrUpdateInformeForm"))
        .andExpect(model().attributeExists("informe")        
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testInitUpdateInforme() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");


        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/edit", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/createOrUpdateInformeForm"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attributeExists("motivo_consulta"))
        .andExpect(model().attributeExists("diagnostico"))
        .andExpect(model().attributeExists("cita"));
    }
    
	    @WithMockUser(value = "spring")
	    @Test
	void testInitUpdateInformeNotAuthorized() throws Exception{
	
	    Informe informe = new Informe();
	    informe.setCita(cita4);
	    informe.setId(TEST_INFORME_ID);
	    informe.setDiagnostico("Diag");
	    informe.setMotivo_consulta("motivo");
	
	
	    given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
	
	    mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/edit", TEST_CITA4_ID, TEST_INFORME_ID))
	    .andExpect(status().isOk())
	    .andExpect(view().name("accessNotAuthorized"));
	}
    
    
    @WithMockUser(value = "spring")
        @Test
    void testSalvarInformeEditSuccess() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");

        mockMvc.perform(post("/citas/{citaId}/informes/{informeId}/edit", TEST_CITA_ID, TEST_INFORME_ID)
        .with(csrf())
        /*.param("id", Integer.toString(TEST_INFORME_ID))
        .param("motivo_consulta", "Consulta")
        .param("diagnostico", "diagnostico")
        .param("cita", "${cita}")*/
        .flashAttr("informe", informe)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
        );
    }
    

    @WithMockUser(value = "spring")
        @Test
    void testSalvarInformeEditHasErrors() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setMotivo_consulta("motivo");
        informe.setDiagnostico("");

        mockMvc.perform(post("/citas/{citaId}/informes/{informeId}/edit", TEST_CITA_ID, TEST_INFORME_ID)
        .with(csrf())
        /*.param("id", Integer.toString(TEST_INFORME_ID))
        .param("motivo_consulta", "Consulta")
        .param("diagnostico", "diagnostico")
        .param("cita", "${cita}")*/
        .flashAttr("informe", informe)
        )
        .andExpect(status().isOk())
        .andExpect(view().name("informes/createOrUpdateInformeForm"))
        .andExpect(model().attributeExists("informe")   
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testBorrarInformeSuccess() throws Exception{
        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
        
        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );

    }
    @WithMockUser(value = "spring")
        @Test
    void testBorrarInformeCantDelete() throws Exception{
        Informe informe = new Informe();
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informe.setHistoriaClinica(new HistoriaClinica());

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
        
        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );

    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarInformeCantDeletePastHC() throws Exception{
        Informe informe = new Informe();
        informe.setCita(cita3);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informe.setHistoriaClinica(new HistoriaClinica());

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
        
        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );

    }

    @WithMockUser(value = "spring")
        @Test
    void testBorrarInformeCantDeletePast() throws Exception{
        Informe informe = new Informe();
        informe.setCita(cita3);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
        
        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );

    }

    @WithMockUser(value = "spring")
        @Test
    void testShowInformeAuthorized() throws Exception{

    	HistoriaClinica hist = new HistoriaClinica();
        hist.setDescripcion("desc");
        hist.setPaciente(this.javier);
        hist.setId(31);
    	
        Informe informe = new Informe();
        //Cita con LocalDate now
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informe.setHistoriaClinica(hist);

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", true)

        );
    }
    
	    @WithMockUser(value = "spring")
	    @Test
	void testShowInformeNotAuthorized() throws Exception{
	
	    Informe informe = new Informe();
	    //Cita con LocalDate now
	    informe.setCita(cita4);
	    informe.setId(TEST_INFORME_ID);
	    informe.setDiagnostico("Diag");
	    informe.setMotivo_consulta("motivo");
	    informe.setHistoriaClinica(null);
	
	    given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
	
	    mockMvc.perform(get("citas/{citaId}/informes/{informeId}",TEST_CITA4_ID, TEST_INFORME_ID))
	    .andExpect(status().is4xxClientError())
	    .andExpect(view().name("accessNotAuthorized"));
	    		
	    }
    
    @WithMockUser(value = "spring")
        @Test
    void testShowInformeDifferentDate() throws Exception{

        Informe informe = new Informe();
        //Cita con LocalDate now minus 2 days
        informe.setCita(cita3);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        HistoriaClinica hc = new HistoriaClinica();
        informe.setHistoriaClinica(hc);

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", false)
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testShowInformeWithHCPast() throws Exception{

        Informe informe = new Informe();
        //Cita con LocalDate now minus 2 days
        informe.setCita(cita3);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        HistoriaClinica hist = new HistoriaClinica();
        hist.setDescripcion("desc");
        hist.setPaciente(this.javier);
        hist.setId(30);
        
        given(historiaClinicaService.findHistoriaClinicaByPaciente(this.javier)).willReturn(hist);
        informe.setHistoriaClinica(hist);

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", false)
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testShowInformeWithHC() throws Exception{

        Informe informe = new Informe();
        //Cita con LocalDate now minus 2 days
        informe.setCita(cita1);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        HistoriaClinica hist = new HistoriaClinica();
        hist.setDescripcion("desc");
        hist.setPaciente(this.javier);
        hist.setId(30);
        
        given(historiaClinicaService.findHistoriaClinicaByPaciente(this.javier)).willReturn(hist);
        informe.setHistoriaClinica(hist);

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", true)
        );
    }
    
    @WithMockUser(value = "spring")
        @Test
    void testAddHistoriaClinicaToInforme() throws Exception{
        Informe informe = new Informe();
        //Cita con LocalDate now minus 2 days
        informe.setCita(cita3);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");

        HistoriaClinica hist = new HistoriaClinica();
        hist.setId(1);
        hist.setDescripcion("Descrip");
        hist.setPaciente(this.javier);

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
        given(historiaClinicaService.findHistoriaClinicaByPaciente(this.javier)).willReturn(hist);

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/addtohistoriaclinica", TEST_CITA3_ID, TEST_INFORME_ID)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/pacientes/"+TEST_PACIENTE_ID + "/historiaclinica")
        );
    }

    @WithMockUser(value = "spring")
        @Test
    void testDeleteHistoriaClinicaToInforme() throws Exception{
        Informe informe = new Informe();
        //Cita con LocalDate now minus 2 days
        informe.setCita(cita3);
        informe.setId(TEST_INFORME_ID);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");

        HistoriaClinica hist = new HistoriaClinica();
        hist.setId(1);
        hist.setDescripcion("Descrip");
        hist.setPaciente(this.javier);

        informe.setHistoriaClinica(hist);

        given(informeService.findInformeById(TEST_INFORME_ID)).willReturn(Optional.of(informe));
       
        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/detelefromhistoriaclinica", TEST_CITA3_ID, TEST_INFORME_ID)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID+ "/informes/" + TEST_INFORME_ID)
        );
    }
    
}