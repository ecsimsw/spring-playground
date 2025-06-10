package com.ecsimsw.springsdkexternalplatform.service;

import com.ecsimsw.springsdkexternalplatform.config.Envs;
import com.ecsimsw.springsdkexternalplatform.domain.PlatformProducts;
import com.ecsimsw.springsdkexternalplatform.dto.*;
import com.ecsimsw.springsdkexternalplatform.util.HMACSHA256Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TuyaDeviceApiService {

    private final WebClient webClient;

    private TokenResponse getToken() {
        var timeStamp = Instant.now().truncatedTo(ChronoUnit.MICROS).toEpochMilli();
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(Envs.endpoint)
                .port(443)
                .path("/v1.0/token")
                .queryParam("grant_type", "1")
                .build()
            )
            .header("client_id", Envs.clientId)
            .header("sign", calcSign(Envs.clientId, Envs.clientKey, timeStamp))
            .header("t", String.valueOf(timeStamp))
            .header("sign_method", "HMAC-SHA256")
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();
    }

    public String getUserIdByUsername(String username) {
        var token = getToken();
        var timeStamp = Instant.now().truncatedTo(ChronoUnit.MICROS).toEpochMilli();
        var response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(Envs.endpoint)
                .port(443)
                .path("/v1.0/apps/goqual/users")
                .queryParam("page_size", "40")
                .queryParam("page_no", "1")
                .queryParam("username", username)
                .build()
            )
            .header("client_id", Envs.clientId)
            .header("access_token", token.result().accessToken())
            .header("sign", calcSign(Envs.clientId, token.result().accessToken(), Envs.clientKey, timeStamp))
            .header("t", String.valueOf(timeStamp))
            .header("sign_method", "HMAC-SHA256")
            .retrieve()
            .bodyToMono(UserListResponse.class)
            .block();
        if (response.getResult().getList().isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return response.getResult().getList().get(0).getUid();
    }

    public List<DeviceInfo> getDeviceListByUserId(String uid) {
        var token = getToken();
        var timeStamp = Instant.now().truncatedTo(ChronoUnit.MICROS).toEpochMilli();
        var response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(Envs.endpoint)
                .port(443)
                .path("/v1.0/users/" + uid + "/devices")
                .build()
            )
            .header("client_id", Envs.clientId)
            .header("access_token", token.result().accessToken())
            .header("sign", calcSign(Envs.clientId, token.result().accessToken(), Envs.clientKey, timeStamp))
            .header("t", String.valueOf(timeStamp))
            .header("sign_method", "HMAC-SHA256")
            .retrieve()
            .bodyToMono(DevicesResponse.class)
            .block();
        return response.getResult().stream()
            .filter(res -> PlatformProducts.isSupported(res.getPid()))
            .peek(res -> {
                var product = PlatformProducts.getById(res.getPid());
                var parsedStatus = res.getStatus().stream()
                    .map(deviceStatus -> new DeviceStatus(
                        product.asSpCode(deviceStatus.getCode()), deviceStatus.getValue())
                    ).toList();
                res.setStatus(parsedStatus);
            }).toList();
    }

    public void command(String deviceId, String productId, List<DeviceStatus> deviceStatuses) {
        var product = PlatformProducts.getById(productId);
        var commands = deviceStatuses.stream()
            .map(deviceStatus -> new Command(product.asPlatformCode(deviceStatus.getCode()), deviceStatus.getValue()))
            .toList();
        var token = getToken();
        var timeStamp = Instant.now().truncatedTo(ChronoUnit.MICROS).toEpochMilli();
        webClient.post()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(Envs.endpoint)
                .port(443)
                .path("/v1.0/devices/" + deviceId + "/commands")
                .build()
            )
            .header("client_id", Envs.clientId)
            .header("access_token", token.result().accessToken())
            .header("sign", calcSign(Envs.clientId, token.result().accessToken(), Envs.clientKey, timeStamp))
            .header("t", String.valueOf(timeStamp))
            .header("sign_method", "HMAC-SHA256")
            .bodyValue(new CommandRequest(commands))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    private String calcSign(String clientId, String secret, long timeStamp) {
        return HMACSHA256Utils.encrypt(clientId + timeStamp, secret).toUpperCase();
    }

    private String calcSign(String clientId, String accessToken, String secret, long timeStamp) {
        return HMACSHA256Utils.encrypt(clientId + accessToken + timeStamp, secret).toUpperCase();
    }
}
