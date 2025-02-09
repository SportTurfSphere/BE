package com.turf.common.util.client;

import com.turf.common.dto.GenericResponse;
import com.turf.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.turf.common.constants.ResultInfoConstants.STATUS_FAILED;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Log4j2
public class WebClientUtil {

    private static final String POST_MESSAGE = "SENT Webclient POST REQ :{}{}";
    private static final String PUT_MESSAGE = "SENT Webclient PUT REQ :{}{}";
    private static final String PATCH_MESSAGE = "SENT Webclient PATCH REQ :{}{}";
    private static final String GET_MESSAGE = "SENT Webclient GET REQ :{}{}";
    private static final String DELETE_MESSAGE = "SENT Webclient DELETE REQ :{}{}";

    private static final String REC_POST_MESSAGE = "REC  Webclient POST RES :{}{}";
    private static final String REC_PUT_MESSAGE = "REC  Webclient PUT RES :{}{}";

    private static final String REC_PATCH_MESSAGE = "REC  Webclient PATCH RES :{}{}";
    private static final String REC_GET_MESSAGE = "REC  Webclient GET RES :{}{}";
    private static final String REC_DELETE_MESSAGE = "REC  Webclient DELETE RES :{}{}";

    @Qualifier("webClient")
    private final WebClient webClient;

    /**
     * Start building an HTTP POST request.
     *
     * @return a block for specifying the target URL
     * @parameter: baseUrl
     * ,endpoint
     * ,responseType
     * ,headers
     * ,pathVariable
     * ,params
     */

    public Object post(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                       Map<String, String> headers, Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(POST_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.post().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_POST_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);

    }

    public Object post(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                       Map<String, String> headers, MultiValueMap<String, String> params) {
        log.info(POST_MESSAGE, baseUrl, endpoint);
        MediaType expectedContentType = MediaType.APPLICATION_JSON;
        val genericResponse = (GenericResponse) webClient.post().uri(baseUrl.concat(endpoint),
                        uriBuilder -> params == null ? uriBuilder.build() : uriBuilder.queryParams(params).build())
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                    httpHeaders.setAccept(Collections.singletonList(expectedContentType));
                })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_POST_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object post(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                       Map<String, String> headers, Map<String, ?> pathVariables) {
        log.info(POST_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.post().uri(baseUrl.concat(endpoint),
                        uriBuilder -> pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables))
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_POST_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object post(String baseUrl, String endpoint, Object requestBody, Class<?> responseType) {
        log.info(POST_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.post().uri(baseUrl.concat(endpoint))
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_POST_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }


    /**
     * Start building an HTTP PATCH request.
     *
     * @return a block for specifying the target URL
     * @parameter: baseUrl
     * ,endpoint
     * ,responseType
     * ,headers
     * ,pathVariable
     * ,params
     */

    public Object patch(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                        Map<String, String> headers, Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(PATCH_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.patch().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_PATCH_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object patch(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                        Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(PATCH_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.patch().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_PATCH_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object patch(String baseUrl, String endpoint, Class<?> responseType, Map<String, String> headers,
                        Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(PATCH_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.patch().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_PATCH_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);

    }


    /**
     * Start building an HTTP PUT request.
     *
     * @return a block for specifying the target URL
     * @parameter: baseUrl
     * ,endpoint
     * ,responseType
     * ,headers
     * ,pathVariable
     * ,params
     */

    public Object put(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                      Map<String, ?> pathVariables, Map<String, String> headers, MultiValueMap<String, String> params) {
        log.info(PUT_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.put().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_PUT_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object put(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                      Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(PUT_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.put().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_PUT_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object put(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                      Map<String, String> headers) {
        log.info(PUT_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.put().uri(baseUrl.concat(endpoint))
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .bodyValue(requestBody)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_PUT_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }


    /**
     * Start building an HTTP GET request.
     *
     * @return a block for specifying the target URL
     * @parameter: baseUrl
     * ,endpoint
     * ,responseType
     * ,headers
     * ,pathVariable
     * ,params
     */

    public Object get(String baseUrl, String endpoint, Class<?> responseType, Map<String, String> headers,
                      Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(GET_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.get().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_GET_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object get(String baseUrl, String endpoint, Class<?> responseType, Map<String, ?> pathVariables,
                      MultiValueMap<String, String> params) {
        log.info(GET_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.get().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_GET_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object get(String baseUrl, String endpoint, Class<?> responseType, Map<String, String> headers) {
        log.info(GET_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.get().uri(baseUrl.concat(endpoint))
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_GET_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object get(String baseUrl, String endpoint, Class<?> responseType) {
        log.info(GET_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.get().uri(baseUrl.concat(endpoint))
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_GET_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public byte[] download(String baseUrl, String endpoint, Map<String, String> headers,
                           Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(GET_MESSAGE, baseUrl, endpoint);
        return webClient.get().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                }).retrieve().bodyToMono(byte[].class).block();
    }

    /**
     * Start building an HTTP DELETE request.
     *
     * @return a block for specifying the target URL
     * @parameter: baseUrl
     * ,endpoint
     * ,responseType
     * ,pathVariables
     * ,headers
     * ,params
     */

    public Object delete(String baseUrl, String endpoint, Class<?> responseType, Map<String, String> headers,
                         Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(DELETE_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.delete().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_DELETE_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object delete(String baseUrl, String endpoint, Class<?> responseType, Map<String, ?> pathVariables,
                         MultiValueMap<String, String> params) {
        log.info(DELETE_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.delete().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_DELETE_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    public Object delete(String baseUrl, String endpoint, Class<?> responseType, Map<String, String> headers) {
        log.info(DELETE_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.delete().uri(baseUrl.concat(endpoint))
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_DELETE_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }


    /**
     * Start building an HTTP POST request with MULTIPART file.
     *
     * @return a block for specifying the target URL
     * @parameter: baseUrl
     * ,endpoint
     * ,responseType
     * ,pathVariables
     * ,headers
     * ,params
     * ,file
     */
    public Object post(String baseUrl, String endpoint, Class<?> responseType, Map<String, String> headers,
                       MultiValueMap<String, String> params, Map<String, String> pathVariables,
                       MultipartFile file) {
        log.info(POST_MESSAGE + " contains Multipart request",
                baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.post().uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .body(BodyInserters.fromMultipartData(file.getName(), file.getResource()))
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_POST_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }

    //Webclient delete method with bodyValue support in request
    public Object delete(String baseUrl, String endpoint, Object requestBody, Class<?> responseType,
                         Map<String, String> headers, Map<String, ?> pathVariables, MultiValueMap<String, String> params) {
        log.info(DELETE_MESSAGE, baseUrl, endpoint);
        val genericResponse = (GenericResponse) webClient.method(HttpMethod.DELETE).uri(baseUrl.concat(endpoint),
                        uriBuilder -> {
                            if (params != null)
                                uriBuilder.queryParams(params);
                            return pathVariables == null ? uriBuilder.build() : uriBuilder.build(pathVariables);
                        })
                .headers(httpHeaders -> {
                    if (headers != null)
                        headers.forEach(httpHeaders::add);
                })
                .bodyValue(requestBody)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeToMono(response -> response.bodyToMono(responseType)).block();
        assert genericResponse != null;
        log.info(REC_DELETE_MESSAGE, baseUrl, endpoint);
        return Optional.of(genericResponse)
                .filter(r -> r.getResultInfo().getStatus().equals(STATUS_FAILED))
                .map(response -> {
                    throw new ValidationException(response.getResultInfo());
                })
                .orElse(genericResponse);
    }
}
