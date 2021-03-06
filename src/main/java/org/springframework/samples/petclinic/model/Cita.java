
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Cita extends NamedEntity {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate	fecha;

	@NotEmpty
	private String		lugar;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	//@JoinColumn(name = "paciente_id")
	private Paciente	paciente;

	@Valid
	@OneToOne(fetch=FetchType.LAZY, mappedBy = "cita")
	private Informe		informe;
}
