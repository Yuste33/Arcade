package com.example.application.base.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Arcade de Puzzles")
@Route(value = "")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        // Configuración básica del layout
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getStyle()
                .set("background", "linear-gradient(to bottom, #1a1a2e, #16213e)")
                .set("color", "#fff")
                .set("padding", "20px");

        // Título con estilo arcade
        H1 title = new H1("ARCADE DE PUZZLES");
        title.getStyle()
                .set("font-family", "'Press Start 2P', cursive")
                .set("color", "#ff5722")
                .set("text-shadow", "4px 4px 0 #000")
                .set("margin-bottom", "40px");

        // Logo o imagen decorativa - RUTA CORREGIDA
        Image logo = new Image("images/arcade-logo.png", "Arcade Logo");
        logo.setHeight("150px");
        logo.getStyle().set("margin-bottom", "30px");

        // Contenedor de juegos con estilo arcade
        Div gamesContainer = new Div();
        gamesContainer.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(3, 1fr)")
                .set("gap", "20px")
                .set("max-width", "800px")
                .set("margin", "0 auto");


        Button reinasBtn = createGameButton("N-REINAS", "images/queen-icon.png", "reinas");
        Button caballosBtn = createGameButton("CABALLO", "images/knight-icon.png", "caballos");
        Button hanoiBtn = createGameButton("HANÓI", "images/hanoi-icon.png", "hanoi");

        gamesContainer.add(reinasBtn, caballosBtn, hanoiBtn);

        // Efecto de neón decorativo
        Div neonEffect = new Div();
        neonEffect.getStyle()
                .set("position", "absolute")
                .set("bottom", "0")
                .set("left", "0")
                .set("right", "0")
                .set("height", "10px")
                .set("background", "linear-gradient(90deg, #ff00ff, #00ffff, #ffff00, #ff00ff)")
                .set("background-size", "400% 400%")
                .set("animation", "neonBorder 8s linear infinite");

        // Añadir componentes al layout
        add(title, logo, gamesContainer, neonEffect);

        // Estilos CSS globales
        UI.getCurrent().getPage().addStyleSheet("https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap");
        UI.getCurrent().getPage().addStyleSheet("styles/main-view.css");
    }

    private Button createGameButton(String title, String iconPath, String route) {
        Div content = new Div();

        // Icono del juego
        Image icon = new Image(iconPath, title);
        icon.setHeight("80px");
        icon.getStyle().set("margin-bottom", "10px");

        // Texto del botón
        Div text = new Div();
        text.setText(title);
        text.getStyle()
                .set("font-family", "'Press Start 2P', cursive")
                .set("font-size", "14px")
                .set("color", "#fff");

        content.add(icon, text);
        content.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center");

        Button button = new Button(content);
        button.getStyle()
                .set("background", "linear-gradient(to bottom, #4a148c, #311b92)")
                .set("border", "3px solid #7c4dff")
                .set("border-radius", "10px")
                .set("padding", "20px")
                .set("cursor", "pointer")
                .set("transition", "all 0.3s")
                .set("width", "100%")
                .set("height", "100%");

        button.addClickListener(e -> {
            button.getStyle().set("transform", "scale(0.95)");
            UI.getCurrent().navigate(route);
        });

        button.addFocusListener(e -> button.getStyle().set("box-shadow", "0 0 15px #7c4dff"));
        button.addBlurListener(e -> button.getStyle().remove("box-shadow"));

        return button;
    }
}