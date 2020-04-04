
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Tratamiento extends NamedEntity {

	private String		medicamento;

	private String		dosis;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate	f_inicio_tratamiento;

	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate	f_fin_tratamiento;

	@ManyToOne
	@JoinColumn(name = "informe_id")
	@NotNull
	private Informe		informe;
}
