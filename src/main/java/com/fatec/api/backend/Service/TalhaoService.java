package com.fatec.api.backend.Service;

import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class TalhaoService {

    private final String uploadDir = "/path/to/upload/directory/";

    public String createTalhao(String talhao) throws IOException {
        return "ok";
    }
}