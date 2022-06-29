package com.example.rabbitmq.app;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@RestController
@RequestMapping("/folha")
public class TesteController {
    ArrayList<JSONObject> list = new ArrayList<>();
    public TesteController(AmqpTemplate queueConsumer) {
        this.queueConsumer = queueConsumer;
    }

    private final AmqpTemplate queueConsumer;

    @GetMapping("/listar")
    public ArrayList<String> listar(){
        ArrayList<String> strings = new ArrayList<>();
        int count = 0;
        Message msg;
        while ((msg = queueConsumer.receive()) != null) {
            count++;
            System.out.println("MSG:"+ new String(msg.getBody()));
            list.add(new JSONObject(new String(msg.getBody())));
            strings.add(new String(msg.getBody()));
            //json.put(new JSONObject(new String(msg.getBody(), StandardCharsets.UTF_8)));
        }
        return strings;

    }
    @GetMapping("/total")
    public Double listarTotal(){
        double total= 0.0;
        for (JSONObject json: list) {
             total= total + json.getDouble("bruto");
        }
        return total;

    }
    @GetMapping("/media")
    public String listarTotalMedia(){
        double total= 0.0;

        for (JSONObject json: list) {

            total= total + json.getDouble("bruto");
        }
        double media = total / list.size();
        return "Folhas de pagamento: "+list.size() + " Total de salarios: "+total+" Media de salarios:"+media;

    }

}
