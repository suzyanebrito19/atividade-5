package uea.pagamentos_api.repositories.pessoa;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import uea.pagamentos_api.dto.ResumoPessoaDto;
import uea.pagamentos_api.models.Pessoa;
import uea.pagamentos_api.repositories.filters.PessoaFilter;


public class PessoaRepositoryImpl implements PessoaRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<ResumoPessoaDto> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoPessoaDto> criteria = builder.createQuery(ResumoPessoaDto.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		criteria.select(builder.construct(ResumoPessoaDto.class, 
				root.get("nome"), root.get("ativo"), root.get("endereco").get("cidade"), root.get("endereco").get("estado")));
		Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
		if(predicates.length > 0) {
			criteria.where(predicates);
		}
		
		TypedQuery<ResumoPessoaDto> query = manager.createQuery(criteria);

		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(pessoaFilter));
	}
	
	private Long total(PessoaFilter PessoaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);

		Predicate[] predicates = criarRestricoes(PessoaFilter, builder, root);
		if (predicates.length > 0) {
			criteria.where(predicates);
		}

		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<ResumoPessoaDto> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber(), totalRegistroPagina = pageable.getPageSize(),
				primeiroRegistroPagina = paginaAtual * totalRegistroPagina;
		query.setFirstResult(primeiroRegistroPagina);
		query.setMaxResults(totalRegistroPagina);
	}
	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder,
			Root<Pessoa> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!ObjectUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get("nome")),
					"%" + pessoaFilter.getNome().toLowerCase() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
