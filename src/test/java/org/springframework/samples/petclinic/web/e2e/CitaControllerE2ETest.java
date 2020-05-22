package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.CitaController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(
  locations = "classpath:application-mysql.properties")*/
public class CitaControllerE2ETest {

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private CitaService citaService;

	@Autowired
    private MockMvc mockMvc;
    
    private static final int TEST_MEDICO_ID = 1; 
    private static final int TEST_PACIENTE_ID = 1;
    private static final String TEST_USER_ID = "1";
    private static final int TEST_CITA_ID = 1;

	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testCrearCita() throws Exception{
        mockMvc.perform(get("/citas/new/{pacienteId}", TEST_PACIENTE_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm"))
        .andExpect(model().attributeExists("cita"));
    }

    //Prueba cuando intentas crear una cita para un paciente que no es tuyo

    @WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testCrearCitaforPacienteAnotherMedico() throws Exception{
        mockMvc.perform(get("/citas/new/{pacienteId}", 7))
        .andExpect(status().isOk())
        .andExpect(view().name("accessNotAuthorized"));
    }

	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testSalvarCitaSuccess() throws Exception{
        mockMvc.perform(post("/citas/save")
            .with(csrf())
            .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
            .param("paciente.nombre", "test")
            .param("paciente.apellidos", "test")
            .param("paciente.f_nacimiento", "1997/09/09")
            .param("paciente.f_alta", "2020/08/08")
            .param("paciente.DNI", "12345689Q")
            .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
            .param("paciente.medico.nombre", "test")
            .param("paciente.medico.apellidos", "test")
            .param("paciente.medico.domicilio", "test")
            .param("paciente.medico.user.username", "test")
            .param("paciente.medico.user.password", "test")
            .param("fecha","2020-08-08")
            .param("lugar","Seville"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/citas")
        );
    }

	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testSalvarCitaHasErrors() throws Exception{
        mockMvc.perform(post("/citas/save")
        .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
        .param("fecha", "2020-09-09")
        .param("lugar",""))
        .andExpect(model().attributeHasErrors("cita"))
        .andExpect(status().isOk())
        .andExpect(view().name("citas/createOrUpdateCitaForm")
        );
    }

    //Este caso se da cuando un paciente ya tiene una cita ese mismo día
    @WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testSalvarCitaWhenPacienteAlreadyHasThatFecha() throws Exception{
        mockMvc.perform(post("/citas/save")
            .with(csrf())
            .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
            .param("paciente.nombre", "test")
            .param("paciente.apellidos", "test")
            .param("paciente.f_nacimiento", "1997/09/09")
            .param("paciente.f_alta", "2020/08/08")
            .param("paciente.DNI", "12345689Q")
            .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
            .param("paciente.medico.nombre", "test")
            .param("paciente.medico.apellidos", "test")
            .param("paciente.medico.domicilio", "test")
            .param("paciente.medico.user.username", "test")
            .param("paciente.medico.user.password", "test")
            .param("fecha","2020-08-08")
            .param("lugar","Seville"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/citas")
        );

        mockMvc.perform(post("/citas/save")
            .with(csrf())
            .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
            .param("paciente.nombre", "test")
            .param("paciente.apellidos", "test")
            .param("paciente.f_nacimiento", "1997/09/09")
            .param("paciente.f_alta", "2020/08/08")
            .param("paciente.DNI", "12345689Q")
            .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
            .param("paciente.medico.nombre", "test")
            .param("paciente.medico.apellidos", "test")
            .param("paciente.medico.domicilio", "test")
            .param("paciente.medico.user.username", "test")
            .param("paciente.medico.user.password", "test")
            .param("fecha","2020-08-08")
            .param("lugar","Seville"))
            .andExpect(status().isOk())
            .andExpect(view().name("citas/createOrUpdateCitaForm")
        );
    }
    
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	    @Test
	void testSalvarCitaPastDate() throws Exception{
	    mockMvc.perform(post("/citas/save")
	    .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
	    .param("fecha", "2019-01-01")
	    .param("lugar","Seville"))
	    .andExpect(model().attribute("message", "La fecha debe estar en presente o futuro"))
	    .andExpect(status().isOk())
	    .andExpect(view().name("citas/createOrUpdateCitaForm")
	    );
	}
    	    
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	    @Test
	void testSalvarCitaHasErrorsCoverage() throws Exception{
	    mockMvc.perform(post("/citas/save")
	    .with(csrf())
        .param("paciente.id", Integer.toString(TEST_PACIENTE_ID))
        .param("paciente.nombre", "test")
        .param("paciente.apellidos", "test")
        .param("paciente.f_nacimiento", "1997/09/09")
        .param("paciente.f_alta", "2020/08/08")
        .param("paciente.DNI", "12345689Q")
        .param("paciente.medico.id", Integer.toString(TEST_MEDICO_ID))
        .param("paciente.medico.nombre", "test")
        .param("paciente.medico.apellidos", "test")
        .param("paciente.medico.domicilio", "test")
        .param("paciente.medico.user.username", "test")
        .param("paciente.medico.user.password", "test")
	    .param("fecha", "2019-01-01")
	    .param("lugar",""))
	    .andExpect(model().attributeHasErrors("cita"))
	    .andExpect(model().attributeHasFieldErrors("cita", "lugar"))
	    .andExpect(status().isOk())
	    .andExpect(view().name("citas/createOrUpdateCitaForm")
	    );
	}
        
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testBorrarCitas() throws Exception{
        mockMvc.perform(get("/citas/delete/{citaId}", TEST_CITA_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testBorrarCitaNoPresente() throws Exception{
        mockMvc.perform(get("/citas/delete/{citaId}", 2))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas")
        );
    }

    @WithMockUser(username="andresMedico",authorities= {"medico"})
        @Test
    void testBorrarCitasPacienteDifferentMedico() throws Exception{ 
        mockMvc.perform(get("/citas/delete/{citaId}", 1))
        //.andExpect(model().attributeExists("message"))
        .andExpect(status().isOk())
        .andExpect(view().name("accessNotAuthorized")
        );
    }
    
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testInitFindForm() throws Exception{
        mockMvc.perform(
        get("/citas/find"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("cita"))
        .andExpect(view().name("citas/findCitas")
        );
    }
    
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testProcessFindFormSuccess() throws Exception{
		mockMvc.perform(
        get("/citas/porfecha")
        .param("fecha", "2020-03-09"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("selections"))
        .andExpect(view().name("citas/listCitas")
        );
    }

	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	    @Test
	void testProcessFindFormNoDate() throws Exception{
        Cita cita1 = new Cita();
        cita1.setFecha(LocalDate.now());
        cita1.setLugar("LugarTest");
        cita1.setPaciente(this.pacienteService.findPacienteById(1).get());
        this.citaService.save(cita1);
        
        mockMvc.perform(
		    get("/citas/porfecha"))
		    .andExpect(status().isOk())
		    .andExpect(model().attributeExists("selections"))
		    .andExpect(view().name("citas/listCitas")
        );
        this.citaService.delete(cita1);
	}
    
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testProcessFindFormNoCitaFound() throws Exception{
        mockMvc.perform(
            get("/citas/porfecha")
            .param("fecha", "2020-08-07"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("cita", "fecha"))
			.andExpect(model().attributeHasFieldErrorCode("cita", "fecha", "error.citaNotFound"))
            .andExpect(view().name("citas/findCitas")
            );
    }  
    //Existe una cita para otro medico (AlvaroMedico) en esta misma fecha
    @WithMockUser(username="andresMedico",authorities= {"medico"})
        @Test
    void testProcessFindFormNoCitaFoundForThisMedicoOnDate() throws Exception{
        mockMvc.perform(
            get("/citas/porfecha")
            .param("fecha", "2020-03-09"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("cita", "fecha"))
			.andExpect(model().attributeHasFieldErrorCode("cita", "fecha", "error.citaNotFound"))
            .andExpect(view().name("citas/findCitas")
            );
    }  

	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void testInitList() throws Exception{
        mockMvc.perform(
            get("/citas"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/citas/" + TEST_USER_ID)
        );
    }
    
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
        @Test
    void listadoCitasSuccess() throws Exception{
        mockMvc.perform(
            get("/citas/{medicoId}", TEST_MEDICO_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("selections"))
            .andExpect(view().name("citas/listCitas")
        );
    }

    @WithMockUser(username="andresMedico",authorities= {"medico"})
        @Test
    void listadoCitasOfOtherMedico() throws Exception{ 
        
        mockMvc.perform(
            get("/citas/{medicoId}", TEST_MEDICO_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("accessNotAuthorized")
        );
    }
    
	@WithMockUser(username="pedroMedico",authorities= {"medico"})
        @Test
    void listadoCitasIsEmpty() throws Exception{
        mockMvc.perform(
            get("/citas/{medicoId}", 4))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/")
        );
    }
	
}
