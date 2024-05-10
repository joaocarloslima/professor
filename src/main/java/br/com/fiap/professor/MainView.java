package br.com.fiap.professor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
class MainView extends VerticalLayout {

    @Autowired
    ChatService service;

    MainView() {

        var chatBar = new HorizontalLayout();
        chatBar.setWidthFull();

        var messagePanel = new MessageList();
        messagePanel.setHeightFull();
        messagePanel.setWidthFull();
        messagePanel.getStyle().set("background-color", "#f0f0f0");

        TextField textField = new TextField();
        textField.setPlaceholder("digite uma pergunta para o professor");
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.setPrefixComponent(VaadinIcon.CHAT.create());

        Button sendButton = new Button(new Icon(VaadinIcon.PAPERPLANE));
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.setAriaLabel("Send message");
        sendButton.addClickListener(event -> {
            MessageListItem userMessage = new MessageListItem(
                                                textField.getValue(),
                                                LocalDateTime.now().toInstant(ZoneOffset.UTC), "Aluno");

            var response = service.sendMessage(textField.getValue());
            MessageListItem gptMessage = new MessageListItem(
                                                response,
                                                LocalDateTime.now().toInstant(ZoneOffset.UTC), "Professor");

            messagePanel.setItems(List.of(userMessage, gptMessage));

            //messagePanel.add(new Paragraph(response));
        });

        chatBar.add(textField);
        chatBar.add(sendButton);

        setHeightFull();

        add(new H1("Chat de Ciências"));
        add(messagePanel);
        add(chatBar);
    }
}
