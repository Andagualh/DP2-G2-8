
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Informe extends NamedEntity {

	private String	motivo_consulta;

	private String	diagnostico;

	@OneToOne
	@JoinColumn(name = "cita_id")
	@NotNull
	private Cita	cita;

	@ManyToOne
	@JoinColumn(name = "historia_clinica_id")
	private HistoriaClinica	historiaClinica;
}
