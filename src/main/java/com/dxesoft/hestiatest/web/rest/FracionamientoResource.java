package com.dxesoft.hestiatest.web.rest;

import com.dxesoft.hestiatest.domain.Fracionamiento;
import com.dxesoft.hestiatest.repository.FracionamientoRepository;
import com.dxesoft.hestiatest.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dxesoft.hestiatest.domain.Fracionamiento}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FracionamientoResource {

    private final Logger log = LoggerFactory.getLogger(FracionamientoResource.class);

    private static final String ENTITY_NAME = "fracionamiento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FracionamientoRepository fracionamientoRepository;

    public FracionamientoResource(FracionamientoRepository fracionamientoRepository) {
        this.fracionamientoRepository = fracionamientoRepository;
    }

    /**
     * {@code POST  /fracionamientos} : Create a new fracionamiento.
     *
     * @param fracionamiento the fracionamiento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fracionamiento, or with status {@code 400 (Bad Request)} if the fracionamiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fracionamientos")
    public ResponseEntity<Fracionamiento> createFracionamiento(@Valid @RequestBody Fracionamiento fracionamiento)
        throws URISyntaxException {
        log.debug("REST request to save Fracionamiento : {}", fracionamiento);
        if (fracionamiento.getId() != null) {
            throw new BadRequestAlertException("A new fracionamiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fracionamiento result = fracionamientoRepository.save(fracionamiento);
        return ResponseEntity
            .created(new URI("/api/fracionamientos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fracionamientos/:id} : Updates an existing fracionamiento.
     *
     * @param id the id of the fracionamiento to save.
     * @param fracionamiento the fracionamiento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fracionamiento,
     * or with status {@code 400 (Bad Request)} if the fracionamiento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fracionamiento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fracionamientos/{id}")
    public ResponseEntity<Fracionamiento> updateFracionamiento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Fracionamiento fracionamiento
    ) throws URISyntaxException {
        log.debug("REST request to update Fracionamiento : {}, {}", id, fracionamiento);
        if (fracionamiento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fracionamiento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fracionamientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fracionamiento result = fracionamientoRepository.save(fracionamiento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fracionamiento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fracionamientos/:id} : Partial updates given fields of an existing fracionamiento, field will ignore if it is null
     *
     * @param id the id of the fracionamiento to save.
     * @param fracionamiento the fracionamiento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fracionamiento,
     * or with status {@code 400 (Bad Request)} if the fracionamiento is not valid,
     * or with status {@code 404 (Not Found)} if the fracionamiento is not found,
     * or with status {@code 500 (Internal Server Error)} if the fracionamiento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fracionamientos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Fracionamiento> partialUpdateFracionamiento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Fracionamiento fracionamiento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fracionamiento partially : {}, {}", id, fracionamiento);
        if (fracionamiento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fracionamiento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fracionamientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fracionamiento> result = fracionamientoRepository
            .findById(fracionamiento.getId())
            .map(
                existingFracionamiento -> {
                    if (fracionamiento.getName() != null) {
                        existingFracionamiento.setName(fracionamiento.getName());
                    }
                    if (fracionamiento.getStartDate() != null) {
                        existingFracionamiento.setStartDate(fracionamiento.getStartDate());
                    }
                    if (fracionamiento.getTotalHouses() != null) {
                        existingFracionamiento.setTotalHouses(fracionamiento.getTotalHouses());
                    }
                    if (fracionamiento.getCostByHouse() != null) {
                        existingFracionamiento.setCostByHouse(fracionamiento.getCostByHouse());
                    }
                    if (fracionamiento.getStatus() != null) {
                        existingFracionamiento.setStatus(fracionamiento.getStatus());
                    }
                    if (fracionamiento.getContract() != null) {
                        existingFracionamiento.setContract(fracionamiento.getContract());
                    }

                    return existingFracionamiento;
                }
            )
            .map(fracionamientoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fracionamiento.getId().toString())
        );
    }

    /**
     * {@code GET  /fracionamientos} : get all the fracionamientos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fracionamientos in body.
     */
    @GetMapping("/fracionamientos")
    public List<Fracionamiento> getAllFracionamientos() {
        log.debug("REST request to get all Fracionamientos");
        return fracionamientoRepository.findAll();
    }

    /**
     * {@code GET  /fracionamientos/:id} : get the "id" fracionamiento.
     *
     * @param id the id of the fracionamiento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fracionamiento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fracionamientos/{id}")
    public ResponseEntity<Fracionamiento> getFracionamiento(@PathVariable Long id) {
        log.debug("REST request to get Fracionamiento : {}", id);
        Optional<Fracionamiento> fracionamiento = fracionamientoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fracionamiento);
    }

    /**
     * {@code DELETE  /fracionamientos/:id} : delete the "id" fracionamiento.
     *
     * @param id the id of the fracionamiento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fracionamientos/{id}")
    public ResponseEntity<Void> deleteFracionamiento(@PathVariable Long id) {
        log.debug("REST request to delete Fracionamiento : {}", id);
        fracionamientoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
