package org.springframework.samples.petclinic.web.e2e;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Informe;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.HistoriaClinicaService;
import org.springframework.samples.petclinic.service.InformeService;
import org.springframework.samples.petclinic.service.MedicoService;
import org.springframework.samples.petclinic.service.PacienteService;
import org.springframework.samples.petclinic.service.TratamientoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.CitaController;
import org.springframework.samples.petclinic.web.InformeController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TestInformeControladorE2E {

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private CitaService citaService;
    @Autowired
    private InformeService informeService;
    @Autowired
    private HistoriaClinicaService hcService;

    @Autowired
    private MockMvc mockMvc;

    private static final int TEST_MEDICO_ID = 1;
    private static final int TEST_PACIENTE_ID = 1;
    private static final String TEST_USER_ID = "1";
    private static final String TEST_MEDICOUSER_ID = "1";

    private int TEST_INFORME_ID;
    private int TEST_CITA_ID;
    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = new Cita();
        cita.setFecha(LocalDate.now());
        cita.setLugar("Lugar");
        cita.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
        citaService.save(cita);
        TEST_CITA_ID = cita.getId();

    }

    @AfterEach
    void undo() throws DataAccessException, IllegalAccessException {
        citaService.delete(cita);
        TEST_INFORME_ID = 0;
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testCreateInforme() throws Exception{
        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/createOrUpdateInformeForm"))
        .andExpect(model().attributeExists("informe"));
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testCreateInformeForCitaDifferentDateWithInforme() throws Exception{

        Cita citaTemp = citaService.findCitaById(1).get();

        mockMvc.perform(get("/citas/{citaId}/informes/new", citaTemp.getId()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
        );
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testCreateInformeForCitaDifferentDate() throws Exception{

        Cita cita2 = new Cita();
        cita2.setFecha(LocalDate.now().minusDays(1));
        cita2.setLugar("Lugar");
        cita2.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
        citaService.save(cita2);
        int TEST_CITA2_ID = cita2.getId();

        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA2_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
        );

        citaService.delete(cita2);
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testCreateInformeCitaWithInforme() throws Exception{

        Cita cita2 = new Cita();
        cita2.setFecha(LocalDate.now());
        cita2.setLugar("Lugar");
        cita2.setPaciente(this.pacienteService.findPacienteById(TEST_PACIENTE_ID).get());
        citaService.save(cita2);
        int TEST_CITA2_ID = cita2.getId();

        Informe informe = new Informe();
        informe.setCita(cita2);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informeService.saveInforme(informe);

        mockMvc.perform(get("/citas/{citaId}/informes/new", TEST_CITA2_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
        );

        informeService.deleteInforme(informe.getId());
        citaService.delete(cita2);
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testSaveInformeSuccess() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita);
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

        informeService.deleteInforme(informe.getId());
            
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testSaveInformeHasErrors() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita);
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

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testInitUpdateInforme() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/edit", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/createOrUpdateInformeForm"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attributeExists("motivo_consulta"))
        .andExpect(model().attributeExists("diagnostico"))
        .andExpect(model().attributeExists("cita"));

        TEST_INFORME_ID = 0;
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testSalvarInformeEditSuccess() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
        informe.setDiagnostico("diagnosticoTest");
        informe.setMotivo_consulta("motivoTest");

        mockMvc.perform(post("/citas/{citaId}/informes/{informeId}/edit", TEST_CITA_ID, TEST_INFORME_ID)
        .with(csrf())
        .flashAttr("informe", informe)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID)
        );
        informeService.deleteInforme(TEST_INFORME_ID);
        TEST_INFORME_ID = 0;
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testSalvarInformeEditHasErrors() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita);
        informe.setMotivo_consulta("motivo");
        informe.setDiagnostico("Diagnostico");
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
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
        informeService.deleteInforme(TEST_INFORME_ID);
        TEST_INFORME_ID = 0;    
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testBorrarInformeSuccess() throws Exception{
        Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");

        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
        
        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );
        TEST_INFORME_ID = 0;   
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testBorrarInformeCantDelete() throws Exception{
        Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
        
        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );
        TEST_INFORME_ID = 0; 
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testBorrarInformeCantDeletePastHC() throws Exception{
        Informe informe = informeService.findInformeById(3).get();
        TEST_INFORME_ID = informe.getId();
        Cita pastCita = informe.getCita();
        int TEST_CITA3_ID = pastCita.getId();
        informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(pastCita.getPaciente()));
        informeService.saveInformeWithHistoriaClinica(informe);

        mockMvc.perform(get("/citas/{citaId}/informes/delete/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/")
        );
        TEST_INFORME_ID = 0;
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testShowInforme() throws Exception{

        Informe informe = new Informe();
        //Cita con LocalDate now
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        TEST_CITA_ID = informe.getCita().getId();
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();


        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", false))
        .andExpect(model().attribute("canbeedited", true)

        );
        informeService.deleteInforme(TEST_INFORME_ID);
        TEST_INFORME_ID = 0;
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testShowInformeDifferentDate() throws Exception{

        Informe informe =informeService.findInformeById(3).get();
        TEST_INFORME_ID = informe.getId();
        int TEST_CITA3_ID = informe.getCita().getId();


        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", false)
        );
        TEST_INFORME_ID = 0;
        
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testShowInformeWithHCPast() throws Exception{

        Informe informe = informeService.findInformeById(3).get();
        TEST_INFORME_ID = informe.getId();
        Cita pastCita = informe.getCita();
        int TEST_CITA3_ID = pastCita.getId();
        informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(pastCita.getPaciente()));
        informeService.saveInformeWithHistoriaClinica(informe);


        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA3_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", false)
        );
        TEST_INFORME_ID = 0;
        
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testShowInformeWithHC() throws Exception{

        Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
        
        informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));
        informeService.saveInformeWithHistoriaClinica(informe);

        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}", TEST_CITA_ID, TEST_INFORME_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("informes/informeDetails"))
        .andExpect(model().attributeExists("informe"))
        .andExpect(model().attribute("cannotbedeleted", true))
        .andExpect(model().attribute("canbeedited", true)
        );

        informeService.deleteInformeToHistoriaClinica(informe);
        informeService.deleteInforme(TEST_INFORME_ID);
        TEST_INFORME_ID = 0;
        
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testAddHistoriaClinicaToInforme() throws Exception{
        Informe informe = new Informe();
       
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
        informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));

    
        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/addtohistoriaclinica", TEST_CITA_ID, TEST_INFORME_ID)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/pacientes/"+TEST_PACIENTE_ID + "/historiaclinica")
        );

        informeService.deleteInformeToHistoriaClinica(informe);
        informeService.deleteInforme(TEST_INFORME_ID);
        TEST_INFORME_ID = 0;
    }

    @WithMockUser(username="alvaroMedico", authorities={"medico"})
        @Test
    void testDeleteHistoriaClinicaToInforme() throws Exception{
        Informe informe = new Informe();
       
        informe.setCita(cita);
        informe.setDiagnostico("Diag");
        informe.setMotivo_consulta("motivo");
        informeService.saveInforme(informe);
        TEST_INFORME_ID = informe.getId();
        informe.setHistoriaClinica(hcService.findHistoriaClinicaByPaciente(cita.getPaciente()));
        informeService.saveInformeWithHistoriaClinica(informe);


        mockMvc.perform(get("/citas/{citaId}/informes/{informeId}/detelefromhistoriaclinica", TEST_CITA_ID, TEST_INFORME_ID)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/citas/"+TEST_MEDICO_ID+ "/informes/" + TEST_INFORME_ID)
        );
        
        informeService.deleteInforme(TEST_INFORME_ID);
        TEST_INFORME_ID = 0;    
    }

}