
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "paciente")
public class Paciente extends BaseEntity {

	@NotBlank
	private String		nombre;

	@NotBlank
	private String		apellidos;

	@PastOrPresent
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate	f_nacimiento;

	@Length(min = 9, max = 9)
	@NotBlank
	private String		DNI;

	private Integer		n_telefono;

	private String		domicilio;

	@Email
	private String		email;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate	f_alta;

	@ManyToOne
	@JoinColumn(name = "medico_id")
	@NotNull
	@Valid
	private Medico		medico;

	//	@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
	//	@Valid
	//	private Collection<@Valid Cita>	citas;
}
