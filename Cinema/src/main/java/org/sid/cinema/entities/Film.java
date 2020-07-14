package org.sid.cinema.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Film {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name, realisateur,description,photo;
	private double duree;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateSortie;
	@OneToMany(mappedBy ="film")
	@JsonProperty(access=Access.WRITE_ONLY)
	private Collection<Projection> projection;
	@ManyToOne
	private Categorie categorie;
}
