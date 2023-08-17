package com.example.indexer.web;

import com.example.indexer.service.IndexerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DataResource {

    private final IndexerService indexerService;

    public DataResource(IndexerService indexerService) {
        this.indexerService = indexerService;
    }

    @PostMapping("/index")
    public ResponseEntity index() {
        this.indexerService.index();
        return ResponseEntity.ok().build();
    }
}
