package com.example.application.view.problemaReinas;

import com.example.application.model.PartidaReinas;
import com.example.application.repository.PartidaReinasRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Historial de Partidas")
@Route(value = "historial-reinas")
public class HistorialReinasView extends VerticalLayout {

    private final PartidaReinasRepository partidaReinasRepository;
    private final Grid<PartidaReinas> grid;

    @Autowired
    public HistorialReinasView(PartidaReinasRepository partidaReinasRepository) {
        this.partidaReinasRepository = partidaReinasRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 titulo = new H1("Historial de Partidas del Problema de las Reinas");

        grid = new Grid<>(PartidaReinas.class, false);
        grid.addColumn(PartidaReinas::getN).setHeader("Tamaño del tablero");
        grid.addColumn(partida -> partida.isResuelto() ? "Sí" : "No").setHeader("Resuelto");
        grid.addColumn(PartidaReinas::getIntentos).setHeader("Intentos");

        cargarDatos();

        add(titulo, grid);
    }

    private void cargarDatos() {
        List<PartidaReinas> partidas = partidaReinasRepository.findAll();
        grid.setItems(partidas);
    }
}
