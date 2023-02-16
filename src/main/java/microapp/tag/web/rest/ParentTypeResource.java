package microapp.tag.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.tag.repository.ParentTypeRepository;
import microapp.tag.service.ParentTypeService;
import microapp.tag.service.dto.ParentTypeDTO;
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
 * REST controller for managing {@link microapp.tag.domain.ParentTypeTag}.
 */
@RestController
@RequestMapping("/api/admin")
public class ParentTypeResource {

    private final Logger log = LoggerFactory.getLogger(ParentTypeResource.class);

    private static final String ENTITY_NAME = "tagParentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParentTypeService parentTypeService;

    private final ParentTypeRepository parentTypeRepository;

    public ParentTypeResource(ParentTypeService parentTypeService, ParentTypeRepository parentTypeRepository) {
        this.parentTypeService = parentTypeService;
        this.parentTypeRepository = parentTypeRepository;
    }

    /**
     * {@code POST  /parent-types} : Create a new parentType.
     *
     * @param parentTypeDTO the parentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parentTypeDTO, or with status {@code 400 (Bad Request)} if the parentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parent-types")
    public Mono<ResponseEntity<ParentTypeDTO>> createParentType(@Valid @RequestBody ParentTypeDTO parentTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ParentType : {}", parentTypeDTO);
        if (parentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new parentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return parentTypeService
            .save(parentTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/parent-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /parent-types/:id} : Updates an existing parentType.
     *
     * @param id the id of the parentTypeDTO to save.
     * @param parentTypeDTO the parentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the parentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parent-types/{id}")
    public Mono<ResponseEntity<ParentTypeDTO>> updateParentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParentTypeDTO parentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParentType : {}, {}", id, parentTypeDTO);
        if (parentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parentTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return parentTypeService
                    .update(parentTypeDTO)
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
     * {@code PATCH  /parent-types/:id} : Partial updates given fields of an existing parentType, field will ignore if it is null
     *
     * @param id the id of the parentTypeDTO to save.
     * @param parentTypeDTO the parentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the parentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parent-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ParentTypeDTO>> partialUpdateParentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParentTypeDTO parentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParentType partially : {}, {}", id, parentTypeDTO);
        if (parentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parentTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ParentTypeDTO> result = parentTypeService.partialUpdate(parentTypeDTO);

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
     * {@code GET  /parent-types} : get all the parentTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parentTypes in body.
     */
    @GetMapping("/parent-types")
    public Mono<ResponseEntity<List<ParentTypeDTO>>> getAllParentTypes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ParentTypes");
        return parentTypeService
            .countAll()
            .zipWith(parentTypeService.findAll(pageable).collectList())
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
     * {@code GET  /parent-types/:id} : get the "id" parentType.
     *
     * @param id the id of the parentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parent-types/{id}")
    public Mono<ResponseEntity<ParentTypeDTO>> getParentType(@PathVariable Long id) {
        log.debug("REST request to get ParentType : {}", id);
        Mono<ParentTypeDTO> parentTypeDTO = parentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parentTypeDTO);
    }

    /**
     * {@code DELETE  /parent-types/:id} : delete the "id" parentType.
     *
     * @param id the id of the parentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parent-types/{id}")
    public Mono<ResponseEntity<Void>> deleteParentType(@PathVariable Long id) {
        log.debug("REST request to delete ParentType : {}", id);
        return parentTypeService
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
