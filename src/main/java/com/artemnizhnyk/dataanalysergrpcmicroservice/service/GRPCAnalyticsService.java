package com.artemnizhnyk.dataanalysergrpcmicroservice.service;

import com.artemnizhnyk.grpccommon.MeasurementType;
import com.artemnizhnyk.dataanalysergrpcmicroservice.model.Data;
import com.artemnizhnyk.grpccommon.AnalyticsServerGrpc;
import com.artemnizhnyk.grpccommon.GRPCAnalyticsRequest;
import com.artemnizhnyk.grpccommon.GRPCData;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@GrpcService
public class GRPCAnalyticsService extends AnalyticsServerGrpc.AnalyticsServerImplBase {

    private final DataService dataService;

    @Override
    public void askForData(final GRPCAnalyticsRequest request, final StreamObserver<GRPCData> responseObserver) {
        List<Data> data = dataService.getWithBatch(request.getBatchSize());
        for(Data d: data) {
            GRPCData dataRequest = GRPCData.newBuilder()
                    .setSensorId(d.getSensorId())
                    .setTimestamp(
                            Timestamp.newBuilder()
                                    .setSeconds(d.getTimestamp()
                                            .toEpochSecond(ZoneOffset.UTC))
                                    .build()
                    )
                    .setMeasurementType(
                            MeasurementType.valueOf(d.getMeasurementType().name())
                    )
                    .setMeasurement(d.getMeasurement())
                    .build();
            responseObserver.onNext(dataRequest);
        }
        log.info("Batch was sent");
        responseObserver.onCompleted();
    }
}
