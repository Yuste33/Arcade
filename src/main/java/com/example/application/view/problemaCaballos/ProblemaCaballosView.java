package com.example.application.view.problemaCaballos;

import com.example.application.controller.problemaCaballos.ProblemaCaballosController;
import com.example.application.model.problemaCaballos.PiezaCaballo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
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
import java.util.Arrays;
import java.util.List;

@PageTitle("Recorrido del Caballo")
@Route(value = "caballos")
public class ProblemaCaballosView extends VerticalLayout {

    private ProblemaCaballosController controller;
    private final IntegerField tamañoField;
    private final Div tableroContainer;
    private final Button comprobarBtn;

    public ProblemaCaballosView() {
        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        H1 titulo = new H1("Problema del Recorrido del Caballo");

        tamañoField = new IntegerField("Tamaño del tablero");
        tamañoField.setValue(8);
        tamañoField.setMin(5);
        tamañoField.setMax(10);

        Button iniciarBtn = new Button("Crear Tablero", e -> iniciarJuego());
        comprobarBtn = new Button("Comprobar Solución", e -> comprobarSolucion());
        comprobarBtn.setEnabled(false);

        Button resolverBtn = new Button("Resolver Automático", e -> resolverAutomatico());

        tableroContainer = new Div();
        tableroContainer.setWidth("600px");
        tableroContainer.setHeight("600px");
        tableroContainer.getStyle()
                .set("margin", "auto")
                .set("display", "grid");

        add(
                titulo,
                new HorizontalLayout(tamañoField, iniciarBtn, comprobarBtn, resolverBtn),
                tableroContainer
        );
    }

    private void iniciarJuego() {
        try {
            int tamaño = tamañoField.getValue();
            controller = new ProblemaCaballosController(tamaño);
            crearTableroVisual(tamaño);
            comprobarBtn.setEnabled(true);
            Notification.show("Tablero de " + tamaño + "x" + tamaño + " creado", 3000, Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("Error al crear tablero: " + e.getMessage(), 3000, Position.MIDDLE);
        }
    }

    private void crearTableroVisual(int tamaño) {
        tableroContainer.removeAll();
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
                    if (controller.agregarMovimiento(currentFila, currentCol)) {
                        actualizarTableroVisual();
                        Notification.show("Movimiento " + controller.getMovimientos().size() + " agregado",
                                2000, Position.BOTTOM_START);
                    } else {
                        Notification.show("Movimiento no válido", 3000, Position.MIDDLE);
                    }
                });

                tableroContainer.add(casilla);
            }
        }
    }

    private void actualizarTableroVisual() {
        int[][] estado = controller.getEstadoTablero();
        int tamaño = controller.getTamaño();

        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                int index = i * tamaño + j;
                Div casilla = (Div) tableroContainer.getChildren().toArray()[index];
                casilla.removeAll();

                if (estado[i][j] != -1) {
                    Div numero = new Div();
                    numero.setText(String.valueOf(estado[i][j] + 1));
                    numero.getStyle()
                            .set("font-size", "18px")
                            .set("font-weight", "bold")
                            .set("color", "blue");
                    casilla.add(numero);
                    casilla.getStyle().set("background-color", "#d4edff");
                }
            }
        }
    }

    private void comprobarSolucion() {
        if (controller.solucionCompleta()) {
            Notification.show("¡Solución completa! Todos los movimientos son válidos.",
                    3000, Position.MIDDLE);
        } else {
            Notification.show("Aún no es una solución completa. Movimientos: " +
                    controller.getMovimientos().size(), 3000, Position.MIDDLE);
        }
    }


    private void resolverAutomatico() {
        if (controller == null) {
            Notification.show("Primero crea un tablero", 3000, Position.MIDDLE);
            return;
        }

        // Diálogo para seleccionar posición inicial
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
            int fila = filaField.getValue();
            int columna = columnaField.getValue();

            if (controller.resolverAutomatico(fila, columna)) {
                actualizarTableroVisual();
                dialogo.close();
                Notification.show("Solución encontrada!", 3000, Position.MIDDLE);

                // Animación del recorrido
                animarRecorrido();
            } else {
                Notification.show("No se pudo encontrar una solución completa", 3000, Position.MIDDLE);
            }
        });

        Button cancelarBtn = new Button("Cancelar", e -> dialogo.close());

        layoutDialogo.add(
                new H3("Selecciona la posición inicial"),
                filaField,
                columnaField,
                new HorizontalLayout(resolverBtn, cancelarBtn)
        );
        layoutDialogo.setAlignItems(Alignment.CENTER);
        layoutDialogo.setSpacing(true);

        dialogo.add(layoutDialogo);
        dialogo.open();
    }

    private void animarRecorrido() {
        List<PiezaCaballo> movimientos = controller.getMovimientos();
        int tamaño = controller.getTamaño();

        // Primero limpiar el tablero
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                int index = i * tamaño + j;
                Div casilla = (Div) tableroContainer.getChildren().toArray()[index];
                casilla.getStyle().set("background-color", (i + j) % 2 == 0 ? "white" : "#f0f0f0");
            }
        }

        // Animación paso a paso
        UI ui = UI.getCurrent();
        new Thread(() -> {
            for (PiezaCaballo movimiento : movimientos) {
                int finalIndex = movimiento.getFila() * tamaño + movimiento.getColumna();

                try {
                    Thread.sleep(300); // Velocidad de la animación
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                ui.access(() -> {
                    Div casilla = (Div) tableroContainer.getChildren().toArray()[finalIndex];
                    casilla.removeAll();

                    Div numero = new Div();
                    numero.setText(String.valueOf(movimiento.getNumeroMovimiento() + 1));
                    numero.getStyle()
                            .set("font-size", "18px")
                            .set("font-weight", "bold")
                            .set("color", "blue");

                    casilla.add(numero);
                    casilla.getStyle().set("background-color", "#a5d6a7"); // Verde claro

                    if (movimiento.getNumeroMovimiento() == movimientos.size() - 1) {
                        Notification.show("Recorrido completado!", 2000, Position.MIDDLE);
                    }
                });
            }
        }).start();
    }
}