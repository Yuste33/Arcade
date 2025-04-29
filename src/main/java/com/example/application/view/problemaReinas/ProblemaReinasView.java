package com.example.application.view.problemaReinas;

import com.example.application.controller.problemaReinas.ProblemaReinasController;
import com.example.application.model.problemaReinas.PiezaReinas;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Problema de las Reinas")
@Route(value = "reinas")
public class ProblemaReinasView extends VerticalLayout {

    private ProblemaReinasController controller;
    private final IntegerField tamañoField;
    private final Div tableroContainer;
    private final List<Div> casillas;
    private final Button comprobarBtn;

    public ProblemaReinasView() {
        // Configuración básica
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        // Componentes UI
        H1 titulo = new H1("Problema de las N Reinas");

        tamañoField = new IntegerField("Tamaño del tablero (N)");
        tamañoField.setValue(8);
        tamañoField.setMin(4);
        tamañoField.setMax(12);

        Button iniciarBtn = new Button("Crear Tablero");
        iniciarBtn.addClickListener(e -> iniciarJuego());

        comprobarBtn = new Button("Comprobar Solución");
        comprobarBtn.addClickListener(e -> comprobarSolucion());
        comprobarBtn.setEnabled(false);

        tableroContainer = new Div();
        tableroContainer.setWidth("600px");
        tableroContainer.setHeight("600px");
        tableroContainer.getStyle()
                .set("margin", "auto")
                .set("display", "grid");

        casillas = new ArrayList<>();

        // Añadir componentes al layout
        add(
                titulo,
                new HorizontalLayout(tamañoField, iniciarBtn, comprobarBtn),
                tableroContainer
        );
    }

    private void iniciarJuego() {
        try {
            int tamaño = tamañoField.getValue();
            controller = new ProblemaReinasController(tamaño);
            crearTableroVisual(tamaño);
            comprobarBtn.setEnabled(true);
            Notification.show("Tablero de " + tamaño + "x" + tamaño + " creado", 3000, Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al crear tablero: " + e.getMessage(), 3000, Position.MIDDLE);
        }
    }

    private void crearTableroVisual(int tamaño) {
        tableroContainer.removeAll();
        casillas.clear();

        // Configurar grid CSS
        tableroContainer.getStyle()
                .set("grid-template-columns", "repeat(" + tamaño + ", 1fr)")
                .set("grid-template-rows", "repeat(" + tamaño + ", 1fr)");

        for (int fila = 0; fila < tamaño; fila++) {
            for (int col = 0; col < tamaño; col++) {
                final int currentFila = fila;
                final int currentCol = col;

                Div casilla = new Div();
                casilla.setWidthFull();
                casilla.setHeightFull();
                casilla.getStyle()
                        .set("border", "1px solid black")
                        .set("display", "flex")
                        .set("align-items", "center")
                        .set("justify-content", "center")
                        .set("cursor", "pointer")
                        .set("background-color", (fila + col) % 2 == 0 ? "white" : "#f0f0f0");

                casilla.addClickListener(e -> {
                    if (controller.esSeguro(currentFila, currentCol)) {
                        PiezaReinas reina = new PiezaReinas(currentFila, currentCol);
                        if (controller.agregarReina(reina)) {
                            actualizarTableroVisual();
                            Notification.show("Reina colocada en (" + (currentFila+1) + "," + (currentCol+1) + ")",
                                    2000, Position.BOTTOM_START);
                        }
                    } else {
                        Notification.show("¡Posición no válida! La reina está amenazada.",
                                3000, Position.MIDDLE);
                    }
                });

                casillas.add(casilla);
                tableroContainer.add(casilla);
            }
        }
    }

    private void actualizarTableroVisual() {
        List<PiezaReinas> reinas = controller.getSolucion();

        // Limpiar todas las casillas
        for (Div casilla : casillas) {
            casilla.removeAll();
            casilla.getStyle().set("background-color",
                    (casillas.indexOf(casilla) / controller.getTamaño() +
                            casillas.indexOf(casilla) % controller.getTamaño()) % 2 == 0
                            ? "white" : "#f0f0f0");
        }

        // Colocar las reinas
        for (PiezaReinas reina : reinas) {
            int index = reina.getFila() * controller.getTamaño() + reina.getColumna();
            Div casilla = casillas.get(index);

            Div reinaVisual = new Div();
            reinaVisual.setText("♛"); // Símbolo de reina
            reinaVisual.getStyle()
                    .set("font-size", "24px")
                    .set("color", "red");

            casilla.add(reinaVisual);
            casilla.getStyle().set("background-color", "#ffdddd");
        }
    }

    private void comprobarSolucion() {
        if (controller.solucionCompleta()) {
            Notification.show("¡Correcto! Has colocado todas las reinas sin conflictos.",
                    3000, Position.MIDDLE);
        } else {
            Notification.show("Aún no es una solución válida. Faltan reinas o hay conflictos.",
                    3000, Position.MIDDLE);
        }
    }
}