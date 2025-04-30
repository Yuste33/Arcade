package com.example.application.view.problemaHanoi;

import com.example.application.model.PartidaHanoi;
import com.example.application.repository.PartidaCaballosRepository;
import com.example.application.repository.PartidaHanoiRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Historial Hanoi")
@Route(value = "historial-hanoi")
public class HistorialHanoiView extends VerticalLayout {

    public HistorialHanoiView(@Autowired PartidaHanoiRepository repository) {
        Button volverBtn = new Button("Volver al Juego", e -> {
            getUI().ifPresent(ui -> ui.navigate("hanoi"));
        });

        Button inicioBtn = new Button("Volver al Inicio", e -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        HorizontalLayout botonesNavegacion = new HorizontalLayout(volverBtn, inicioBtn);
        botonesNavegacion.setSpacing(true);

        Grid<PartidaHanoi> grid = new Grid<>(PartidaHanoi.class, false);
        grid.addColumn(PartidaHanoi::getNumDiscos).setHeader("Discos");
        grid.addColumn(p -> p.isResuelto() ? "Sí" : "No").setHeader("Resuelto");
        grid.addColumn(PartidaHanoi::getMovimientos).setHeader("Movimientos");
        grid.setItems(repository.findAll());

        add(
                botonesNavegacion,  // Usamos el layout de botones en lugar de solo el botón volver
                new H1("Historial de Torres de Hanói"),
                grid
        );

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
    }
}