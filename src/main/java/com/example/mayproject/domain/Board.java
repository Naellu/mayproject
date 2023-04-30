package com.example.mayproject.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class Board {
    private Integer id;
    private String title;
    private String body;
    private LocalDateTime inserted;
    private String writer;
}
