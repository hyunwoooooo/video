package com.example.video.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
@Configuration
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void whenReady() {
        System.out.println("\n" +
                " ██╗   ██╗██╗██████╗ ███████╗ ██████╗     ████████╗███████╗███████╗████████╗ \n" +
                " ██║   ██║██║██╔══██╗██╔════╝██╔═══██╗    ╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝ \n" +
                " ██║   ██║██║██║  ██║█████╗  ██║   ██║       ██║   █████╗  ███████╗   ██║    \n" +
                " ╚██╗ ██╔╝██║██║  ██║██╔══╝  ██║   ██║       ██║   ██╔══╝  ╚════██║   ██║    \n" +
                "  ╚████╔╝ ██║██████╔╝███████╗╚██████╔╝       ██║   ███████╗███████║   ██║    \n" +
                "   ╚═══╝  ╚═╝╚═════╝ ╚══════╝ ╚═════╝        ╚═╝   ╚══════╝╚══════╝   ╚═╝    \n");
    }
}
