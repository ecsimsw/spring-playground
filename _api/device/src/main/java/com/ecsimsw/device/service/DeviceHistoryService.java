package com.ecsimsw.device.service;

import com.ecsimsw.common.dto.DeviceHistoryEvent;
import com.ecsimsw.device.domain.BindDeviceRepository;
import com.ecsimsw.device.domain.DeviceHistory;
import com.ecsimsw.device.domain.DeviceHistoryRepository;
import com.ecsimsw.device.dto.DeviceHistoryPageResponse;
import com.ecsimsw.device.dto.DeviceHistoryResponse;
import com.ecsimsw.device.dto.PageInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class DeviceHistoryService {

    private final DeviceHistoryRepository deviceHistoryRepository;
    private final BindDeviceRepository bindDeviceRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    // TODO :: Transaction?
    public void save(DeviceHistoryEvent event) {
        var deviceId = event.getDeviceId();
        var optBindDevice = bindDeviceRepository.findById(deviceId);
        if (optBindDevice.isEmpty()) {
            return;
        }
        var deviceHistory = new DeviceHistory(deviceId, event.getCode(), event.getValue(), event.getTimestamp());
        deviceHistoryRepository.save(deviceHistory).block();
    }

    // TODO :: Flux나 Mono를 어디까지 가져가야 할까
    // TODO :: MongoDB의 인덱스, 순서 명시
    public Mono<DeviceHistoryPageResponse> historiesByCursor(String deviceId, String historyCode, Sort sort, Object cursor, int size) {
        var order = sort.iterator().next();
        var property = order.getProperty();
        return mongoTemplate.find(
                cursoredPagination(deviceId, historyCode, cursor, sort, size + 1),
                DeviceHistory.class
            ).collectList()
            .flatMap(contents -> Mono.zip(
                Mono.just(contents.stream().map(DeviceHistoryResponse::of).toList()),
                mongoTemplate.count(totalQuery(deviceId, historyCode), DeviceHistory.class),
                mongoTemplate.exists(cursoredPagination(deviceId, historyCode, cursor, sort.reverse(), 1), DeviceHistory.class)
            ).map(monos -> new DeviceHistoryPageResponse(
                    monos.getT1(),
                    monos.getT2(),
                    new PageInfo(
                        objectMapper.convertValue(monos.getT1().getFirst().historyValue(), Map.class).get(property),
                        objectMapper.convertValue(monos.getT1().getLast().historyValue(), Map.class).get(property),
                        contents.size() > size,
                        monos.getT3()
                    )
                )
            ));
    }

    private Query cursoredPagination(String deviceId, String historyCode, Object cursor, Sort sort, int size) {
        var order = sort.iterator().next();
        var direction = order.getDirection();
        var columnName = "historyValue." + order.getProperty();
        var query = new Query();
        query.with(Sort.by(direction, columnName));
        query.limit(size);
        if (direction.isAscending()) {
            query.addCriteria(new Criteria().andOperator(
                Criteria.where("deviceId").is(deviceId),
                Criteria.where("historyCode").is(historyCode),
                Criteria.where(columnName).gt(cursor)
            ));
        }
        if (direction.isDescending()) {
            query.addCriteria(new Criteria().andOperator(
                Criteria.where("deviceId").is(deviceId),
                Criteria.where("historyCode").is(historyCode),
                Criteria.where(columnName).lt(cursor)
            ));
        }
        return query;
    }

    private Query totalQuery(String deviceId, String historyCode) {
        return new Query(new Criteria().andOperator(
            Criteria.where("deviceId").is(deviceId),
            Criteria.where("historyCode").is(historyCode)
        ));
    }
}
