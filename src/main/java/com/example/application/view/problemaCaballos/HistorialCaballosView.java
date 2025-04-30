package com.example.application.view.problemaCaballos;

import com.example.application.model.PartidaCaballos;
import com.example.application.repository.PartidaCaballosRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Historial Caballos")
@Route(value = "historial-caballos")
public class HistorialCaballosView extends VerticalLayout {

    public HistorialCaballosView(@Autowired PartidaCaballosRepository repository) {
        Button volverBtn = new Button("Volver al Juego", e -> {
            getUI().ifPresent(ui -> ui.navigate("caballos"));
        });

        Button inicioBtn = new Button("Inicio", e -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        HorizontalLayout botonesNavegacion = new HorizontalLayout(volverBtn, inicioBtn);
        botonesNavegacion.setSpacing(true);

        Grid<PartidaCaballos> grid = new Grid<>(PartidaCaballos.class, false);
        grid.addColumn(PartidaCaballos::getTamañoTablero).setHeader("Tamaño");
        grid.addColumn(p -> p.isResuelto() ? "Sí" : "No").setHeader("Resuelto");
        grid.addColumn(PartidaCaballos::getMovimientos).setHeader("Movimientos");

        grid.setItems(repository.findAll());

        add(
                botonesNavegacion,
                new H1("Historial de Recorridos"),
                grid
        );
    }
}