package com.example.application.view.problemaReinas;

import com.example.application.controller.problemaReinas.ProblemaReinasController;
import com.example.application.model.problemaReinas.PiezaReinas;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
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
    private final Button resolverAutomaticoBtn;

    public ProblemaReinasView() {
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        H1 titulo = new H1("Problema de las N Reinas");

        tamañoField = new IntegerField("Tamaño del tablero (N)");
        tamañoField.setValue(8);
        tamañoField.setMin(4);
        tamañoField.setMax(12);

        Button iniciarBtn = new Button("Crear Tablero", e -> iniciarJuego());
        comprobarBtn = new Button("Comprobar Solución", e -> comprobarSolucion());
        comprobarBtn.setEnabled(false);

        resolverAutomaticoBtn = new Button("Resolver Automático", e -> mostrarDialogoResolucion());
        resolverAutomaticoBtn.setEnabled(false);

        tableroContainer = new Div();
        tableroContainer.setWidth("600px");
        tableroContainer.setHeight("600px");
        tableroContainer.getStyle()
                .set("margin", "auto")
                .set("display", "grid");

        casillas = new ArrayList<>();

        add(
                titulo,
                new HorizontalLayout(tamañoField, iniciarBtn, comprobarBtn, resolverAutomaticoBtn),
                tableroContainer
        );
    }

    private void iniciarJuego() {
        try {
            int tamaño = tamañoField.getValue();
            controller = new ProblemaReinasController(tamaño);
            crearTableroVisual(tamaño);
            comprobarBtn.setEnabled(true);
            resolverAutomaticoBtn.setEnabled(true);
            Notification.show("Tablero de " + tamaño + "x" + tamaño + " creado", 3000, Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al crear tablero: " + e.getMessage(), 3000, Position.MIDDLE);
        }
    }

    private void crearTableroVisual(int tamaño) {
        tableroContainer.removeAll();
        casillas.clear();

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

        for (Div casilla : casillas) {
            casilla.removeAll();
            casilla.getStyle().set("background-color",
                    (casillas.indexOf(casilla) / controller.getTamaño() +
                            casillas.indexOf(casilla) % controller.getTamaño()) % 2 == 0
                            ? "white" : "#f0f0f0");
        }

        for (PiezaReinas reina : reinas) {
            int index = reina.getFila() * controller.getTamaño() + reina.getColumna();
            Div casilla = casillas.get(index);

            Div reinaVisual = new Div();
            reinaVisual.setText("♛");
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

    private void mostrarDialogoResolucion() {
        if (controller == null) {
            Notification.show("Primero crea un tablero", 3000, Position.MIDDLE);
            return;
        }

        Dialog dialogo = new Dialog();
        VerticalLayout layoutDialogo = new VerticalLayout();

        IntegerField filaField = new IntegerField("Fila inicial (0-" + (controller.getTamaño()-1) + ")");
        filaField.setMin(0);
        filaField.setMax(controller.getTamaño()-1);
        filaField.setValue(0);

        IntegerField columnaField = new IntegerField("Columna inicial (0-" + (controller.getTamaño()-1) + ")");
        columnaField.setMin(0);
        columnaField.setMax(controller.getTamaño()-1);
        columnaField.setValue(0);

        Button resolverBtn = new Button("Resolver", e -> {
            resolverDesdePosicion(filaField.getValue(), columnaField.getValue());
            dialogo.close();
        });

        Button cancelarBtn = new Button("Cancelar", e -> dialogo.close());

        layoutDialogo.add(
                new H3("Resolver desde posición inicial"),
                filaField,
                columnaField,
                new HorizontalLayout(resolverBtn, cancelarBtn)
        );
        layoutDialogo.setAlignItems(Alignment.CENTER);
        layoutDialogo.setSpacing(true);

        dialogo.add(layoutDialogo);
        dialogo.open();
    }

    private void resolverDesdePosicion(int filaInicial, int columnaInicial) {
        if (controller.resolverDesdePosicion(filaInicial, columnaInicial)) {
            actualizarTableroVisual();
            Notification.show("Solución encontrada colocando la primera reina en (" +
                    filaInicial + "," + columnaInicial + ")", 3000, Position.MIDDLE);
        } else {
            Notification.show("No se encontró solución con la reina inicial en (" +
                    filaInicial + "," + columnaInicial + ")", 3000, Position.MIDDLE);
        }
    }
}