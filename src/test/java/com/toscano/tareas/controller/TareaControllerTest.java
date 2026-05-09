package com.toscano.tareas.controller;

import com.toscano.tareas.entity.Tarea;
import com.toscano.tareas.service.TareaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TareaController.class)
class TareaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TareaService service;

    @Test
    void get_tareaExiste_retorna200() throws Exception {
        Tarea t = new Tarea();
        t.setId(1L);
        t.setTitulo("Test");
        when(service.buscarPorId(1L)).thenReturn(t);

        mockMvc.perform(get("/api/tareas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Test"));
    }

    @Test
    void get_noExiste_retorna404() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new EntityNotFoundException("no encontrada"));

        mockMvc.perform(get("/api/tareas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_conTareaValida_retorna201() throws Exception {
        Tarea t = new Tarea();
        t.setId(1L);
        t.setTitulo("Nueva tarea");
        when(service.crear(any(Tarea.class))).thenReturn(t);

        mockMvc.perform(post("/api/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Nueva tarea\",\"descripcion\":\"Desc\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Nueva tarea"));
    }

    @Test
    void post_conTituloVacio_retorna400() throws Exception {
        when(service.crear(any(Tarea.class)))
                .thenThrow(new IllegalArgumentException("El título no puede estar vacío"));

        mockMvc.perform(post("/api/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\" \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patch_completar_retorna200() throws Exception {
        Tarea t = new Tarea();
        t.setId(1L);
        t.setTitulo("Tarea");
        t.setCompletada(true);
        when(service.completar(1L)).thenReturn(t);

        mockMvc.perform(patch("/api/tareas/1/completar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completada").value(true));
    }
}
