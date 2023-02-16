package microapp.tag.repository;

import microapp.tag.domain.ParentTypeTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ParentTypeTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParentTypeRepository extends ReactiveCrudRepository<ParentTypeTag, Long>, ParentTypeRepositoryInternal {
    Flux<ParentTypeTag> findAllBy(Pageable pageable);

    @Override
    <S extends ParentTypeTag> Mono<S> save(S entity);

    @Override
    Flux<ParentTypeTag> findAll();

    @Override
    Mono<ParentTypeTag> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ParentTypeRepositoryInternal {
    <S extends ParentTypeTag> Mono<S> save(S entity);

    Flux<ParentTypeTag> findAllBy(Pageable pageable);

    Flux<ParentTypeTag> findAll();

    Mono<ParentTypeTag> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ParentTypeTag> findAllBy(Pageable pageable, Criteria criteria);

}
