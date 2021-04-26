package com.dxesoft.hestiatest.repository;

import com.dxesoft.hestiatest.domain.Fracionamiento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fracionamiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FracionamientoRepository extends JpaRepository<Fracionamiento, Long> {}
