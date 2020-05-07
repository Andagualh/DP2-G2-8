
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "historiaclinica")
public class HistoriaClinica extends BaseEntity {

	private String descripcion;


	public HistoriaClinica() {
		super();
		this.descripcion = "";
	}

	public HistoriaClinica(final String descripcion) {
		super();
		this.descripcion = descripcion;
	}


	@NotNull
	@Valid
	@OneToOne(optional = false)
	@JoinColumn(name = "paciente_id")
	private Paciente paciente;

	//	@OneToMany(mappedBy = "informe")
	//	private Collection<Informe>	informes;
}
