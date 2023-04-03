package microapp.tag.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import microapp.tag.IntegrationTest;
import microapp.tag.domain.ParentTypeTag;
import microapp.tag.repository.EntityManager;
import microapp.tag.repository.ParentTypeRepository;
import microapp.tag.service.dto.ParentTypeDTO;
import microapp.tag.service.mapper.ParentTypeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ParentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ParentTypeResourceIT {

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String DEFAULT_PARENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_SERVER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_USER_MANAGEABLE = false;
    private static final Boolean UPDATED_USER_MANAGEABLE = true;

    private static final Boolean DEFAULT_IS_ENCRYPTED = false;
    private static final Boolean UPDATED_IS_ENCRYPTED = true;

    private static final String ENTITY_API_URL = "/api/parent-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParentTypeRepository parentTypeRepository;

    @Autowired
    private ParentTypeMapper parentTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ParentTypeTag parentTypeTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParentTypeTag createEntity(EntityManager em) {
        ParentTypeTag parentTypeTag = new ParentTypeTag()
            .topic(DEFAULT_TOPIC)
            .parentId(DEFAULT_PARENT_ID)
            .parentType(DEFAULT_PARENT_TYPE)
            .server(DEFAULT_SERVER)
            .userManageable(DEFAULT_USER_MANAGEABLE)
            .isEncrypted(DEFAULT_IS_ENCRYPTED);
        return parentTypeTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParentTypeTag createUpdatedEntity(EntityManager em) {
        ParentTypeTag parentTypeTag = new ParentTypeTag()
            .topic(UPDATED_TOPIC)
            .parentId(UPDATED_PARENT_ID)
            .parentType(UPDATED_PARENT_TYPE)
            .server(UPDATED_SERVER)
            .userManageable(UPDATED_USER_MANAGEABLE)
            .isEncrypted(UPDATED_IS_ENCRYPTED);
        return parentTypeTag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ParentTypeTag.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        parentTypeTag = createEntity(em);
    }

    @Test
    void createParentType() throws Exception {
        int databaseSizeBeforeCreate = parentTypeRepository.findAll().collectList().block().size();
        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ParentTypeTag testParentType = parentTypeList.get(parentTypeList.size() - 1);
        assertThat(testParentType.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testParentType.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testParentType.getParentType()).isEqualTo(DEFAULT_PARENT_TYPE);
        assertThat(testParentType.getServer()).isEqualTo(DEFAULT_SERVER);
        assertThat(testParentType.getUserManageable()).isEqualTo(DEFAULT_USER_MANAGEABLE);
        assertThat(testParentType.getIsEncrypted()).isEqualTo(DEFAULT_IS_ENCRYPTED);
    }

    @Test
    void createParentTypeWithExistingId() throws Exception {
        // Create the ParentType with an existing ID
        parentTypeTag.setId(1L);
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        int databaseSizeBeforeCreate = parentTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTopicIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentTypeRepository.findAll().collectList().block().size();
        // set the field null
        parentTypeTag.setTopic(null);

        // Create the ParentType, which fails.
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkParentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentTypeRepository.findAll().collectList().block().size();
        // set the field null
        parentTypeTag.setParentId(null);

        // Create the ParentType, which fails.
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkParentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentTypeRepository.findAll().collectList().block().size();
        // set the field null
        parentTypeTag.setParentType(null);

        // Create the ParentType, which fails.
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkServerIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentTypeRepository.findAll().collectList().block().size();
        // set the field null
        parentTypeTag.setServer(null);

        // Create the ParentType, which fails.
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserManageableIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentTypeRepository.findAll().collectList().block().size();
        // set the field null
        parentTypeTag.setUserManageable(null);

        // Create the ParentType, which fails.
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIsEncryptedIsRequired() throws Exception {
        int databaseSizeBeforeTest = parentTypeRepository.findAll().collectList().block().size();
        // set the field null
        parentTypeTag.setIsEncrypted(null);

        // Create the ParentType, which fails.
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParentTypes() {
        // Initialize the database
        parentTypeRepository.save(parentTypeTag).block();

        // Get all the parentTypeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(parentTypeTag.getId().intValue()))
            .jsonPath("$.[*].topic")
            .value(hasItem(DEFAULT_TOPIC))
            .jsonPath("$.[*].parentId")
            .value(hasItem(DEFAULT_PARENT_ID.intValue()))
            .jsonPath("$.[*].parentType")
            .value(hasItem(DEFAULT_PARENT_TYPE))
            .jsonPath("$.[*].server")
            .value(hasItem(DEFAULT_SERVER))
            .jsonPath("$.[*].userManageable")
            .value(hasItem(DEFAULT_USER_MANAGEABLE.booleanValue()))
            .jsonPath("$.[*].isEncrypted")
            .value(hasItem(DEFAULT_IS_ENCRYPTED.booleanValue()));
    }

    @Test
    void getParentType() {
        // Initialize the database
        parentTypeRepository.save(parentTypeTag).block();

        // Get the parentType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, parentTypeTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(parentTypeTag.getId().intValue()))
            .jsonPath("$.topic")
            .value(is(DEFAULT_TOPIC))
            .jsonPath("$.parentId")
            .value(is(DEFAULT_PARENT_ID.intValue()))
            .jsonPath("$.parentType")
            .value(is(DEFAULT_PARENT_TYPE))
            .jsonPath("$.server")
            .value(is(DEFAULT_SERVER))
            .jsonPath("$.userManageable")
            .value(is(DEFAULT_USER_MANAGEABLE.booleanValue()))
            .jsonPath("$.isEncrypted")
            .value(is(DEFAULT_IS_ENCRYPTED.booleanValue()));
    }

    @Test
    void getNonExistingParentType() {
        // Get the parentType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParentType() throws Exception {
        // Initialize the database
        parentTypeRepository.save(parentTypeTag).block();

        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();

        // Update the parentType
        ParentTypeTag updatedParentTypeTag = parentTypeRepository.findById(parentTypeTag.getId()).block();
        updatedParentTypeTag
            .topic(UPDATED_TOPIC)
            .parentId(UPDATED_PARENT_ID)
            .parentType(UPDATED_PARENT_TYPE)
            .server(UPDATED_SERVER)
            .userManageable(UPDATED_USER_MANAGEABLE)
            .isEncrypted(UPDATED_IS_ENCRYPTED);
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(updatedParentTypeTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parentTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
        ParentTypeTag testParentType = parentTypeList.get(parentTypeList.size() - 1);
        assertThat(testParentType.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testParentType.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testParentType.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testParentType.getServer()).isEqualTo(UPDATED_SERVER);
        assertThat(testParentType.getUserManageable()).isEqualTo(UPDATED_USER_MANAGEABLE);
        assertThat(testParentType.getIsEncrypted()).isEqualTo(UPDATED_IS_ENCRYPTED);
    }

    @Test
    void putNonExistingParentType() throws Exception {
        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();
        parentTypeTag.setId(count.incrementAndGet());

        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parentTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParentType() throws Exception {
        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();
        parentTypeTag.setId(count.incrementAndGet());

        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParentType() throws Exception {
        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();
        parentTypeTag.setId(count.incrementAndGet());

        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParentTypeWithPatch() throws Exception {
        // Initialize the database
        parentTypeRepository.save(parentTypeTag).block();

        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();

        // Update the parentType using partial update
        ParentTypeTag partialUpdatedParentTypeTag = new ParentTypeTag();
        partialUpdatedParentTypeTag.setId(parentTypeTag.getId());

        partialUpdatedParentTypeTag.topic(UPDATED_TOPIC).parentId(UPDATED_PARENT_ID).parentType(UPDATED_PARENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParentTypeTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParentTypeTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
        ParentTypeTag testParentType = parentTypeList.get(parentTypeList.size() - 1);
        assertThat(testParentType.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testParentType.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testParentType.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testParentType.getServer()).isEqualTo(DEFAULT_SERVER);
        assertThat(testParentType.getUserManageable()).isEqualTo(DEFAULT_USER_MANAGEABLE);
        assertThat(testParentType.getIsEncrypted()).isEqualTo(DEFAULT_IS_ENCRYPTED);
    }

    @Test
    void fullUpdateParentTypeWithPatch() throws Exception {
        // Initialize the database
        parentTypeRepository.save(parentTypeTag).block();

        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();

        // Update the parentType using partial update
        ParentTypeTag partialUpdatedParentTypeTag = new ParentTypeTag();
        partialUpdatedParentTypeTag.setId(parentTypeTag.getId());

        partialUpdatedParentTypeTag
            .topic(UPDATED_TOPIC)
            .parentId(UPDATED_PARENT_ID)
            .parentType(UPDATED_PARENT_TYPE)
            .server(UPDATED_SERVER)
            .userManageable(UPDATED_USER_MANAGEABLE)
            .isEncrypted(UPDATED_IS_ENCRYPTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParentTypeTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParentTypeTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
        ParentTypeTag testParentType = parentTypeList.get(parentTypeList.size() - 1);
        assertThat(testParentType.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testParentType.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testParentType.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testParentType.getServer()).isEqualTo(UPDATED_SERVER);
        assertThat(testParentType.getUserManageable()).isEqualTo(UPDATED_USER_MANAGEABLE);
        assertThat(testParentType.getIsEncrypted()).isEqualTo(UPDATED_IS_ENCRYPTED);
    }

    @Test
    void patchNonExistingParentType() throws Exception {
        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();
        parentTypeTag.setId(count.incrementAndGet());

        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, parentTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParentType() throws Exception {
        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();
        parentTypeTag.setId(count.incrementAndGet());

        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParentType() throws Exception {
        int databaseSizeBeforeUpdate = parentTypeRepository.findAll().collectList().block().size();
        parentTypeTag.setId(count.incrementAndGet());

        // Create the ParentType
        ParentTypeDTO parentTypeDTO = parentTypeMapper.toDto(parentTypeTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parentTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParentType in the database
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParentType() {
        // Initialize the database
        parentTypeRepository.save(parentTypeTag).block();

        int databaseSizeBeforeDelete = parentTypeRepository.findAll().collectList().block().size();

        // Delete the parentType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, parentTypeTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ParentTypeTag> parentTypeList = parentTypeRepository.findAll().collectList().block();
        assertThat(parentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
