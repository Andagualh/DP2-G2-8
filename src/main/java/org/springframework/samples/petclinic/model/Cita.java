package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
public class Cita extends NamedEntity {
	
	@NotBlank
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate fecha;
	
	@NotBlank
	private String lugar;
	
	@ManyToOne(optional = false)
	private Paciente paciente;
	
}
