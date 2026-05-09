package com.toscano.tareas.repository;

import com.toscano.tareas.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByCompletada(boolean completada);
}
