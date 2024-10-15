package com.equipoC.alquilerQuinchos.repositorios;

import com.equipoC.alquilerQuinchos.entidades.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarioRepositorio extends JpaRepository<Calendario, String> {

    @Query("SELECT c FROM Calendario c WHERE c.id = :id ")
    public Calendario buscarCalendarioPorId(@Param("id") Long id);

}
