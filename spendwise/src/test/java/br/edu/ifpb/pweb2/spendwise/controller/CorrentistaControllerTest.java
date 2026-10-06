package br.edu.ifpb.pweb2.spendwise.controller;

import br.edu.ifpb.pweb2.spendwise.model.Categoria;
import br.edu.ifpb.pweb2.spendwise.model.Comentario;
import br.edu.ifpb.pweb2.spendwise.model.Conta;
import br.edu.ifpb.pweb2.spendwise.model.Correntista;
import br.edu.ifpb.pweb2.spendwise.model.Transacao;
import br.edu.ifpb.pweb2.spendwise.service.CategoriaService;
import br.edu.ifpb.pweb2.spendwise.service.ComentarioService;
import br.edu.ifpb.pweb2.spendwise.service.ContaService;
import br.edu.ifpb.pweb2.spendwise.service.CorrentistaService;
import br.edu.ifpb.pweb2.spendwise.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CorrentistaController.class)
class CorrentistaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CorrentistaService correntistaService;

    @MockBean
    private ContaService contaService;

    @MockBean
    private TransacaoService transacaoService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private ComentarioService comentarioService;

    @Test
    void deveCarregarComentarioExistenteNaEdicaoDaTransacao() throws Exception {
        var correntista = new Correntista();
        correntista.setId(1L);

        var conta = new Conta();
        conta.setId(2L);
        conta.setDescricao("Principal");
        conta.setCorrentista(correntista);

        var transacao = new Transacao();
        transacao.setId(3L);
        transacao.setDescricao("Compra no mercado");
        transacao.setConta(conta);

        var categoria = new Categoria();
        categoria.setId(10L);
        categoria.setNome("Alimentação");

        transacao.setCategoria(categoria);

        when(correntistaService.buscarPorId(1L)).thenReturn(correntista);
        when(contaService.buscarPorId(2L)).thenReturn(conta);
        when(transacaoService.buscarPorId(3L)).thenReturn(transacao);
        when(categoriaService.listarAtivas()).thenReturn(List.of(categoria));
        when(comentarioService.buscarPorTransacao(3L)).thenReturn(Optional.of(Comentario.builder().texto("Comentário da transação").build()));

        mockMvc.perform(get("/correntista/1/conta/2/transacao/3/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("correntista/editarTransacao"))
                .andExpect(model().attribute("comentarioTexto", "Comentário da transação"));
    }
}
