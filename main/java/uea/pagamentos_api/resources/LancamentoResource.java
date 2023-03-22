package uea.pagamentos_api.resources;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uea.pagamentos_api.dto.ResumoLancamentoDto;
import uea.pagamentos_api.models.Lancamento;
import uea.pagamentos_api.repositories.filters.LancamentoFilter;
import uea.pagamentos_api.services.LancamentoService;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

	@Autowired
	private LancamentoService lancamentoService;

	@GetMapping
	public ResponseEntity<Page<ResumoLancamentoDto>> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		Page<ResumoLancamentoDto> resumos = lancamentoService.resumir(lancamentoFilter, pageable);
		return ResponseEntity.ok().body(resumos);
	}

	@PostMapping
	public ResponseEntity<Lancamento> criar(@RequestBody Lancamento lancamento) {
		Lancamento lancamentoSalva = lancamentoService.criar(lancamento);
		return ResponseEntity.ok().body(lancamentoSalva);
	}
	@GetMapping
	public ResponseEntity<List<Lancamento>> listar() {
		List<Lancamento> lancamentos = lancamentoService.Listar();
	return ResponseEntity.ok().body(lancamentos);
	}
	
	
	@GetMapping(value = "/{codigo}")
	public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoService.buscarPorCodigo(codigo);
		return ResponseEntity.ok().body(lancamento);
	}

	@DeleteMapping(value = "/{codigo}")
	public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
		lancamentoService.excluir(codigo);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{codigo}")
	public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @RequestBody Lancamento lancamento) {
		Lancamento lancamentoSalva = lancamentoService.atualizar(codigo, lancamento);
		return ResponseEntity.ok().body(lancamentoSalva);
	}

}