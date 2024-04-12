package com.artemnizhnyk.dataanalysergrpcmicroservice.service;

import com.artemnizhnyk.dataanalysergrpcmicroservice.model.Data;
import com.artemnizhnyk.dataanalysergrpcmicroservice.repository.DataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataServiceImpl implements DataService {

    private final DataRepository dataRepository;

    @Override
    public void handle(Data data) {
        log.info("Data object {} was saved", data);
        dataRepository.save(data);
    }

    @Transactional
    @Override
    public List<Data> getWithBatch(long batchSize) {
        List<Data> data = dataRepository.findAllWithOffset(batchSize);
        if (!data.isEmpty()) {
            dataRepository.incrementOffset(Long.min(batchSize, data.size()));
        }
        return data;
    }

}