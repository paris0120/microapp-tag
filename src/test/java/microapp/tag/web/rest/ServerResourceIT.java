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
import microapp.tag.domain.ServerTag;
import microapp.tag.repository.EntityManager;
import microapp.tag.repository.ServerRepository;
import microapp.tag.service.dto.ServerDTO;
import microapp.tag.service.mapper.ServerMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ServerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ServerResourceIT {

    private static final String DEFAULT_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_SERVER = "BBBBBBBBBB";

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_DECODER = "AAAAAAAAAA";
    private static final String UPDATED_DECODER = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/servers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerMapper serverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ServerTag serverTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServerTag createEntity(EntityManager em) {
        ServerTag serverTag = new ServerTag().server(DEFAULT_SERVER).uuid(DEFAULT_UUID).decoder(DEFAULT_DECODER).password(DEFAULT_PASSWORD);
        return serverTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServerTag createUpdatedEntity(EntityManager em) {
        ServerTag serverTag = new ServerTag().server(UPDATED_SERVER).uuid(UPDATED_UUID).decoder(UPDATED_DECODER).password(UPDATED_PASSWORD);
        return serverTag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ServerTag.class).block();
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
        serverTag = createEntity(em);
    }

    @Test
    void createServer() throws Exception {
        int databaseSizeBeforeCreate = serverRepository.findAll().collectList().block().size();
        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeCreate + 1);
        ServerTag testServer = serverList.get(serverList.size() - 1);
        assertThat(testServer.getServer()).isEqualTo(DEFAULT_SERVER);
        assertThat(testServer.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testServer.getDecoder()).isEqualTo(DEFAULT_DECODER);
        assertThat(testServer.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    void createServerWithExistingId() throws Exception {
        // Create the Server with an existing ID
        serverTag.setId(1L);
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        int databaseSizeBeforeCreate = serverRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkServerIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverRepository.findAll().collectList().block().size();
        // set the field null
        serverTag.setServer(null);

        // Create the Server, which fails.
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllServers() {
        // Initialize the database
        serverRepository.save(serverTag).block();

        // Get all the serverList
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
            .value(hasItem(serverTag.getId().intValue()))
            .jsonPath("$.[*].server")
            .value(hasItem(DEFAULT_SERVER))
            .jsonPath("$.[*].uuid")
            .value(hasItem(DEFAULT_UUID.toString()))
            .jsonPath("$.[*].decoder")
            .value(hasItem(DEFAULT_DECODER))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD.toString()));
    }

    @Test
    void getServer() {
        // Initialize the database
        serverRepository.save(serverTag).block();

        // Get the server
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, serverTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(serverTag.getId().intValue()))
            .jsonPath("$.server")
            .value(is(DEFAULT_SERVER))
            .jsonPath("$.uuid")
            .value(is(DEFAULT_UUID.toString()))
            .jsonPath("$.decoder")
            .value(is(DEFAULT_DECODER))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD.toString()));
    }

    @Test
    void getNonExistingServer() {
        // Get the server
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingServer() throws Exception {
        // Initialize the database
        serverRepository.save(serverTag).block();

        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();

        // Update the server
        ServerTag updatedServerTag = serverRepository.findById(serverTag.getId()).block();
        updatedServerTag.server(UPDATED_SERVER).uuid(UPDATED_UUID).decoder(UPDATED_DECODER).password(UPDATED_PASSWORD);
        ServerDTO serverDTO = serverMapper.toDto(updatedServerTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
        ServerTag testServer = serverList.get(serverList.size() - 1);
        assertThat(testServer.getServer()).isEqualTo(UPDATED_SERVER);
        assertThat(testServer.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testServer.getDecoder()).isEqualTo(UPDATED_DECODER);
        assertThat(testServer.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void putNonExistingServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();
        serverTag.setId(count.incrementAndGet());

        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serverDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();
        serverTag.setId(count.incrementAndGet());

        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();
        serverTag.setId(count.incrementAndGet());

        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateServerWithPatch() throws Exception {
        // Initialize the database
        serverRepository.save(serverTag).block();

        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();

        // Update the server using partial update
        ServerTag partialUpdatedServerTag = new ServerTag();
        partialUpdatedServerTag.setId(serverTag.getId());

        partialUpdatedServerTag.decoder(UPDATED_DECODER).password(UPDATED_PASSWORD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServerTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServerTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
        ServerTag testServer = serverList.get(serverList.size() - 1);
        assertThat(testServer.getServer()).isEqualTo(DEFAULT_SERVER);
        assertThat(testServer.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testServer.getDecoder()).isEqualTo(UPDATED_DECODER);
        assertThat(testServer.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void fullUpdateServerWithPatch() throws Exception {
        // Initialize the database
        serverRepository.save(serverTag).block();

        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();

        // Update the server using partial update
        ServerTag partialUpdatedServerTag = new ServerTag();
        partialUpdatedServerTag.setId(serverTag.getId());

        partialUpdatedServerTag.server(UPDATED_SERVER).uuid(UPDATED_UUID).decoder(UPDATED_DECODER).password(UPDATED_PASSWORD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServerTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServerTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
        ServerTag testServer = serverList.get(serverList.size() - 1);
        assertThat(testServer.getServer()).isEqualTo(UPDATED_SERVER);
        assertThat(testServer.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testServer.getDecoder()).isEqualTo(UPDATED_DECODER);
        assertThat(testServer.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    void patchNonExistingServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();
        serverTag.setId(count.incrementAndGet());

        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, serverDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();
        serverTag.setId(count.incrementAndGet());

        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamServer() throws Exception {
        int databaseSizeBeforeUpdate = serverRepository.findAll().collectList().block().size();
        serverTag.setId(count.incrementAndGet());

        // Create the Server
        ServerDTO serverDTO = serverMapper.toDto(serverTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serverDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Server in the database
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteServer() {
        // Initialize the database
        serverRepository.save(serverTag).block();

        int databaseSizeBeforeDelete = serverRepository.findAll().collectList().block().size();

        // Delete the server
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, serverTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ServerTag> serverList = serverRepository.findAll().collectList().block();
        assertThat(serverList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
