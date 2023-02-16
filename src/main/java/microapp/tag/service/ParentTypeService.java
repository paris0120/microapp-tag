package microapp.tag.service;

import microapp.tag.domain.ParentTypeTag;
import microapp.tag.repository.ParentTypeRepository;
import microapp.tag.service.dto.ParentTypeDTO;
import microapp.tag.service.mapper.ParentTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ParentTypeTag}.
 */
@Service
@Transactional
public class ParentTypeService {

    private final Logger log = LoggerFactory.getLogger(ParentTypeService.class);

    private final ParentTypeRepository parentTypeRepository;

    private final ParentTypeMapper parentTypeMapper;

    public ParentTypeService(ParentTypeRepository parentTypeRepository, ParentTypeMapper parentTypeMapper) {
        this.parentTypeRepository = parentTypeRepository;
        this.parentTypeMapper = parentTypeMapper;
    }

    /**
     * Save a parentType.
     *
     * @param parentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParentTypeDTO> save(ParentTypeDTO parentTypeDTO) {
        log.debug("Request to save ParentType : {}", parentTypeDTO);
        return parentTypeRepository.save(parentTypeMapper.toEntity(parentTypeDTO)).map(parentTypeMapper::toDto);
    }

    /**
     * Update a parentType.
     *
     * @param parentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParentTypeDTO> update(ParentTypeDTO parentTypeDTO) {
        log.debug("Request to update ParentType : {}", parentTypeDTO);
        return parentTypeRepository.save(parentTypeMapper.toEntity(parentTypeDTO)).map(parentTypeMapper::toDto);
    }

    /**
     * Partially update a parentType.
     *
     * @param parentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParentTypeDTO> partialUpdate(ParentTypeDTO parentTypeDTO) {
        log.debug("Request to partially update ParentType : {}", parentTypeDTO);

        return parentTypeRepository
            .findById(parentTypeDTO.getId())
            .map(existingParentType -> {
                parentTypeMapper.partialUpdate(existingParentType, parentTypeDTO);

                return existingParentType;
            })
            .flatMap(parentTypeRepository::save)
            .map(parentTypeMapper::toDto);
    }

    /**
     * Get all the parentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ParentTypes");
        return parentTypeRepository.findAllBy(pageable).map(parentTypeMapper::toDto);
    }

    /**
     * Returns the number of parentTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return parentTypeRepository.count();
    }

    /**
     * Get one parentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParentTypeDTO> findOne(Long id) {
        log.debug("Request to get ParentType : {}", id);
        return parentTypeRepository.findById(id).map(parentTypeMapper::toDto);
    }

    /**
     * Delete the parentType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ParentType : {}", id);
        return parentTypeRepository.deleteById(id);
    }
}
