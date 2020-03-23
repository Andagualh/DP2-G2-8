
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "historiaclinica")
public class HistoriaClinica extends NamedEntity {

	private String		descripcion;

	@OneToOne
	@JoinColumn(name = "paciente_id")
	@NotNull
	private Paciente	paciente;

}
