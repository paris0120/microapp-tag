package microapp.tag.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import microapp.tag.IntegrationTest;
import microapp.tag.domain.TagTag;
import microapp.tag.repository.EntityManager;
import microapp.tag.repository.TagRepository;
import microapp.tag.service.dto.TagDTO;
import microapp.tag.service.mapper.TagMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link TagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TagResourceIT {

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_FILL_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FILL_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BORDER_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BORDER_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;

    private static final String DEFAULT_PARENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_SERVER = "BBBBBBBBBB";

    private static final UUID DEFAULT_PARENT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_PARENT_UUID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TagTag tagTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagTag createEntity(EntityManager em) {
        TagTag tagTag = new TagTag()
            .tag(DEFAULT_TAG)
            .textColor(DEFAULT_TEXT_COLOR)
            .fillColor(DEFAULT_FILL_COLOR)
            .borderColor(DEFAULT_BORDER_COLOR)
            .icon(DEFAULT_ICON)
            .parentId(DEFAULT_PARENT_ID)
            .parentType(DEFAULT_PARENT_TYPE)
            .parentServer(DEFAULT_PARENT_SERVER)
            .parentUuid(DEFAULT_PARENT_UUID);
        return tagTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagTag createUpdatedEntity(EntityManager em) {
        TagTag tagTag = new TagTag()
            .tag(UPDATED_TAG)
            .textColor(UPDATED_TEXT_COLOR)
            .fillColor(UPDATED_FILL_COLOR)
            .borderColor(UPDATED_BORDER_COLOR)
            .icon(UPDATED_ICON)
            .parentId(UPDATED_PARENT_ID)
            .parentType(UPDATED_PARENT_TYPE)
            .parentServer(UPDATED_PARENT_SERVER)
            .parentUuid(UPDATED_PARENT_UUID);
        return tagTag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TagTag.class).block();
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
        tagTag = createEntity(em);
    }

    @Test
    void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().collectList().block().size();
        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        TagTag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testTag.getTextColor()).isEqualTo(DEFAULT_TEXT_COLOR);
        assertThat(testTag.getFillColor()).isEqualTo(DEFAULT_FILL_COLOR);
        assertThat(testTag.getBorderColor()).isEqualTo(DEFAULT_BORDER_COLOR);
        assertThat(testTag.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testTag.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testTag.getParentType()).isEqualTo(DEFAULT_PARENT_TYPE);
        assertThat(testTag.getParentServer()).isEqualTo(DEFAULT_PARENT_SERVER);
        assertThat(testTag.getParentUuid()).isEqualTo(DEFAULT_PARENT_UUID);
    }

    @Test
    void createTagWithExistingId() throws Exception {
        // Create the Tag with an existing ID
        tagTag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        int databaseSizeBeforeCreate = tagRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().collectList().block().size();
        // set the field null
        tagTag.setTag(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkParentServerIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().collectList().block().size();
        // set the field null
        tagTag.setParentServer(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTags() {
        // Initialize the database
        tagRepository.save(tagTag).block();

        // Get all the tagList
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
            .value(hasItem(tagTag.getId().intValue()))
            .jsonPath("$.[*].tag")
            .value(hasItem(DEFAULT_TAG))
            .jsonPath("$.[*].textColor")
            .value(hasItem(DEFAULT_TEXT_COLOR))
            .jsonPath("$.[*].fillColor")
            .value(hasItem(DEFAULT_FILL_COLOR))
            .jsonPath("$.[*].borderColor")
            .value(hasItem(DEFAULT_BORDER_COLOR))
            .jsonPath("$.[*].icon")
            .value(hasItem(DEFAULT_ICON))
            .jsonPath("$.[*].parentId")
            .value(hasItem(DEFAULT_PARENT_ID.intValue()))
            .jsonPath("$.[*].parentType")
            .value(hasItem(DEFAULT_PARENT_TYPE))
            .jsonPath("$.[*].parentServer")
            .value(hasItem(DEFAULT_PARENT_SERVER))
            .jsonPath("$.[*].parentUuid")
            .value(hasItem(DEFAULT_PARENT_UUID.toString()));
    }

    @Test
    void getTag() {
        // Initialize the database
        tagRepository.save(tagTag).block();

        // Get the tag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, tagTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(tagTag.getId().intValue()))
            .jsonPath("$.tag")
            .value(is(DEFAULT_TAG))
            .jsonPath("$.textColor")
            .value(is(DEFAULT_TEXT_COLOR))
            .jsonPath("$.fillColor")
            .value(is(DEFAULT_FILL_COLOR))
            .jsonPath("$.borderColor")
            .value(is(DEFAULT_BORDER_COLOR))
            .jsonPath("$.icon")
            .value(is(DEFAULT_ICON))
            .jsonPath("$.parentId")
            .value(is(DEFAULT_PARENT_ID.intValue()))
            .jsonPath("$.parentType")
            .value(is(DEFAULT_PARENT_TYPE))
            .jsonPath("$.parentServer")
            .value(is(DEFAULT_PARENT_SERVER))
            .jsonPath("$.parentUuid")
            .value(is(DEFAULT_PARENT_UUID.toString()));
    }

    @Test
    void getNonExistingTag() {
        // Get the tag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTag() throws Exception {
        // Initialize the database
        tagRepository.save(tagTag).block();

        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();

        // Update the tag
        TagTag updatedTagTag = tagRepository.findById(tagTag.getId()).block();
        updatedTagTag
            .tag(UPDATED_TAG)
            .textColor(UPDATED_TEXT_COLOR)
            .fillColor(UPDATED_FILL_COLOR)
            .borderColor(UPDATED_BORDER_COLOR)
            .icon(UPDATED_ICON)
            .parentId(UPDATED_PARENT_ID)
            .parentType(UPDATED_PARENT_TYPE)
            .parentServer(UPDATED_PARENT_SERVER)
            .parentUuid(UPDATED_PARENT_UUID);
        TagDTO tagDTO = tagMapper.toDto(updatedTagTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        TagTag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testTag.getTextColor()).isEqualTo(UPDATED_TEXT_COLOR);
        assertThat(testTag.getFillColor()).isEqualTo(UPDATED_FILL_COLOR);
        assertThat(testTag.getBorderColor()).isEqualTo(UPDATED_BORDER_COLOR);
        assertThat(testTag.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTag.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testTag.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testTag.getParentServer()).isEqualTo(UPDATED_PARENT_SERVER);
        assertThat(testTag.getParentUuid()).isEqualTo(UPDATED_PARENT_UUID);
    }

    @Test
    void putNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tagTag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, tagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tagTag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tagTag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.save(tagTag).block();

        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();

        // Update the tag using partial update
        TagTag partialUpdatedTagTag = new TagTag();
        partialUpdatedTagTag.setId(tagTag.getId());

        partialUpdatedTagTag
            .tag(UPDATED_TAG)
            .fillColor(UPDATED_FILL_COLOR)
            .borderColor(UPDATED_BORDER_COLOR)
            .icon(UPDATED_ICON)
            .parentType(UPDATED_PARENT_TYPE)
            .parentUuid(UPDATED_PARENT_UUID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTagTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTagTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        TagTag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testTag.getTextColor()).isEqualTo(DEFAULT_TEXT_COLOR);
        assertThat(testTag.getFillColor()).isEqualTo(UPDATED_FILL_COLOR);
        assertThat(testTag.getBorderColor()).isEqualTo(UPDATED_BORDER_COLOR);
        assertThat(testTag.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTag.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testTag.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testTag.getParentServer()).isEqualTo(DEFAULT_PARENT_SERVER);
        assertThat(testTag.getParentUuid()).isEqualTo(UPDATED_PARENT_UUID);
    }

    @Test
    void fullUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.save(tagTag).block();

        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();

        // Update the tag using partial update
        TagTag partialUpdatedTagTag = new TagTag();
        partialUpdatedTagTag.setId(tagTag.getId());

        partialUpdatedTagTag
            .tag(UPDATED_TAG)
            .textColor(UPDATED_TEXT_COLOR)
            .fillColor(UPDATED_FILL_COLOR)
            .borderColor(UPDATED_BORDER_COLOR)
            .icon(UPDATED_ICON)
            .parentId(UPDATED_PARENT_ID)
            .parentType(UPDATED_PARENT_TYPE)
            .parentServer(UPDATED_PARENT_SERVER)
            .parentUuid(UPDATED_PARENT_UUID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTagTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTagTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        TagTag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testTag.getTextColor()).isEqualTo(UPDATED_TEXT_COLOR);
        assertThat(testTag.getFillColor()).isEqualTo(UPDATED_FILL_COLOR);
        assertThat(testTag.getBorderColor()).isEqualTo(UPDATED_BORDER_COLOR);
        assertThat(testTag.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTag.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testTag.getParentType()).isEqualTo(UPDATED_PARENT_TYPE);
        assertThat(testTag.getParentServer()).isEqualTo(UPDATED_PARENT_SERVER);
        assertThat(testTag.getParentUuid()).isEqualTo(UPDATED_PARENT_UUID);
    }

    @Test
    void patchNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tagTag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, tagDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tagTag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().collectList().block().size();
        tagTag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tagTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(tagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Tag in the database
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTag() {
        // Initialize the database
        tagRepository.save(tagTag).block();

        int databaseSizeBeforeDelete = tagRepository.findAll().collectList().block().size();

        // Delete the tag
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, tagTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TagTag> tagList = tagRepository.findAll().collectList().block();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
