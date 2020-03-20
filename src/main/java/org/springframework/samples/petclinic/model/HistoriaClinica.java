package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Entity
@Data
public class HistoriaClinica extends NamedEntity {
	
	private String	descripcion;

	@OneToOne
	@JoinColumn(name = "paciente_id")
	@NotNull
	private Paciente		paciente;

}