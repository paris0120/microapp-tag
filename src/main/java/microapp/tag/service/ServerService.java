package microapp.tag.service;

import microapp.tag.domain.ServerTag;
import microapp.tag.repository.ServerRepository;
import microapp.tag.service.dto.ServerDTO;
import microapp.tag.service.mapper.ServerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ServerTag}.
 */
@Service
@Transactional
public class ServerService {

    private final Logger log = LoggerFactory.getLogger(ServerService.class);

    private final ServerRepository serverRepository;

    private final ServerMapper serverMapper;

    public ServerService(ServerRepository serverRepository, ServerMapper serverMapper) {
        this.serverRepository = serverRepository;
        this.serverMapper = serverMapper;
    }

    /**
     * Save a server.
     *
     * @param serverDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ServerDTO> save(ServerDTO serverDTO) {
        log.debug("Request to save Server : {}", serverDTO);
        return serverRepository.save(serverMapper.toEntity(serverDTO)).map(serverMapper::toDto);
    }

    /**
     * Update a server.
     *
     * @param serverDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ServerDTO> update(ServerDTO serverDTO) {
        log.debug("Request to update Server : {}", serverDTO);
        return serverRepository.save(serverMapper.toEntity(serverDTO)).map(serverMapper::toDto);
    }

    /**
     * Partially update a server.
     *
     * @param serverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ServerDTO> partialUpdate(ServerDTO serverDTO) {
        log.debug("Request to partially update Server : {}", serverDTO);

        return serverRepository
            .findById(serverDTO.getId())
            .map(existingServer -> {
                serverMapper.partialUpdate(existingServer, serverDTO);

                return existingServer;
            })
            .flatMap(serverRepository::save)
            .map(serverMapper::toDto);
    }

    /**
     * Get all the servers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ServerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Servers");
        return serverRepository.findAllBy(pageable).map(serverMapper::toDto);
    }

    /**
     * Returns the number of servers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return serverRepository.count();
    }

    /**
     * Get one server by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ServerDTO> findOne(Long id) {
        log.debug("Request to get Server : {}", id);
        return serverRepository.findById(id).map(serverMapper::toDto);
    }

    /**
     * Delete the server by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Server : {}", id);
        return serverRepository.deleteById(id);
    }
}
