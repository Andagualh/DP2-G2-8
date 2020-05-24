
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Tratamiento extends NamedEntity {

	@NotBlank
	private String		medicamento;

	@NotBlank
	private String		dosis;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate	f_inicio_tratamiento;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate	f_fin_tratamiento;

	@ManyToOne
	@JoinColumn(name = "informe_id")
	@NotNull
	private Informe		informe;
}
