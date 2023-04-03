package microapp.tag.repository;

import microapp.tag.domain.TagTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TagTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends ReactiveCrudRepository<TagTag, Long>, TagRepositoryInternal {
    Flux<TagTag> findAllByOrderByTagAsc();

    Flux<TagTag> findAllByParentServerOrderByTagAsc(String server);
    Flux<TagTag> findAllByParentServerAndParentTypeOrderByTagAsc(String server, String type);
    Flux<TagTag> findAllByParentServerAndParentTypeAAndParentIdOrderByTagAsc(String server, String type, long id);

    @Override
    <S extends TagTag> Mono<S> save(S entity);

    @Override
    Flux<TagTag> findAll();

    @Override
    Mono<TagTag> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TagRepositoryInternal {
    <S extends TagTag> Mono<S> save(S entity);

    Flux<TagTag> findAllBy(Pageable pageable);

    Flux<TagTag> findAll();

    Mono<TagTag> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TagTag> findAllBy(Pageable pageable, Criteria criteria);

}
