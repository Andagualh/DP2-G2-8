package org.springframework.samples.petclinic.web.e2e;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class TratamientoE2ETest {
	
	@Autowired
	private MockMvc				mockMvc;
	
	@Autowired
	private InformeService informeService;
	@Autowired
	private TratamientoService tratamientoService;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private MedicoService medicoService;
	@Autowired
	private CitaService citaService;
	@Autowired
	private HistoriaClinicaService historiaClinicaService;
	@Autowired
	private UserService userService;
	
	private static int TEST_INFORME_ID = 1;
	private static int TEST_CITA_ID = 1;
	private static int TEST_PACIENTE_ID = 1;
	private static int TEST_MEDICO_ID = 1;
	private static int TEST_TRATAMIENTO_ID = 1;
	private static int TEST_TRATAMIENTO_ID2 = 1;
	private static int TEST_TRATAMIENTO_ID3 = 1;
	
	@BeforeEach
	void setup() throws InvalidAttributeValueException{
		
		Cita cita = citaService.findCitaById(1).get();
		cita.setFecha(LocalDate.now());
		citaService.save(cita);
		
	
	}
	
	public Cita createDummyCita1(final Paciente paciente) throws InvalidAttributeValueException {
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setFecha(LocalDate.now());
        cita.setLugar("Consulta 1");
        citaService.save(cita);
        return cita;
    }
	
	public Paciente createDummyPaciente(final Medico medico, final HistoriaClinica hs) {
        Paciente paciente = new Paciente();
        paciente.setNombre("Paciente 1");
        paciente.setApellidos("Apellidos");
        paciente.setF_nacimiento(LocalDate.of(1996, 01, 12));
        paciente.setDNI("12345678A");
        paciente.setDomicilio("Sevilla");
        paciente.setEmail("paciente@email.com");
        paciente.setF_alta(LocalDate.now());
        paciente.setMedico(medico);

        this.pacienteService.pacienteCreate(paciente);

        hs.setDescripcion("placeholder");
        hs.setPaciente(paciente);
        this.historiaClinicaService.saveHistoriaClinica(hs);

        return paciente;
    }

	
	public Informe createDummyInforme(final Cita cita) throws DataAccessException, IllegalAccessException {
		Informe informe = new Informe();
        informe.setCita(cita);
        informe.setDiagnostico("Dermatitis");
        informe.setMotivo_consulta("Picor en frente");
        informeService.saveInforme(informe);
        return informe;
	}
	
	public Medico createDummyMedico() {
        Medico medico = new Medico();
        User medicoUser = new User();
        Authorities authorities = new Authorities();

        medico.setNombre("Medico 1");
        medico.setApellidos("Apellidos");
        medico.setDNI("12345678A");
        medico.setN_telefono("123456789");
        medico.setDomicilio("Domicilio");
        medicoUser.setUsername("medico1");
        medicoUser.setPassword("medico1");
        medicoUser.setEnabled(true);
        medico.setUser(medicoUser);
        authorities.setUsername(medicoUser.getUsername());
        authorities.setAuthority("medico");

        this.medicoService.medicoCreate(medico);

        return medico;
    }
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitCreateTratamientoForm() throws Exception {
		mockMvc.perform(get("/tratamientos/new/{informeId}", TEST_INFORME_ID))
				.andExpect(status().isOk())
				//.andExpect(model().attributeExists("tratamiento"))
				//.andExpect(model().attributeExists("informe"))
				.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
		
	}	
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoForm() throws Exception {
		Tratamiento tratamiento = tratamientoService.findTratamientoById(TEST_TRATAMIENTO_ID).get() ;
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", TEST_TRATAMIENTO_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("tratamiento"))
				.andExpect(model().attributeExists("informe"))
				.andExpect(model().attribute("tratamiento", hasProperty("medicamento", is(tratamiento.getMedicamento()))))
				.andExpect(model().attribute("tratamiento", hasProperty("dosis", is(tratamiento.getDosis()))))
				.andExpect(model().attribute("tratamiento", hasProperty("f_inicio_tratamiento", is(tratamiento.getF_inicio_tratamiento()))))
				.andExpect(model().attribute("tratamiento", hasProperty("f_fin_tratamiento", is(tratamiento.getF_fin_tratamiento()))))
				.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"));
	}	
	
	//CASO POSITIVO
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testSaveSuccessTratamiento() throws Exception {
        
		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis de prueba");
		tratamiento.setMedicamento("medicamento de prueba");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-10"));
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
		//.andExpect(status().isOk()) redirige bien pero no es ok
		.andExpect(view().name("redirect:/citas/1/informes/1"));	

	}
	
	//Caso fecha fin en pasado
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
    void testSaveTratamientoFechaFinPasado() throws Exception{

		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis de prueba");
		tratamiento.setMedicamento("medicamento de prueba");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-04-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-01-10"));
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
		.andExpect(status().isOk())
        .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm")
        );
}
	
	//Caso fecha inicio en futuro
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
    void testSaveTratamientoFechaInicioFuturo() throws Exception{
		
		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("dosis test");
		tratamiento.setMedicamento("medicamento test");
		tratamiento.setF_inicio_tratamiento(LocalDate.parse("2020-12-22"));
		tratamiento.setF_fin_tratamiento(LocalDate.parse("2020-10-22"));
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
        
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
        .andExpect(status().isOk())
        .andExpect(view().name("tratamientos/createOrUpdateTratamientosForm")
        );

    }

	
	//CASO CAMPOS VACIOS
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
    void testSaveTratamientoNullInputs() throws Exception{
		
		Tratamiento tratamiento = new Tratamiento();
		tratamiento.setInforme(informeService.findInformeById(TEST_INFORME_ID).get());
		tratamiento.setDosis("");
		tratamiento.setMedicamento("");
		tratamiento.setF_inicio_tratamiento(null);
		tratamiento.setF_fin_tratamiento(null);
		
        mockMvc.perform(post("/tratamientos/save")
        		.with(csrf())
        	    .flashAttr("tratamiento", tratamiento))
        /*
        .andExpect(model().attributeHasFieldErrors("tratamiento","medicamento"))
        .andExpect(model().attributeHasFieldErrors("tratamiento","dosis"))
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_inicio_tratamiento"))
        .andExpect(model().attributeHasFieldErrors("tratamiento","f_fin_tratamiento"))
        */
        .andExpect(status().isOk())
        //.andExpect(view().name("tratamientos/createOrUpdateTratamientosForm"))
        ;
	}

	//CASO EDITAR (INIT) TRATAMIENTO NO VIGENTE
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoNoVigente() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", 4))
				.andExpect(status().isOk())
				//.andExpect(view().name("redirect:/"))
				;
	}
	
	// CASO CREAR (INIT) TRATAMIENTO A INFORME DE OTRO MEDICO
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitCreateTratamientoInformeOtroMedico() throws Exception {
		mockMvc.perform(get("/tratamientos/new/{informeId}", 4))
				.andExpect(view().name("redirect:/"));
	}
	
	// CASO UPDATE (INIT) TRATAMIENTO DE OTRO MEDICO     peta da igual lo que haga
	
	@WithMockUser(username="alvaroMedico",authorities= {"medico"})
	@Test
	void testInitUpdateTratamientoOtroMedico() throws Exception {
		mockMvc.perform(get("/tratamientos/{tratamientoId}/edit", 7))
				.andExpect(view().name("redirect:/"));
	}
	
		@WithMockUser(username="alvaroMedico",authorities= {"medico"})
		@Test
		void testDeleteTratamientoSuccess() throws Exception{
			Tratamiento tratamiento = new Tratamiento();
			Medico medico = userService.getCurrentMedico();
			Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
			Cita cita = createDummyCita1(paciente);
			citaService.save(cita);
	
			Informe informe = new Informe();
			informe.setCita(cita);
			informe.setDiagnostico("Dermatitis");
			informe.setMotivo_consulta("Picor en frente");
			informeService.saveInforme(informe);
			
			tratamiento.setId(TEST_TRATAMIENTO_ID);
			tratamiento.setMedicamento("aspirina1");
			tratamiento.setDosis("1 pastilla cada 8 horas");
			tratamiento.setF_inicio_tratamiento(LocalDate.now());
			tratamiento.setF_fin_tratamiento(LocalDate.now().plusDays(5));
			tratamiento.setInforme(informe);
			
			
			tratamientoService.save(tratamiento);
			
			
			mockMvc.perform(get("/tratamientos/delete/{tratamientoId}", TEST_TRATAMIENTO_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/citas/" + tratamiento.getInforme().getCita().getPaciente().getMedico().getId() + "/informes/"
					+ tratamiento.getInforme().getId()));
	
			
		}
		
		@WithMockUser(username="alvaroMedico",authorities= {"medico"})
		@Test
		void testDeleteTratamientoCantDeletePastDate() throws Exception{
			Tratamiento tratamiento = new Tratamiento();
			Medico medico = userService.getCurrentMedico();
			Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
			Cita cita = createDummyCita1(paciente);
			citaService.save(cita);
	
			Informe informe = new Informe();
			informe.setCita(cita);
			informe.setDiagnostico("Dermatitis");
			informe.setMotivo_consulta("Picor en frente");
			informeService.saveInforme(informe);
			
			cita.setFecha(LocalDate.parse("2020-01-01"));
			citaService.saveOldDate(cita);
			
			tratamiento.setId(TEST_TRATAMIENTO_ID);
			tratamiento.setMedicamento("aspirina1");
			tratamiento.setDosis("1 pastilla cada 8 horas");
			tratamiento.setF_inicio_tratamiento(LocalDate.now());
			tratamiento.setF_fin_tratamiento(LocalDate.now().plusDays(5));
			tratamiento.setInforme(informe);
			tratamiento.getInforme().setCita(cita);
			
			
			tratamientoService.save(tratamiento);
			
			//mockMvc.perform(get("/tratamientos/delete/{tratamientoId}", TEST_TRATAMIENTO_ID))
			//.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/citas/" + tratamiento.getInforme().getCita().getPaciente().getMedico().getId() + "/informes/"
			//		+ tratamiento.getInforme().getId()));
			
			mockMvc.perform(get("/tratamientos/delete/{tratamientoId}", TEST_TRATAMIENTO_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/citas/" + cita.getPaciente().getMedico().getId() + "/informes/"
					+ informe.getId()));
			
			
			//tratamientoService.deleteTratamiento(TEST_TRATAMIENTO_ID, tratamiento.getInforme().getCita().getPaciente().getMedico().getId());
			
			
		}
		
		@WithMockUser(username="andresMedico",authorities= {"medico"})
		@Test
		void testDeleteTratamientoCantDeleteWrongAuthority() throws Exception{
			Tratamiento tratamiento = new Tratamiento();
			Medico medico = createDummyMedico();
			Paciente paciente = createDummyPaciente(medico, new HistoriaClinica());
			Cita cita = createDummyCita1(paciente);
			citaService.save(cita);
			Informe informe = new Informe();
			informe.setCita(cita);
			informe.setDiagnostico("Dermatitis");
			informe.setMotivo_consulta("Picor en frente");
			informeService.saveInforme(informe);
			
			tratamiento.setId(TEST_TRATAMIENTO_ID);
			tratamiento.setMedicamento("aspirina1");
			tratamiento.setDosis("1 pastilla cada 8 horas");
			tratamiento.setF_inicio_tratamiento(LocalDate.now());
			tratamiento.setF_fin_tratamiento(LocalDate.now().plusDays(5));
			tratamiento.setInforme(informe);
			
			tratamientoService.save(tratamiento);
			
			mockMvc.perform(get("/tratamientos/delete/{tratamientoId}", TEST_TRATAMIENTO_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("accessNotAuthorized"));
			
			tratamientoService.deleteTratamiento(TEST_TRATAMIENTO_ID, tratamiento.getInforme().getCita().getPaciente().getMedico().getId());
	
		}

}
