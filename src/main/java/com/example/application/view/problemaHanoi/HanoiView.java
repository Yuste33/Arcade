package com.example.application.view.problemaHanoi;

import com.example.application.controller.problemaHanoi.HanoiController;
import com.example.application.model.PartidaHanoi;
import com.example.application.model.problemaHanoi.Torre;
import com.example.application.model.problemaHanoi.Disco;
import com.example.application.repository.PartidaHanoiRepository;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@PageTitle("Torres de Hanói")
@Route(value = "hanoi")
public class HanoiView extends VerticalLayout {

    private HanoiController controller;
    private final IntegerField discosField;
    private final Map<Torre, VerticalLayout> torreVisualMap = new HashMap<>();
    private Torre torreSeleccionada = null;
    private final Button resolverBtn;
    private final Div estadoDiv;
    private final PartidaHanoiRepository partidaHanoiRepository;
    private final Button guardarBtn;
    private final Button historialBtn;
    private final Button volverInicioBtn; // Nuevo botón

    @Autowired
    public HanoiView(PartidaHanoiRepository partidaHanoiRepository) {
        this.partidaHanoiRepository = partidaHanoiRepository;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        H1 titulo = new H1("Torres de Hanói");

        // Configuración inicial
        discosField = new IntegerField("Número de discos (3-8)");
        discosField.setValue(3);
        discosField.setMin(3);
        discosField.setMax(8);

        Button iniciarBtn = new Button("Iniciar Juego", e -> iniciarJuego());
        Button reiniciarBtn = new Button("Reiniciar", e -> reiniciarJuego());
        resolverBtn = new Button("Resolver Automático", e -> mostrarDialogoResolucion());
        guardarBtn = new Button("Guardar Partida", e -> guardarPartida());
        guardarBtn.setEnabled(false);
        historialBtn = new Button("Ver Historial", e -> verHistorial());
        volverInicioBtn = new Button("Volver al Inicio", e -> volverAlInicio()); // Nuevo botón

        // Usando Div en lugar de Label
        estadoDiv = new Div();
        estadoDiv.setText("Seleccione una torre para comenzar");
        estadoDiv.getStyle()
                .set("font-weight", "bold")
                .set("margin", "10px 0");

        HorizontalLayout torresLayout = new HorizontalLayout();
        torresLayout.setWidthFull();
        torresLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        torresLayout.setSpacing(true);

        add(
                titulo,
                new HorizontalLayout(discosField, iniciarBtn, reiniciarBtn, resolverBtn, guardarBtn, historialBtn, volverInicioBtn),
                estadoDiv,
                torresLayout
        );
    }

    // Método para volver al inicio
    private void volverAlInicio() {
        UI.getCurrent().navigate(""); // Navega a la ruta raíz
    }

    private void guardarPartida() {
        try {
            if (controller == null || controller.getMovimientos() == 0) {
                Notification.show("Realice al menos un movimiento antes de guardar", 3000, Notification.Position.MIDDLE);
                return;
            }

            PartidaHanoi partida = new PartidaHanoi();
            partida.setNumDiscos(controller.getNumDiscos());
            partida.setResuelto(controller.juegoCompletado());
            partida.setMovimientos(controller.getMovimientos());

            partidaHanoiRepository.save(partida);

            Notification.show("Partida guardada correctamente", 3000, Notification.Position.MIDDLE);
            guardarBtn.setEnabled(false); // Opcional: deshabilitar después de guardar

        } catch (Exception e) {
            Notification.show("Error al guardar: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void verHistorial() {
        UI.getCurrent().navigate("historial-hanoi");
    }

    private void iniciarJuego() {
        int numDiscos = discosField.getValue();
        controller = new HanoiController(numDiscos);
        crearTorresVisuales();
        estadoDiv.setText("Seleccione la torre de origen"); // Actualizado
        Notification.show("Juego iniciado con " + numDiscos + " discos", 3000, Notification.Position.MIDDLE);
    }

    private void crearTorresVisuales() {
        HorizontalLayout torresLayout = (HorizontalLayout) getComponentAt(3);
        torresLayout.removeAll();
        torreVisualMap.clear();
        torreSeleccionada = null;

        for (Torre torre : new Torre[]{controller.getJuego().getTorreA(),
                controller.getJuego().getTorreB(),
                controller.getJuego().getTorreC()}) {
            VerticalLayout torreVisual = new VerticalLayout();
            torreVisual.setWidth("200px");
            torreVisual.setHeight("300px");
            torreVisual.getStyle()
                    .set("border", "2px solid #333")
                    .set("border-radius", "5px")
                    .set("background", "#f5f5f5")
                    .set("align-items", "center")
                    .set("justify-content", "flex-end")
                    .set("cursor", "pointer");

            torreVisual.addClickListener(e -> manejarClicTorre(torre));

            torreVisualMap.put(torre, torreVisual);
            torresLayout.add(torreVisual);
        }

        actualizarTorresVisuales();
    }

    private void manejarClicTorre(Torre torre) {
        if (torreSeleccionada == null) {
            if (!torre.estaVacia()) {
                torreSeleccionada = torre;
                resaltarTorre(torre, true);
                estadoDiv.setText("Seleccione la torre destino");
            }
        } else {
            resaltarTorre(torreSeleccionada, false);

            if (controller.moverDisco(torreSeleccionada, torre)) {
                actualizarTorresVisuales();
                estadoDiv.setText("Movimiento realizado (" + controller.getMovimientos() + "). Seleccione la torre de origen");
                guardarBtn.setEnabled(true); // Habilitar guardado

                if (controller.juegoCompletado()) {
                    estadoDiv.setText("¡Juego completado en " + controller.getMovimientos() + " movimientos!");
                    guardarBtn.setEnabled(true);
                }
            }
            torreSeleccionada = null;
        }
    }

    private void resaltarTorre(Torre torre, boolean resaltar) {
        VerticalLayout torreVisual = torreVisualMap.get(torre);
        if (resaltar) {
            torreVisual.getStyle().set("border", "3px solid #FF5722");
        } else {
            torreVisual.getStyle().set("border", "2px solid #333");
        }
    }

    private void actualizarTorresVisuales() {
        torreVisualMap.forEach((torre, layout) -> {
            layout.removeAll();

            Div base = new Div();
            base.setWidth("180px");
            base.setHeight("10px");
            base.getStyle()
                    .set("background", "#333")
                    .set("margin-top", "auto");
            layout.add(base);

            Div spacer = new Div();
            spacer.getStyle().set("flex-grow", "1");
            layout.add(spacer);

            for (int i = torre.getDiscos().size() - 1; i >= 0; i--) {
                Disco disco = torre.getDiscos().get(i);
                Div discoVisual = new Div();
                discoVisual.setWidth((disco.getTamaño() * 20) + "px");
                discoVisual.setHeight("20px");
                discoVisual.getStyle()
                        .set("background", disco.getColor())
                        .set("border", "1px solid #333")
                        .set("border-radius", "5px")
                        .set("margin", "2px auto");
                layout.add(discoVisual);
            }
        });
    }

    private void reiniciarJuego() {
        if (controller != null) {
            controller.reiniciar();
            actualizarTorresVisuales();
            estadoDiv.setText("Seleccione la torre de origen"); // Actualizado
            Notification.show("Juego reiniciado", 3000, Notification.Position.MIDDLE);
        } else {
            Notification.show("Primero inicia un juego", 3000, Notification.Position.MIDDLE);
        }
    }

    private void mostrarDialogoResolucion() {
        if (controller == null) {
            Notification.show("Primero inicia un juego", 3000, Notification.Position.MIDDLE);
            return;
        }

        Dialog dialogo = new Dialog();
        VerticalLayout layoutDialogo = new VerticalLayout();

        Button resolverBtn = new Button("Resolver", e -> {
            resolverAutomatico();
            dialogo.close();
        });

        Button cancelarBtn = new Button("Cancelar", e -> dialogo.close());

        layoutDialogo.add(
                new H3("¿Resolver automáticamente?"),
                new HorizontalLayout(resolverBtn, cancelarBtn)
        );
        layoutDialogo.setAlignItems(Alignment.CENTER);
        layoutDialogo.setSpacing(true);

        dialogo.add(layoutDialogo);
        dialogo.open();
    }

    private void resolverAutomatico() {
        controller.reiniciar();
        actualizarTorresVisuales();
        estadoDiv.setText("Resolviendo automáticamente..."); // Actualizado

        new Thread(() -> {
            try {
                controller.resolverAutomatico();

                for (String movimiento : controller.getHistorial()) {
                    Thread.sleep(500);

                    getUI().ifPresent(ui -> ui.access(() -> {
                        Notification.show(movimiento, 1000, Notification.Position.BOTTOM_START);
                        actualizarTorresVisuales();
                    }));
                }

                getUI().ifPresent(ui -> ui.access(() -> {
                    estadoDiv.setText("¡Resuelto en " + controller.getMovimientos() + " movimientos!");
                    Notification.show("¡Resuelto en " + controller.getMovimientos() + " movimientos!",
                            3000, Notification.Position.MIDDLE);
                }));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}