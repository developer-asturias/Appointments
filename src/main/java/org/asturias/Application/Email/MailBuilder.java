package org.asturias.Application.Email;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

import java.time.LocalDateTime;


@AllArgsConstructor
@Service
public class MailBuilder {


    private final TemplateEngine templateEngine;


    public String buildTemplate(LocalDateTime date, String name){
        Context context = new Context();
        context.setVariable("date", date);
        context.setVariable("name", name);
        return templateEngine.process("templateEmail.html", context);

    }

}
