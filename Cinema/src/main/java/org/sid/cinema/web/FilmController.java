package org.sid.cinema.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.validation.Valid;

import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FilmController {

	@Autowired 
	private CategorieRepository categorieRepository;
	@Autowired 
	private FilmRepository filmRepository;
	
	
	@GetMapping(path="/films")
	public String listCategories(Model model ,
			@RequestParam(name="page" , defaultValue="0") int p ,
	        @RequestParam(name="size" , defaultValue="5") int s,
	        @RequestParam(name="keyword" , defaultValue="") String kw) 
	{
	    Page<Film> pageFilms = filmRepository.findByNameContains(kw, PageRequest.of(p, s));
		model.addAttribute("listFilms" , pageFilms.getContent());
		model.addAttribute("pages", new int[pageFilms.getTotalPages()]);
		model.addAttribute("currentPage", p);
		model.addAttribute("keyword", kw);
		return "filmsView" ;
	}
	
	@GetMapping(path="/image/{imageFilm}" , produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getImage(@PathVariable(name = "imageFilm") String imageName) throws IOException {

	    File serverFile = new File(System.getProperty("user.home") + "/Cinema/images/" + imageName + ".jpg");

	    return Files.readAllBytes(serverFile.toPath());
	}
	
	@GetMapping(path="/deleteFilm")
	public String delete(Long id , String keyword, int page , int size)
	{
		filmRepository.deleteById(id);
		return "redirect:/films?page="+page+"&size="+size+"&keyword="+keyword;
	}
	
	@GetMapping(path="/formFilm")
	public String formFilm(Model model)
	{
		List<Categorie> pageCategories = categorieRepository.findAll();
		model.addAttribute("categories" , pageCategories);
		model.addAttribute("film", new Film() );
		return "formFilm" ;
	}
	
	@PostMapping(path="/saveFilms")
	public String saveFilms(Model model, @Valid Film film , BindingResult bindingResult , RedirectAttributes redirectAttributes)
	{
		if(bindingResult.hasErrors()) return "formFilm" ;
		List<Categorie> pageCategories = categorieRepository.findAll();
		model.addAttribute("categories" , pageCategories);
		filmRepository.save(film);
		redirectAttributes.addFlashAttribute("message", "success!");
		return "redirect:/formCinema";
	}
	
	@GetMapping(path = "/editFilm")
	public String editFilm(Model model, Long id) {
		List<Categorie> pageCategories = categorieRepository.findAll();
		model.addAttribute("categories", pageCategories);
		Film f = filmRepository.findById(id).get();
		model.addAttribute("film", f);
		model.addAttribute("mode", "edit");
		return "formFilm";
	}
}
