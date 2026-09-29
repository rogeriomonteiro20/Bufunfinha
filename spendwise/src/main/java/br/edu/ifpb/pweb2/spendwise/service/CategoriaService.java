package br.edu.ifpb.pweb2.spendwise.service;

import br.edu.ifpb.pweb2.spendwise.model.Categoria;
import br.edu.ifpb.pweb2.spendwise.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarAtivas() {
        return categoriaRepository.findByAtivoTrueOrderByOrdemAsc();
    }
    
    public Categoria buscarPorId(Long id) {

        var categoria = categoriaRepository.findById(id);

        if (categoria.isEmpty()) {
            throw new IllegalArgumentException("Categoria não encontrada.");
        }

        return categoria.get();
    }
}