
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Informe extends NamedEntity {

	@NotEmpty
	private String			motivo_consulta;

	@NotEmpty
	private String			diagnostico;

	@Valid
	@OneToOne
	@JoinColumn(name = "cita_id")
	@NotNull
	private Cita			cita;

	@ManyToOne
	@JoinColumn(name = "historiaClinica_id")
	private HistoriaClinica	historiaClinica;
}
