package com.example.indexer.service;

import com.example.indexer.model.MockAccount;
import java.io.BufferedReader;
import java.io.FileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexerService {

    private final ResourceLoader resourceLoader;
    private final KafkaDispatcher dispatcher;

    @Transactional
    public void index() {
        // fb, linkedin, twitter API stream usage would be here
        //as for the test example, we use mock data from MOCK_DATA.csv
        Resource resource = resourceLoader.getResource("classpath:MOCK_DATA.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile())); CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br)) {
            parser.stream()
                    .map(record ->
                            MockAccount.builder()
                                    .id(Long.valueOf(record.get("id")))
                                    .firstName(record.get("first_name"))
                                    .lastName(record.get("last_name")).build())
                    .forEach(dispatcher::send);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
