package microapp.tag.service;

import microapp.tag.domain.TagTag;
import microapp.tag.repository.TagRepository;
import microapp.tag.service.dto.TagDTO;
import microapp.tag.service.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TagTag}.
 */
@Service
@Transactional
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    /**
     * Save a tag.
     *
     * @param tagDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TagDTO> save(TagDTO tagDTO) {
        log.debug("Request to save Tag : {}", tagDTO);
        return tagRepository.save(tagMapper.toEntity(tagDTO)).map(tagMapper::toDto);
    }

    /**
     * Update a tag.
     *
     * @param tagDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TagDTO> update(TagDTO tagDTO) {
        log.debug("Request to update Tag : {}", tagDTO);
        return tagRepository.save(tagMapper.toEntity(tagDTO)).map(tagMapper::toDto);
    }

    /**
     * Partially update a tag.
     *
     * @param tagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TagDTO> partialUpdate(TagDTO tagDTO) {
        log.debug("Request to partially update Tag : {}", tagDTO);

        return tagRepository
            .findById(tagDTO.getId())
            .map(existingTag -> {
                tagMapper.partialUpdate(existingTag, tagDTO);

                return existingTag;
            })
            .flatMap(tagRepository::save)
            .map(tagMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TagDTO> findAll() {
        log.debug("Request to get all Tags");
        return tagRepository.findAllByOrderByTagAsc().map(tagMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TagDTO> findAll(String server, String type, long id) {
        log.debug("Request to get all Tags");
        return tagRepository.findAllByParentServerAndParentTypeAAndParentIdOrderByTagAsc(server, type, id).map(tagMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @param server the server name.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TagDTO> findAll(String server) {
        log.debug("Request to get all Tags");
        return tagRepository.findAllByParentServerOrderByTagAsc(server).map(tagMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @param server the server name.
     * @param type the parent type name.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TagDTO> findAll(String server, String type) {
        log.debug("Request to get all Tags");
        return tagRepository.findAllByParentServerAndParentTypeOrderByTagAsc(server, type).map(tagMapper::toDto);
    }

    /**
     * Returns the number of tags available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return tagRepository.count();
    }

    /**
     * Get one tag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TagDTO> findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id).map(tagMapper::toDto);
    }

    /**
     * Delete the tag by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        return tagRepository.deleteById(id);
    }
}
