
package org.springframework.samples.petclinic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
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


	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Paciente			paciente;

	@OneToMany(mappedBy = "informe")
	private Collection<Informe>	informes;
}
