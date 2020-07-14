package org.sid.cinema.web;

import java.util.List;

import javax.validation.Valid;

import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.SeanceRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Seance;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProjectionController {

	
	@Autowired 
	private ProjectionRepository projectionRepository;
	@Autowired 
	private FilmRepository filmRepository;
	@Autowired 
	private SalleRepository salleRepository;
	@Autowired 
	private SeanceRepository seanceRepository;
	
	
	@GetMapping(path="/projections")
	public String listProjections(Model model ,
			@RequestParam(name="page" , defaultValue="0") int p ,
	        @RequestParam(name="size" , defaultValue="5") int s,
	        @RequestParam(name="keyword" , defaultValue="") String kw) 
	{
		Page<Projection> pageProjections = projectionRepository.findByFilm(kw, PageRequest.of(p, s));
		model.addAttribute("listProjections" , pageProjections.getContent());
		model.addAttribute("pages", new int[pageProjections.getTotalPages()]);
		model.addAttribute("currentPage", p);
		model.addAttribute("keyword", kw);
		return "projectionsView" ;
	}
	
	@GetMapping(path="/deleteProjection")
	public String delete(Long id , String keyword, int page , int size)
	{
		projectionRepository.deleteById(id);
		return "redirect:/projections?page="+page+"&size="+size+"&keyword="+keyword;
	}
	
	@GetMapping(path="/formProjection")
	public String formProjection(Model model)
	{
		List<Film> pageFilms = filmRepository.findAll();
		model.addAttribute("films" , pageFilms);
		List<Salle> pageSalles = salleRepository.findAll();
		model.addAttribute("salles" , pageSalles);
		List<Seance> pageSeances = seanceRepository.findAll();
		model.addAttribute("seances" , pageSeances);
		model.addAttribute("projection", new Projection() );
		model.addAttribute("mode", "new");
		return "formProjection" ;
	}
	
	@PostMapping(path="/saveProjections")
	public String saveProjections(Model model, @Valid Projection projection , BindingResult bindingResult , RedirectAttributes redirectAttributes)
	{
		if(bindingResult.hasErrors()) return "formProjection" ;
		List<Film> pageFilms = filmRepository.findAll();
		model.addAttribute("films" , pageFilms);
		List<Salle> pageSalles = salleRepository.findAll();
		model.addAttribute("salles" , pageSalles);
		List<Seance> pageSeances = seanceRepository.findAll();
		model.addAttribute("seances" , pageSeances);
		projectionRepository.save(projection);
		redirectAttributes.addFlashAttribute("message", "success!");
		return "redirect:/formCinema";
	}
	
	@GetMapping(path = "/editProjection")
	public String editProjection(Model model, Long id) {
		
		List<Film> pageFilms = filmRepository.findAll();
		model.addAttribute("films" , pageFilms);
		List<Salle> pageSalles = salleRepository.findAll();
		model.addAttribute("salles" , pageSalles);
		List<Seance> pageSeances = seanceRepository.findAll();
		model.addAttribute("seances" , pageSeances);
		Projection p = projectionRepository.findById(id).get();
		model.addAttribute("projection", p);
		model.addAttribute("mode", "edit");
		return "formProjection";
	}
	
}
