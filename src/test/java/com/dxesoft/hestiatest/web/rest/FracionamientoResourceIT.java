package com.dxesoft.hestiatest.web.rest;

import static com.dxesoft.hestiatest.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dxesoft.hestiatest.IntegrationTest;
import com.dxesoft.hestiatest.domain.Fracionamiento;
import com.dxesoft.hestiatest.domain.enumeration.StatusFraccionamiento;
import com.dxesoft.hestiatest.repository.FracionamientoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FracionamientoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FracionamientoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TOTAL_HOUSES = 49;
    private static final Integer UPDATED_TOTAL_HOUSES = 50;

    private static final BigDecimal DEFAULT_COST_BY_HOUSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST_BY_HOUSE = new BigDecimal(2);

    private static final StatusFraccionamiento DEFAULT_STATUS = StatusFraccionamiento.ACTIVO;
    private static final StatusFraccionamiento UPDATED_STATUS = StatusFraccionamiento.TERMINADO;

    private static final String DEFAULT_CONTRACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fracionamientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FracionamientoRepository fracionamientoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFracionamientoMockMvc;

    private Fracionamiento fracionamiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fracionamiento createEntity(EntityManager em) {
        Fracionamiento fracionamiento = new Fracionamiento()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .totalHouses(DEFAULT_TOTAL_HOUSES)
            .costByHouse(DEFAULT_COST_BY_HOUSE)
            .status(DEFAULT_STATUS)
            .contract(DEFAULT_CONTRACT);
        return fracionamiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fracionamiento createUpdatedEntity(EntityManager em) {
        Fracionamiento fracionamiento = new Fracionamiento()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .totalHouses(UPDATED_TOTAL_HOUSES)
            .costByHouse(UPDATED_COST_BY_HOUSE)
            .status(UPDATED_STATUS)
            .contract(UPDATED_CONTRACT);
        return fracionamiento;
    }

    @BeforeEach
    public void initTest() {
        fracionamiento = createEntity(em);
    }

    @Test
    @Transactional
    void createFracionamiento() throws Exception {
        int databaseSizeBeforeCreate = fracionamientoRepository.findAll().size();
        // Create the Fracionamiento
        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isCreated());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeCreate + 1);
        Fracionamiento testFracionamiento = fracionamientoList.get(fracionamientoList.size() - 1);
        assertThat(testFracionamiento.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFracionamiento.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFracionamiento.getTotalHouses()).isEqualTo(DEFAULT_TOTAL_HOUSES);
        assertThat(testFracionamiento.getCostByHouse()).isEqualByComparingTo(DEFAULT_COST_BY_HOUSE);
        assertThat(testFracionamiento.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFracionamiento.getContract()).isEqualTo(DEFAULT_CONTRACT);
    }

    @Test
    @Transactional
    void createFracionamientoWithExistingId() throws Exception {
        // Create the Fracionamiento with an existing ID
        fracionamiento.setId(1L);

        int databaseSizeBeforeCreate = fracionamientoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fracionamientoRepository.findAll().size();
        // set the field null
        fracionamiento.setName(null);

        // Create the Fracionamiento, which fails.

        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fracionamientoRepository.findAll().size();
        // set the field null
        fracionamiento.setStartDate(null);

        // Create the Fracionamiento, which fails.

        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalHousesIsRequired() throws Exception {
        int databaseSizeBeforeTest = fracionamientoRepository.findAll().size();
        // set the field null
        fracionamiento.setTotalHouses(null);

        // Create the Fracionamiento, which fails.

        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostByHouseIsRequired() throws Exception {
        int databaseSizeBeforeTest = fracionamientoRepository.findAll().size();
        // set the field null
        fracionamiento.setCostByHouse(null);

        // Create the Fracionamiento, which fails.

        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = fracionamientoRepository.findAll().size();
        // set the field null
        fracionamiento.setStatus(null);

        // Create the Fracionamiento, which fails.

        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractIsRequired() throws Exception {
        int databaseSizeBeforeTest = fracionamientoRepository.findAll().size();
        // set the field null
        fracionamiento.setContract(null);

        // Create the Fracionamiento, which fails.

        restFracionamientoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFracionamientos() throws Exception {
        // Initialize the database
        fracionamientoRepository.saveAndFlush(fracionamiento);

        // Get all the fracionamientoList
        restFracionamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fracionamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalHouses").value(hasItem(DEFAULT_TOTAL_HOUSES)))
            .andExpect(jsonPath("$.[*].costByHouse").value(hasItem(sameNumber(DEFAULT_COST_BY_HOUSE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].contract").value(hasItem(DEFAULT_CONTRACT)));
    }

    @Test
    @Transactional
    void getFracionamiento() throws Exception {
        // Initialize the database
        fracionamientoRepository.saveAndFlush(fracionamiento);

        // Get the fracionamiento
        restFracionamientoMockMvc
            .perform(get(ENTITY_API_URL_ID, fracionamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fracionamiento.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.totalHouses").value(DEFAULT_TOTAL_HOUSES))
            .andExpect(jsonPath("$.costByHouse").value(sameNumber(DEFAULT_COST_BY_HOUSE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.contract").value(DEFAULT_CONTRACT));
    }

    @Test
    @Transactional
    void getNonExistingFracionamiento() throws Exception {
        // Get the fracionamiento
        restFracionamientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFracionamiento() throws Exception {
        // Initialize the database
        fracionamientoRepository.saveAndFlush(fracionamiento);

        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();

        // Update the fracionamiento
        Fracionamiento updatedFracionamiento = fracionamientoRepository.findById(fracionamiento.getId()).get();
        // Disconnect from session so that the updates on updatedFracionamiento are not directly saved in db
        em.detach(updatedFracionamiento);
        updatedFracionamiento
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .totalHouses(UPDATED_TOTAL_HOUSES)
            .costByHouse(UPDATED_COST_BY_HOUSE)
            .status(UPDATED_STATUS)
            .contract(UPDATED_CONTRACT);

        restFracionamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFracionamiento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFracionamiento))
            )
            .andExpect(status().isOk());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
        Fracionamiento testFracionamiento = fracionamientoList.get(fracionamientoList.size() - 1);
        assertThat(testFracionamiento.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFracionamiento.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFracionamiento.getTotalHouses()).isEqualTo(UPDATED_TOTAL_HOUSES);
        assertThat(testFracionamiento.getCostByHouse()).isEqualTo(UPDATED_COST_BY_HOUSE);
        assertThat(testFracionamiento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFracionamiento.getContract()).isEqualTo(UPDATED_CONTRACT);
    }

    @Test
    @Transactional
    void putNonExistingFracionamiento() throws Exception {
        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();
        fracionamiento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFracionamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fracionamiento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFracionamiento() throws Exception {
        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();
        fracionamiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFracionamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFracionamiento() throws Exception {
        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();
        fracionamiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFracionamientoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fracionamiento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFracionamientoWithPatch() throws Exception {
        // Initialize the database
        fracionamientoRepository.saveAndFlush(fracionamiento);

        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();

        // Update the fracionamiento using partial update
        Fracionamiento partialUpdatedFracionamiento = new Fracionamiento();
        partialUpdatedFracionamiento.setId(fracionamiento.getId());

        partialUpdatedFracionamiento.status(UPDATED_STATUS);

        restFracionamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFracionamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFracionamiento))
            )
            .andExpect(status().isOk());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
        Fracionamiento testFracionamiento = fracionamientoList.get(fracionamientoList.size() - 1);
        assertThat(testFracionamiento.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFracionamiento.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFracionamiento.getTotalHouses()).isEqualTo(DEFAULT_TOTAL_HOUSES);
        assertThat(testFracionamiento.getCostByHouse()).isEqualByComparingTo(DEFAULT_COST_BY_HOUSE);
        assertThat(testFracionamiento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFracionamiento.getContract()).isEqualTo(DEFAULT_CONTRACT);
    }

    @Test
    @Transactional
    void fullUpdateFracionamientoWithPatch() throws Exception {
        // Initialize the database
        fracionamientoRepository.saveAndFlush(fracionamiento);

        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();

        // Update the fracionamiento using partial update
        Fracionamiento partialUpdatedFracionamiento = new Fracionamiento();
        partialUpdatedFracionamiento.setId(fracionamiento.getId());

        partialUpdatedFracionamiento
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .totalHouses(UPDATED_TOTAL_HOUSES)
            .costByHouse(UPDATED_COST_BY_HOUSE)
            .status(UPDATED_STATUS)
            .contract(UPDATED_CONTRACT);

        restFracionamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFracionamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFracionamiento))
            )
            .andExpect(status().isOk());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
        Fracionamiento testFracionamiento = fracionamientoList.get(fracionamientoList.size() - 1);
        assertThat(testFracionamiento.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFracionamiento.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFracionamiento.getTotalHouses()).isEqualTo(UPDATED_TOTAL_HOUSES);
        assertThat(testFracionamiento.getCostByHouse()).isEqualByComparingTo(UPDATED_COST_BY_HOUSE);
        assertThat(testFracionamiento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFracionamiento.getContract()).isEqualTo(UPDATED_CONTRACT);
    }

    @Test
    @Transactional
    void patchNonExistingFracionamiento() throws Exception {
        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();
        fracionamiento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFracionamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fracionamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFracionamiento() throws Exception {
        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();
        fracionamiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFracionamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFracionamiento() throws Exception {
        int databaseSizeBeforeUpdate = fracionamientoRepository.findAll().size();
        fracionamiento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFracionamientoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fracionamiento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fracionamiento in the database
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFracionamiento() throws Exception {
        // Initialize the database
        fracionamientoRepository.saveAndFlush(fracionamiento);

        int databaseSizeBeforeDelete = fracionamientoRepository.findAll().size();

        // Delete the fracionamiento
        restFracionamientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, fracionamiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fracionamiento> fracionamientoList = fracionamientoRepository.findAll();
        assertThat(fracionamientoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
