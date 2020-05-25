
package org.springframework.samples.petclinic.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// @Data

@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Informe extends BaseEntity {

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
	
	@OneToMany(mappedBy = "informe")
	private Collection<@Valid Tratamiento>	tratamientos;
}
