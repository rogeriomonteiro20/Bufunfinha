package br.edu.ifpb.pweb2.spendwise.controller;

import br.edu.ifpb.pweb2.spendwise.model.Comentario;
import br.edu.ifpb.pweb2.spendwise.model.Conta;
import br.edu.ifpb.pweb2.spendwise.model.Correntista;
import br.edu.ifpb.pweb2.spendwise.service.CategoriaService;
import br.edu.ifpb.pweb2.spendwise.service.ContaService;
import br.edu.ifpb.pweb2.spendwise.service.CorrentistaService;
import br.edu.ifpb.pweb2.spendwise.service.TransacaoService;
import br.edu.ifpb.pweb2.spendwise.model.Transacao;
import br.edu.ifpb.pweb2.spendwise.service.ComentarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/correntista")
public class CorrentistaController {

    private final CorrentistaService correntistaService;
    private final ContaService contaService;
    private final TransacaoService transacaoService;
    private final CategoriaService categoriaService;
    private final ComentarioService comentarioService;

    public CorrentistaController(CorrentistaService correntistaService, ContaService contaService, TransacaoService transacaoService, CategoriaService categoriaService, ComentarioService comentarioService) {
        this.correntistaService = correntistaService;
        this.contaService = contaService;
        this.transacaoService = transacaoService;
        this.categoriaService = categoriaService;
        this.comentarioService = comentarioService;
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
    public String salvarTransacao( @PathVariable Long correntistaId, @PathVariable Long contaId, Transacao transacao, @RequestParam Long categoriaId, Model model) {
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

            Optional<Comentario> comentarioOpt = comentarioService.buscarPorTransacao(transacaoId);
            String comentarioTexto = comentarioOpt.map(Comentario::getTexto).orElse("");

            model.addAttribute("transacaoForm", transacao);
            model.addAttribute("comentarioTexto", comentarioTexto);
            model.addAttribute("conta", conta);
            model.addAttribute("correntistaId", correntistaId);
            model.addAttribute("categorias", categoriaService.listarAtivas());

            return "correntista/editarTransacao";

        } catch (IllegalArgumentException e) {
            return "correntista/correntistaNaoEncontrado";
        }
    }

    @PostMapping("/{correntistaId}/conta/{contaId}/transacao/{transacaoId}/editar")
        public String salvarEdicaoTransacao(@PathVariable Long correntistaId, 
                                    @PathVariable Long contaId, 
                                    @PathVariable Long transacaoId, 
                                    Transacao transacao, 
                                    @RequestParam Long categoriaId, 
                                    @RequestParam(value = "comentarioTexto", required = false) String comentarioTexto,
                                    Model model) {
        try {
            correntistaService.buscarPorId(correntistaId);

            var conta = contaService.buscarPorId(contaId);
            var categoria = categoriaService.buscarPorId(categoriaId);

            transacao.setId(transacaoId);
            transacao.setConta(conta);
            transacao.setCategoria(categoria);


            transacaoService.salvar(transacao);

            var comentarioOpt = comentarioService.buscarPorTransacao(transacaoId);

            if (comentarioOpt.isPresent()) {
                if (comentarioTexto == null || comentarioTexto.isBlank()) {
                    comentarioService.deletar(comentarioOpt.get().getId());
                } else {
                    // atualiza
                    comentarioService.atualizar(comentarioOpt.get().getId(), comentarioTexto);
                }
            } else if (comentarioTexto != null && !comentarioTexto.isBlank()) {
                // se não existia, cria novo
                comentarioService.salvar(transacaoId, comentarioTexto);
            }

            return "redirect:/correntista/" + correntistaId + "/conta/" + contaId;

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