package org.sid.cinema.web;

import java.util.List;

import javax.validation.Valid;

import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Cinema;
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
public class CinemaController {

	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private VilleRepository villeRepository;

	@GetMapping(path = "/cinemas")
	public String listCinemas(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "size", defaultValue = "5") int s,
			@RequestParam(name = "keyword", defaultValue = "") String kw) {
		Page<Cinema> pageCinemas = cinemaRepository.findByNameContains(kw, PageRequest.of(p, s));
		model.addAttribute("listCinemas", pageCinemas.getContent());
		model.addAttribute("pages", new int[pageCinemas.getTotalPages()]);
		model.addAttribute("currentPage", p);
		model.addAttribute("size", s);
		model.addAttribute("keyword", kw);
		return "cinemasView";
	}

	@GetMapping(path = "/deleteCinema")
	public String delete(Long id, String keyword, int page, int size) {
		cinemaRepository.deleteById(id);
		return "redirect:/cinemas?page=" + page + "&size=" + size + "&keyword=" + keyword;
	}

	@GetMapping(path = "/formCinema")
	public String formCinema(Model model) {
		List<Ville> pageVilles = villeRepository.findAll();
		model.addAttribute("villes", pageVilles);
		model.addAttribute("cinema", new Cinema());
		model.addAttribute("mode", "new");
		return "formCinema";
	}

	@PostMapping(path = "/saveCinemas")
	public String saveCinemas(Model model, @Valid Cinema cinema, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors())
			return "formCinema";
		List<Ville> pageVilles = villeRepository.findAll();
		model.addAttribute("villes", pageVilles);
		cinemaRepository.save(cinema);
		redirectAttributes.addFlashAttribute("message", "success!");
		return "redirect:/formCinema";
	}

	@GetMapping(path = "/editCinema")
	public String editCinema(Model model, Long id) {
		List<Ville> pageVilles = villeRepository.findAll();
		model.addAttribute("villes", pageVilles);
		Cinema c = cinemaRepository.findById(id).get();
		model.addAttribute("cinema", c);
		model.addAttribute("mode", "edit");
		return "formCinema";
	}
}
