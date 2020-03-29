
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.HistoriaClinica;
import org.springframework.samples.petclinic.model.Paciente;
import org.springframework.samples.petclinic.repository.PacienteRepository;
import org.springframework.samples.petclinic.util.DniValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepository		pacienteRepo;
	@Autowired
	private CitaService				citaService;
	@Autowired
	private MedicoService			medicoService;
	@Autowired
	private HistoriaClinicaService	historiaClinicaService;


	@Autowired
	public PacienteService(final PacienteRepository pacienteRepo) {
		this.pacienteRepo = pacienteRepo;
	}

	@Transactional
	public Optional<Paciente> existsPacienteById(final int id) {
		return this.pacienteRepo.findById(id);
	}

	@Transactional
	public Collection<Paciente> getPacientes() {
		Collection<Paciente> pacientes = new ArrayList<Paciente>();
		this.pacienteRepo.findAll().forEach(pacientes::add);
		return pacientes;
	}

	@Transactional(readOnly = true)
	public Paciente getPacienteById(final int id) throws DataAccessException {
		return this.pacienteRepo.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Optional<Paciente> findPacienteById(final int id) throws DataAccessException {
		return this.pacienteRepo.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Paciente> findPacienteByApellidos(final String apellidos) throws DataAccessException {
		return this.pacienteRepo.findPacienteByApellidos(apellidos);
	}

	@Transactional
	public int pacienteCreate(final Paciente paciente) {
		return this.pacienteRepo.save(paciente).getId();
	}

	@Transactional
	public void savePaciente(final Paciente paciente) {
		this.pacienteRepo.save(paciente).getId();
	}

	@Transactional
	public void savePacienteByMedico(final Paciente paciente, final int idMedico) {
		boolean tieneTelefono = false;

		if (paciente.getN_telefono() != null) {
			tieneTelefono = true;
		}

		boolean tieneContacto = tieneTelefono || !paciente.getDomicilio().isEmpty() || !paciente.getEmail().isEmpty();
		boolean dniOk = new DniValidator(paciente.getDNI()).validar();

		if (paciente.getMedico().getId() == idMedico) {
			if (dniOk) {
				if (tieneContacto) {
					if (tieneTelefono) {
						if (!paciente.getN_telefono().toString().isEmpty() && !(paciente.getN_telefono().toString().length() == 9)) {
							throw new IllegalArgumentException("Número de teléfono incorrecto");
						} else {
							this.pacienteRepo.save(paciente).getId();
						}
					} else {
						this.pacienteRepo.save(paciente).getId();
					}
				} else {
					throw new IllegalArgumentException("No tiene forma de contacto");
				}
			} else {
				throw new IllegalArgumentException("Dni incorrecto");

			}
		} else {
			throw new IllegalAccessError();
		}
	}

	@Transactional
	public void pacienteDelete(final int idPaciente) {
		this.pacienteRepo.deleteById(idPaciente);
	}

	@Transactional
	public void deletePacienteByMedico(final int idPaciente, final int idMedico) {
		Paciente paciente = this.pacienteRepo.findById(idPaciente).get();
		boolean medicoEnabled = this.medicoService.getMedicoById(idMedico).getUser().isEnabled();

		if (paciente.getMedico().getId() == idMedico && medicoEnabled) {
			Collection<Cita> citas = this.citaService.findAllByPaciente(paciente);
			boolean puedeBorrarse = citas.isEmpty();
			if (!citas.isEmpty()) {
				LocalDate ultimaCita = citas.stream().map(Cita::getFecha).max(LocalDate::compareTo).get();
				LocalDate hoy = LocalDate.now();
				puedeBorrarse = hoy.compareTo(ultimaCita) >= 6 || hoy.compareTo(ultimaCita) == 5 && hoy.getDayOfYear() >= hoy.getDayOfYear();
			}

			HistoriaClinica hs = this.findHistoriaClinicaByPaciente(paciente);

			puedeBorrarse = puedeBorrarse && hs.getDescripcion().isEmpty();

			if (citas.isEmpty() && hs.getDescripcion().isEmpty()) {
				this.historiaClinicaService.deleteHistoriaClinica(hs);
				this.citaService.deleteAllByPaciente(paciente);
				this.pacienteRepo.deleteById(idPaciente);
			} else if (puedeBorrarse) {
				this.historiaClinicaService.deleteHistoriaClinica(hs);
				this.citaService.deleteAllByPaciente(paciente);
				this.pacienteRepo.deleteById(idPaciente);
			} else {
				throw new IllegalStateException();
			}
		} else {
			throw new IllegalAccessError();
		}
	}

	@Transactional
	public int pacienteCount() {
		return (int) this.pacienteRepo.count();
	}

	@Transactional(readOnly = true)
	public Collection<Paciente> findPacienteByMedicoId(final int id) throws DataAccessException {
		return this.pacienteRepo.findPacientesByMedicoId(id);
	}

	@Transactional
	public HistoriaClinica findHistoriaClinicaByPaciente(final Paciente paciente) {
		return this.historiaClinicaService.findHistoriaClinicaByPaciente(paciente);
	}
}
