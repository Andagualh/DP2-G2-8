package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "paciente")
public class Paciente extends NamedEntity {

	@NotBlank
	private String				nombre;

	private String				apellidos;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate			f_nacimiento;

	@Length(min = 9, max = 9)
	private String				DNI;

	private Integer				n_telefono;

	private String				domicilio;

	@Email
	private String				email;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate			f_alta;

	@ManyToOne
	@JoinColumn(name = "medico_id")
	@NotNull
	private Medico				medico;

	@OneToMany(mappedBy = "paciente")
	private Collection<Cita>	citas;

}