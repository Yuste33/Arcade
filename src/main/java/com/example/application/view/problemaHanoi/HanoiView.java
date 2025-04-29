package com.example.application.view.problemaHanoi;

import com.example.application.controller.problemaHanoi.HanoiController;
import com.example.application.model.problemaHanoi.Disco;
import com.example.application.model.problemaHanoi.Torre;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import java.util.HashMap;
import java.util.Map;

@PageTitle("Torres de Hanói")
@Route(value = "hanoi")
public class HanoiView extends VerticalLayout {

    private HanoiController controller;
    private final IntegerField discosField;
    private final Map<Torre, VerticalLayout> torreVisualMap = new HashMap<>();
    private final ComboBox<Torre> origenCombo;
    private final ComboBox<Torre> destinoCombo;

    public HanoiView() {
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
        Button resolverBtn = new Button("Resolver Automático", e -> mostrarDialogoResolucion());

        HorizontalLayout torresLayout = new HorizontalLayout();
        torresLayout.setWidthFull();
        torresLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        torresLayout.setSpacing(true);

        origenCombo = new ComboBox<>("Torre origen");
        destinoCombo = new ComboBox<>("Torre destino");
        Button moverBtn = new Button("Mover Disco", e -> moverDisco());

        HorizontalLayout controlesLayout = new HorizontalLayout(
                origenCombo,
                destinoCombo,
                moverBtn
        );
        controlesLayout.setAlignItems(Alignment.END);

        add(
                titulo,
                new HorizontalLayout(discosField, iniciarBtn, reiniciarBtn, resolverBtn),
                torresLayout,
                controlesLayout
        );
    }

    private void iniciarJuego() {
        int numDiscos = discosField.getValue();
        controller = new HanoiController(numDiscos);
        crearTorresVisuales();
        actualizarCombos();
        Notification.show("Juego iniciado con " + numDiscos + " discos", 3000, Position.MIDDLE);
    }

    private void crearTorresVisuales() {
        HorizontalLayout torresLayout = (HorizontalLayout) getComponentAt(2);
        torresLayout.removeAll();
        torreVisualMap.clear();

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
                    .set("justify-content", "flex-end");

            torreVisualMap.put(torre, torreVisual);
            torresLayout.add(torreVisual);
        }

        actualizarTorresVisuales();
    }


    private void actualizarTorresVisuales() {
        torreVisualMap.forEach((torre, layout) -> {
            layout.removeAll();

            // Base de la torre
            Div base = new Div();
            base.setWidth("180px");
            base.setHeight("10px");
            base.getStyle()
                    .set("background", "#333")
                    .set("margin-top", "auto");
            layout.add(base);

            // Espaciador para alinear los discos en la parte inferior
            Div spacer = new Div();
            spacer.getStyle().set("flex-grow", "1");
            layout.add(spacer);

            // Discos (en orden correcto)
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

    private void actualizarCombos() {
        // Configurar ComboBox con nombres personalizados
        origenCombo.setItems(
                controller.getJuego().getTorreA(),
                controller.getJuego().getTorreB(),
                controller.getJuego().getTorreC()
        );

        destinoCombo.setItems(
                controller.getJuego().getTorreA(),
                controller.getJuego().getTorreB(),
                controller.getJuego().getTorreC()
        );

        // Establecer cómo mostrar los nombres de las torres
        origenCombo.setItemLabelGenerator(torre -> {
            if (torre == controller.getJuego().getTorreA()) return "Torre 1";
            if (torre == controller.getJuego().getTorreB()) return "Torre 2";
            return "Torre 3";
        });

        destinoCombo.setItemLabelGenerator(torre -> {
            if (torre == controller.getJuego().getTorreA()) return "Torre 1";
            if (torre == controller.getJuego().getTorreB()) return "Torre 2";
            return "Torre 3";
        });
    }

    private void moverDisco() {
        Torre origen = origenCombo.getValue();
        Torre destino = destinoCombo.getValue();

        if (origen == null || destino == null) {
            Notification.show("Selecciona torre origen y destino", 3000, Position.MIDDLE);
            return;
        }

        if (controller.moverDisco(origen, destino)) {
            actualizarTorresVisuales();
            Notification.show("Movimiento realizado. Total: " + controller.getMovimientos(),
                    3000, Position.MIDDLE);

            if (controller.juegoCompletado()) {
                Notification.show("¡Felicidades! Has completado el juego en " +
                                controller.getMovimientos() + " movimientos.",
                        5000, Position.MIDDLE);
            }
        } else {
            Notification.show("Movimiento no válido", 3000, Position.MIDDLE);
        }
    }

    private void reiniciarJuego() {
        if (controller != null) {
            controller.reiniciar();
            actualizarTorresVisuales();
            Notification.show("Juego reiniciado", 3000, Position.MIDDLE);
        } else {
            Notification.show("Primero inicia un juego", 3000, Position.MIDDLE);
        }
    }

    private void mostrarDialogoResolucion() {
        if (controller == null) {
            Notification.show("Primero inicia un juego", 3000, Position.MIDDLE);
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

        new Thread(() -> {
            try {
                controller.resolverAutomatico();

                // Animación de los movimientos
                for (String movimiento : controller.getHistorial()) {
                    Thread.sleep(500); // Pausa entre movimientos

                    getUI().ifPresent(ui -> ui.access(() -> {
                        Notification.show(movimiento, 1000, Position.BOTTOM_START);
                        actualizarTorresVisuales();
                    }));
                }

                getUI().ifPresent(ui -> ui.access(() -> {
                    Notification.show("¡Resuelto en " + controller.getMovimientos() + " movimientos!",
                            3000, Position.MIDDLE);
                }));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}