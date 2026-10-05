package br.edu.ifpb.pweb2.spendwise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.spendwise.model.Correntista;
import br.edu.ifpb.pweb2.spendwise.service.CorrentistaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CorrentistaService correntistaService;

    public AdminController(CorrentistaService correntistaService) {
        this.correntistaService = correntistaService;
    }

    @GetMapping
    public String inicio() {
        return "redirect:/admin/correntistas";
    }

    @GetMapping("/cadastrar")
    public String cadastrarCorrentista(Model model) {
        model.addAttribute("correntistaForm", new Correntista());

        return "admin/cadastroCorrentista";
    }

    @PostMapping("/cadastrar")
    public String salvarCorrentista(@Valid @ModelAttribute("correntistaForm") Correntista correntista, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/cadastroCorrentista";
        }
        try {
            correntistaService.salvar(correntista);

            return "redirect:/admin/cadastrar";

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());

            model.addAttribute("correntistaForm", correntista);

            return "admin/cadastroCorrentista";
        }
    }

    @GetMapping("/correntistas")
    public String listarCorrentistas(Model model) {
        model.addAttribute("correntistas", correntistaService.listar());

        return "admin/listaCorrentista";
    }
}