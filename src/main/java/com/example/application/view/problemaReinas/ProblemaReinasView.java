package com.example.application.view.problemaReinas;

import com.example.application.controller.problemaReinas.ProblemaReinasController;
import com.example.application.model.PartidaReinas;
import com.example.application.model.problemaReinas.PiezaReinas;
import com.example.application.repository.PartidaReinasRepository;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Problema de las Reinas")
@Route(value = "reinas")
public class ProblemaReinasView extends VerticalLayout {

    private final PartidaReinasRepository partidaReinasRepository;
    private ProblemaReinasController controller;
    private final IntegerField tamañoField;
    private final Div tableroContainer;
    private final List<Div> casillas;
    private final Button guardarBtn;
    private final Div contadorReinas;

    @Autowired
    public ProblemaReinasView(PartidaReinasRepository partidaReinasRepository) {
        this.partidaReinasRepository = partidaReinasRepository;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        // Botones de navegación
        Button volverInicioBtn = new Button("Volver al Inicio", e -> {
            UI.getCurrent().navigate("");
        });

        Button verHistorialBtn = new Button("Ver Historial", e -> {
            UI.getCurrent().navigate("historial-reinas");
        });

        HorizontalLayout navegacionLayout = new HorizontalLayout(volverInicioBtn, verHistorialBtn);
        navegacionLayout.setSpacing(true);

        // Componentes principales
        H1 titulo = new H1("Problema de las N Reinas");

        tamañoField = new IntegerField("Tamaño del tablero (N)");
        tamañoField.setValue(8);
        tamañoField.setMin(4);
        tamañoField.setMax(12);

        Button iniciarBtn = new Button("Crear Tablero", e -> iniciarJuego());
        guardarBtn = new Button("Guardar Partida", e -> guardarPartida());
        guardarBtn.setEnabled(false);

        Button resolverAutomaticoBtn = new Button("Resolver Automático", e -> mostrarDialogoResolucion());

        contadorReinas = new Div();
        contadorReinas.getStyle()
                .set("font-weight", "bold")
                .set("margin", "10px");

        tableroContainer = new Div();
        tableroContainer.setWidth("600px");
        tableroContainer.setHeight("600px");
        tableroContainer.getStyle()
                .set("margin", "auto")
                .set("display", "grid");

        casillas = new ArrayList<>();

        add(
                navegacionLayout,
                titulo,
                new HorizontalLayout(tamañoField, iniciarBtn, guardarBtn, resolverAutomaticoBtn),
                contadorReinas,
                tableroContainer
        );
    }

    private void iniciarJuego() {
        try {
            int tamaño = tamañoField.getValue();
            controller = new ProblemaReinasController(tamaño);
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
                    if (controller != null && controller.esSeguro(currentFila, currentCol)) {
                        PiezaReinas reina = new PiezaReinas(currentFila, currentCol);
                        if (controller.agregarReina(reina)) {
                            actualizarTableroVisual();
                            guardarBtn.setEnabled(true);
                            actualizarContador();
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
        int tamaño = controller.getTamaño();
        List<PiezaReinas> reinas = controller.getSolucion();

        for (int i = 0; i < casillas.size(); i++) {
            Div casilla = casillas.get(i);
            casilla.removeAll();

            int fila = i / tamaño;
            int col = i % tamaño;
            String color = (fila + col) % 2 == 0 ? "white" : "#f0f0f0";
            casilla.getStyle().set("background-color", color);
        }

        for (PiezaReinas reina : reinas) {
            int index = reina.getFila() * tamaño + reina.getColumna();
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

    private void actualizarContador() {
        if (controller != null) {
            contadorReinas.setText("Reinas colocadas: " + controller.getSolucion().size() +
                    "/" + controller.getTamaño());
        }
    }

    private void guardarPartida() {
        if (controller == null || controller.getSolucion().isEmpty()) {
            Notification.show("No hay reinas colocadas para guardar", 3000, Position.MIDDLE);
            return;
        }

        boolean estaResuelto = controller.solucionCompleta();
        Dialog dialogo = new Dialog();
        VerticalLayout layout = new VerticalLayout();

        H3 pregunta = new H3(estaResuelto ?
                "¡Solución completa! Guardar como solución finalizada" :
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

        // Estilo para botones deshabilitados
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
        PartidaReinas partida = new PartidaReinas();
        partida.setN(controller.getTamaño());
        partida.setResuelto(resuelto);
        partida.setIntentos(controller.getSolucion().size()); // Usamos el número de reinas colocadas como "intentos"
        partidaReinasRepository.save(partida);

        String mensaje = resuelto
                ? "Partida guardada como RESUELTA"
                : "Progreso guardado (" + partida.getIntentos() + " reinas colocadas)";
        Notification.show(mensaje, 3000, Position.MIDDLE);
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
            boolean exito = controller.resolverDesdePosicion(filaField.getValue(), columnaField.getValue());
            actualizarTableroVisual();
            guardarBtn.setEnabled(true);
            actualizarContador();

            Notification.show(exito ?
                    "Solución encontrada." :
                    "No se encontró solución con esa posición.", 3000, Position.MIDDLE);

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
}