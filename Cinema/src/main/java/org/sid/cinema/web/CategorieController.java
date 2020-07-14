package org.sid.cinema.web;


import javax.validation.Valid;

import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.entities.Categorie;
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
public class CategorieController {

	@Autowired
	private CategorieRepository categorieRepository;

	@GetMapping(path = "/categories")
	public String listCategories(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "size", defaultValue = "5") int s,
			@RequestParam(name = "keyword", defaultValue = "") String kw) {
		Page<Categorie> pageCategories = categorieRepository.findByNameContains(kw, PageRequest.of(p, s));
		model.addAttribute("listCategories", pageCategories.getContent());
		model.addAttribute("pages", new int[pageCategories.getTotalPages()]);
		model.addAttribute("currentPage", p);
		model.addAttribute("keyword", kw);
		return "categoriesView";
	}

	@GetMapping(path = "/deleteCategorie")
	public String delete(Long id, String keyword, int page, int size) {
		categorieRepository.deleteById(id);
		return "redirect:/categories?page=" + page + "&size=" + size + "&keyword=" + keyword;
	}

	@GetMapping(path = "/index")
	public String index() {
		return "index";
	}

	@GetMapping(path = "/formCategorie")
	public String formCategories(Model model) {
		model.addAttribute("categorie", new Categorie());
		return "formCategorie";
	}

	@PostMapping(path = "/saveCategories")
	public String saveCategorie(@Valid Categorie categorie, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors())
			return "formCategorie";
		categorieRepository.save(categorie);
		redirectAttributes.addFlashAttribute("message", "success!");
		return "redirect:/formCategorie";
	}

	@GetMapping(path = "/editCategorie")
	public String editCategorie(Model model, Long id) {

		Categorie c = categorieRepository.findById(id).get();
		model.addAttribute("categorie", c);
		model.addAttribute("mode", "edit");
		return "formCategorie";
	}

}
