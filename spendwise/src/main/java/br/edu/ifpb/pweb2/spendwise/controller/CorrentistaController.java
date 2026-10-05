package br.edu.ifpb.pweb2.spendwise.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifpb.pweb2.spendwise.model.Conta;
import br.edu.ifpb.pweb2.spendwise.model.Transacao;
import br.edu.ifpb.pweb2.spendwise.service.CategoriaService;
import br.edu.ifpb.pweb2.spendwise.service.ContaService;
import br.edu.ifpb.pweb2.spendwise.service.CorrentistaService;
import br.edu.ifpb.pweb2.spendwise.service.TransacaoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/correntista")
public class CorrentistaController {

    private final CorrentistaService correntistaService;
    private final ContaService contaService;
    private final TransacaoService transacaoService;
    private final CategoriaService categoriaService;

    public CorrentistaController(CorrentistaService correntistaService, ContaService contaService, TransacaoService transacaoService, CategoriaService categoriaService) {
        this.correntistaService = correntistaService;
        this.contaService = contaService;
        this.transacaoService = transacaoService;
        this.categoriaService = categoriaService;

    }

    @GetMapping("/{id}/cadastrar")
    public String cadastrarConta(@PathVariable Long id, Model model) {
        model.addAttribute("contaForm", new Conta());
        model.addAttribute("id", id);

        return "correntista/cadastroConta";
    }

    @PostMapping("/{id}/cadastrar")
    public String cadastrarConta(@PathVariable Long id, Conta conta, Model model) {
        try {
            var correntista = correntistaService.buscarPorId(id);

            conta.setId(null);
            conta.setCorrentista(correntista);

            contaService.salvar(conta);

            return "redirect:/correntista/" + id + "/contas";

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("contaForm", conta);
            model.addAttribute("id", id);

            return "correntista/cadastroConta";
        }
    }

    @GetMapping("/{id}")
    public String inicio(@PathVariable Long id) {
        try{
            List<Conta> contas = contaService.listarPorCorrentista(id);
            correntistaService.buscarPorId(id);

            if (contas.isEmpty()) {
                return "redirect:/correntista/" + id + "/cadastrar";
            }
            
            return "redirect:/correntista/" + id + "/contas";
        } catch (IllegalArgumentException e) {
            return "correntista/correntistaNaoEncontrado";
        }
    }

    @GetMapping("/{id}/contas")
    public String listarContas(@PathVariable Long id, Model model) {
        List<Conta> contas = contaService.listarPorCorrentista(id);

        model.addAttribute("contas", contas);
        model.addAttribute("id", id);

        return "correntista/contas";
    }

    @GetMapping("/{correntistaId}/conta/{contaId}")
    public String listarTransacoes(@PathVariable Long correntistaId, @PathVariable Long contaId, Model model) {

        try {
            correntistaService.buscarPorId(correntistaId);

            var conta = contaService.buscarPorId(contaId);

            List<Transacao> transacoes = transacaoService.listarPorConta(contaId);

            model.addAttribute("conta", conta);
            model.addAttribute("transacoes", transacoes);
            model.addAttribute("correntistaId", correntistaId);
            model.addAttribute("categorias", categoriaService.listarAtivas());
            
            return "correntista/transacoes";

        } catch (IllegalArgumentException e) {
            return "correntista/correntistaNaoEncontrado";
        }
    }

    @GetMapping("/{correntistaId}/conta/{contaId}/transacao/cadastrar")
    public String cadastrarTransacao(@PathVariable Long correntistaId, @PathVariable Long contaId, Model model) {

        try {
            correntistaService.buscarPorId(correntistaId);

            var conta = contaService.buscarPorId(contaId);

            model.addAttribute("transacaoForm", new Transacao());
            model.addAttribute("conta", conta);
            model.addAttribute("correntistaId", correntistaId);
            model.addAttribute("categorias", categoriaService.listarAtivas());

            return "correntista/cadastroTransacao";

        } catch (IllegalArgumentException e) {
            return "correntista/correntistaNaoEncontrado";
        }
    }

    @PostMapping("/{correntistaId}/conta/{contaId}/transacao/cadastrar")
    public String salvarTransacao(@PathVariable Long correntistaId, @PathVariable Long contaId, @Valid @ModelAttribute("transacaoForm") Transacao transacao, BindingResult bindingResult, @RequestParam(required = false) Long categoriaId, Model model) {
        if (categoriaId == null) {
            bindingResult.rejectValue("categoria", "NotNull", "Categoria obrigatória!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("conta", contaService.buscarPorId(contaId));
            model.addAttribute("correntistaId", correntistaId);
            model.addAttribute("categorias", categoriaService.listarAtivas());
            return "correntista/cadastroTransacao";
        }
        
        try {
            correntistaService.buscarPorId(correntistaId);

            var conta = contaService.buscarPorId(contaId);

            var categoria = categoriaService.buscarPorId(categoriaId);

            transacao.setId(null);
            transacao.setConta(conta);
            transacao.setCategoria(categoria);

            transacaoService.salvar(transacao);

            return "redirect:/correntista/"
                    + correntistaId
                    + "/conta/"
                    + contaId;

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("transacaoForm", transacao);
            model.addAttribute("conta", contaService.buscarPorId(contaId));
            model.addAttribute("correntistaId", correntistaId);
            model.addAttribute("categorias", categoriaService.listarAtivas());

            return "correntista/cadastroTransacao";
        }
    }

    @GetMapping("/{correntistaId}/conta/{contaId}/transacao/{transacaoId}/editar")
    public String editarTransacao(@PathVariable Long correntistaId, @PathVariable Long contaId, @PathVariable Long transacaoId, Model model) {
        try {
            correntistaService.buscarPorId(correntistaId);

            var conta = contaService.buscarPorId(contaId);

            var transacao = transacaoService.buscarPorId(transacaoId);

            model.addAttribute("transacaoForm", transacao);
            model.addAttribute("conta", conta);
            model.addAttribute("correntistaId", correntistaId);
            model.addAttribute("categorias", categoriaService.listarAtivas());

            return "correntista/editarTransacao";

        } catch (IllegalArgumentException e) {
            return "correntista/correntistaNaoEncontrado";
        }
    }

@PostMapping("/{correntistaId}/conta/{contaId}/transacao/{transacaoId}/editar")
public String salvarEdicaoTransacao(
        @PathVariable Long correntistaId,
        @PathVariable Long contaId,
        @PathVariable Long transacaoId,
        @Valid @ModelAttribute("transacaoForm") Transacao transacao,
        BindingResult bindingResult,
        @RequestParam(required = false) Long categoriaId,
        Model model) {

    if (categoriaId == null) {
        bindingResult.rejectValue(
            "categoria",
            "NotNull",
            "Categoria obrigatória!"
        );
    }

    if (bindingResult.hasErrors()) {
        transacao.setId(transacaoId);

        model.addAttribute("conta", contaService.buscarPorId(contaId));
        model.addAttribute("correntistaId", correntistaId);
        model.addAttribute("categorias", categoriaService.listarAtivas());

        return "correntista/editarTransacao";
    }

    try {
        correntistaService.buscarPorId(correntistaId);

        var conta = contaService.buscarPorId(contaId);
        var categoria = categoriaService.buscarPorId(categoriaId);

        transacao.setId(transacaoId);
        transacao.setConta(conta);
        transacao.setCategoria(categoria);

        transacaoService.salvar(transacao);

        return "redirect:/correntista/"
                + correntistaId
                + "/conta/"
                + contaId;

    } catch (IllegalArgumentException e) {

        model.addAttribute("erro", e.getMessage());
        model.addAttribute("transacaoForm", transacao);
        model.addAttribute("conta", contaService.buscarPorId(contaId));
        model.addAttribute("correntistaId", correntistaId);
        model.addAttribute("categorias", categoriaService.listarAtivas());

        return "correntista/editarTransacao";
    }
}
}