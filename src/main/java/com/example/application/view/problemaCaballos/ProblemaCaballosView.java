package com.example.application.view.problemaCaballos;

import com.example.application.controller.problemaCaballos.ProblemaCaballosController;
import com.example.application.model.PartidaCaballos;
import com.example.application.model.problemaCaballos.PiezaCaballo;
import com.example.application.repository.PartidaCaballosRepository;
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
import com.vaadin.flow.router.RouterLink;

import java.util.List;

@PageTitle("Recorrido del Caballo")
@Route(value = "caballos")
public class ProblemaCaballosView extends VerticalLayout {

    private final PartidaCaballosRepository partidaCaballosRepository;
    private ProblemaCaballosController controller;
    private final IntegerField tamañoField;
    private final Div tableroContainer;
    private final Button guardarBtn;
    private final Div contadorMovimientos;

    public ProblemaCaballosView(PartidaCaballosRepository partidaCaballosRepository) {
        this.partidaCaballosRepository = partidaCaballosRepository;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        // Botones de navegación
        Button volverInicioBtn = new Button("Volver al Inicio", e -> {
            UI.getCurrent().navigate("");
        });

        Button verHistorialBtn = new Button("Ver Historial", e -> {
            UI.getCurrent().navigate("historial-caballos");
        });

        HorizontalLayout navegacionLayout = new HorizontalLayout(volverInicioBtn, verHistorialBtn);
        navegacionLayout.setSpacing(true);

        // Componentes UI principales
        H1 titulo = new H1("Problema del Recorrido del Caballo");
        tamañoField = new IntegerField("Tamaño del tablero");
        tamañoField.setValue(8);
        tamañoField.setMin(5);
        tamañoField.setMax(10);

        Button iniciarBtn = new Button("Crear Tablero", e -> iniciarJuego());
        Button resolverBtn = new Button("Resolver Automático", e -> resolverAutomatico());
        guardarBtn = new Button("Guardar Partida", e -> guardarPartida());
        guardarBtn.setEnabled(false);

        contadorMovimientos = new Div();
        contadorMovimientos.getStyle()
                .set("font-weight", "bold")
                .set("margin", "10px");

        tableroContainer = new Div();
        tableroContainer.setWidth("600px");
        tableroContainer.setHeight("600px");
        tableroContainer.getStyle()
                .set("margin", "auto")
                .set("display", "grid");

        add(
                navegacionLayout, // Añadimos los botones de navegación primero
                titulo,
                new HorizontalLayout(tamañoField, iniciarBtn, resolverBtn, guardarBtn),
                contadorMovimientos,
                tableroContainer
        );
    }

    // ... (el resto de los métodos permanecen igual)
    private void iniciarJuego() {
        try {
            int tamaño = tamañoField.getValue();
            controller = new ProblemaCaballosController(tamaño);
            crearTableroVisual(tamaño);
            guardarBtn.setEnabled(false);
            actualizarContador();
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
                        guardarBtn.setEnabled(true);
                        actualizarContador();
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

    private void actualizarContador() {
        if (controller != null) {
            contadorMovimientos.setText("Movimientos: " + controller.getMovimientos().size());
        }
    }

    private void guardarPartida() {
        if (controller == null || controller.getMovimientos().isEmpty()) {
            Notification.show("No hay movimientos para guardar", 3000, Position.MIDDLE);
            return;
        }

        boolean estaResuelto = controller.solucionCompleta();
        Dialog dialogo = new Dialog();
        VerticalLayout layout = new VerticalLayout();

        H3 pregunta = new H3(estaResuelto ?
                "¡Recorrido completo! Guardar como solución finalizada" :
                "Guardar progreso actual");

        Button btnResuelta = new Button("Guardar como resuelta", e -> {
            guardarEnBD(true);
            dialogo.close();
        });
        btnResuelta.setEnabled(estaResuelto);

        Button btnProgreso = new Button("Guardar progreso", e -> {
            guardarEnBD(false);
            dialogo.close();
        });
        btnProgreso.setEnabled(!estaResuelto);

        String disabledStyle = "color: var(--lumo-disabled-text-color); " +
                "background-color: var(--lumo-contrast-5pct); " +
                "cursor: not-allowed;";

        if (!estaResuelto) {
            btnResuelta.getElement().getStyle().set("color", "var(--lumo-disabled-text-color)");
            btnResuelta.getElement().getStyle().set("background-color", "var(--lumo-contrast-5pct)");
            btnResuelta.getElement().getStyle().set("cursor", "not-allowed");
        }

        if (estaResuelto) {
            btnProgreso.getElement().getStyle().set("color", "var(--lumo-disabled-text-color)");
            btnProgreso.getElement().getStyle().set("background-color", "var(--lumo-contrast-5pct)");
            btnProgreso.getElement().getStyle().set("cursor", "not-allowed");
        }

        layout.add(
                pregunta,
                new HorizontalLayout(btnResuelta, btnProgreso)
        );
        layout.setAlignItems(Alignment.CENTER);
        dialogo.add(layout);
        dialogo.open();
    }

    private void guardarEnBD(boolean resuelto) {
        PartidaCaballos partida = new PartidaCaballos();
        partida.setTamañoTablero(controller.getTamaño());
        partida.setResuelto(resuelto);
        partida.setMovimientos(controller.getMovimientos().size());
        partidaCaballosRepository.save(partida);

        String mensaje = resuelto
                ? "Partida guardada como RESUELTA"
                : "Progreso guardado (" + partida.getMovimientos() + " movimientos)";
        Notification.show(mensaje, 3000, Position.MIDDLE);
    }

    private void resolverAutomatico() {
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
            int fila = filaField.getValue();
            int columna = columnaField.getValue();

            if (controller.resolverAutomatico(fila, columna)) {
                actualizarTableroVisual();
                dialogo.close();
                Notification.show("Solución encontrada!", 3000, Position.MIDDLE);

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