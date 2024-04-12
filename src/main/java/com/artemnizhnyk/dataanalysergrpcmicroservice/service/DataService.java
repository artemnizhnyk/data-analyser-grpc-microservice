package com.artemnizhnyk.dataanalysergrpcmicroservice.service;

import com.artemnizhnyk.dataanalysergrpcmicroservice.model.Data;

import java.util.List;

public interface DataService {

    void handle(final Data data);

    List<Data> getWithBatch(final long batchSize);
}
