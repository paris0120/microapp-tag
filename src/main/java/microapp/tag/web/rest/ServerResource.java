package microapp.tag.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.tag.repository.ServerRepository;
import microapp.tag.service.ServerService;
import microapp.tag.service.dto.ServerDTO;
import microapp.tag.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link microapp.tag.domain.ServerTag}.
 */
@RestController
@RequestMapping("/api/admin")
public class ServerResource {

    private final Logger log = LoggerFactory.getLogger(ServerResource.class);

    private static final String ENTITY_NAME = "tagServer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServerService serverService;

    private final ServerRepository serverRepository;

    public ServerResource(ServerService serverService, ServerRepository serverRepository) {
        this.serverService = serverService;
        this.serverRepository = serverRepository;
    }

    /**
     * {@code POST  /servers} : Create a new server.
     *
     * @param serverDTO the serverDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serverDTO, or with status {@code 400 (Bad Request)} if the server has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/servers")
    public Mono<ResponseEntity<ServerDTO>> createServer(@Valid @RequestBody ServerDTO serverDTO) throws URISyntaxException {
        log.debug("REST request to save Server : {}", serverDTO);
        if (serverDTO.getId() != null) {
            throw new BadRequestAlertException("A new server cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return serverService
            .save(serverDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/servers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /servers/:id} : Updates an existing server.
     *
     * @param id the id of the serverDTO to save.
     * @param serverDTO the serverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serverDTO,
     * or with status {@code 400 (Bad Request)} if the serverDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/servers/{id}")
    public Mono<ResponseEntity<ServerDTO>> updateServer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServerDTO serverDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Server : {}, {}", id, serverDTO);
        if (serverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return serverService
                    .update(serverDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /servers/:id} : Partial updates given fields of an existing server, field will ignore if it is null
     *
     * @param id the id of the serverDTO to save.
     * @param serverDTO the serverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serverDTO,
     * or with status {@code 400 (Bad Request)} if the serverDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serverDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/servers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ServerDTO>> partialUpdateServer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServerDTO serverDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Server partially : {}, {}", id, serverDTO);
        if (serverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serverRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ServerDTO> result = serverService.partialUpdate(serverDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /servers} : get all the servers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servers in body.
     */
    @GetMapping("/servers")
    public Mono<ResponseEntity<List<ServerDTO>>> getAllServers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Servers");
        return serverService
            .countAll()
            .zipWith(serverService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /servers/:id} : get the "id" server.
     *
     * @param id the id of the serverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serverDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/servers/{id}")
    public Mono<ResponseEntity<ServerDTO>> getServer(@PathVariable Long id) {
        log.debug("REST request to get Server : {}", id);
        Mono<ServerDTO> serverDTO = serverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serverDTO);
    }

    /**
     * {@code DELETE  /servers/:id} : delete the "id" server.
     *
     * @param id the id of the serverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/servers/{id}")
    public Mono<ResponseEntity<Void>> deleteServer(@PathVariable Long id) {
        log.debug("REST request to delete Server : {}", id);
        return serverService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
