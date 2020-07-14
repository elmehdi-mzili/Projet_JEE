package org.sid.cinema.dao;

import org.sid.cinema.entities.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface ProjectionRepository extends JpaRepository<Projection, Long >{
	
	
	@Query(value = "SELECT p FROM Projection p , Film f WHERE p.film.id = f.id AND f.name LIKE %:kw%")
	public Page<Projection> findByFilm(@Param("kw") String kw ,  Pageable pageRequest);

}
