package microapp.tag.repository;

import microapp.tag.domain.ServerTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ServerTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServerRepository extends ReactiveCrudRepository<ServerTag, Long>, ServerRepositoryInternal {
    Flux<ServerTag> findAllBy(Pageable pageable);

    @Override
    <S extends ServerTag> Mono<S> save(S entity);

    @Override
    Flux<ServerTag> findAll();

    @Override
    Mono<ServerTag> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ServerRepositoryInternal {
    <S extends ServerTag> Mono<S> save(S entity);

    Flux<ServerTag> findAllBy(Pageable pageable);

    Flux<ServerTag> findAll();

    Mono<ServerTag> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ServerTag> findAllBy(Pageable pageable, Criteria criteria);

}
