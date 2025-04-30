package com.example.application.view.problemaReinas;

import com.example.application.model.PartidaReinas;
import com.example.application.repository.PartidaReinasRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Historial Reinas")
@Route(value = "historial-reinas")
public class HistorialReinasView extends VerticalLayout {

    public HistorialReinasView(@Autowired PartidaReinasRepository repository) {
        // Botones de navegación (NUEVO)
        Button volverBtn = new Button("Volver al Juego", e -> {
            getUI().ifPresent(ui -> ui.navigate("reinas"));
        });

        Button inicioBtn = new Button("Inicio", e -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        HorizontalLayout botonesNavegacion = new HorizontalLayout(volverBtn, inicioBtn);
        botonesNavegacion.setSpacing(true);

        // Grid de historial
        Grid<PartidaReinas> grid = new Grid<>(PartidaReinas.class, false);
        grid.addColumn(PartidaReinas::getN).setHeader("Tamaño (N)");
        grid.addColumn(p -> p.isResuelto() ? "Sí" : "No").setHeader("Resuelto");
        grid.addColumn(PartidaReinas::getIntentos).setHeader("Reinas Colocadas");

        grid.setItems(repository.findAll());

        add(
                botonesNavegacion,
                new H1("Historial de Partidas - Problema de las Reinas"),
                grid
        );
    }
}