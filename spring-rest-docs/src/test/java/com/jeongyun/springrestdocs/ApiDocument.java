package com.jeongyun.springrestdocs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@EnableMockMvc
@AutoConfigureRestDocs
public class ApiDocument {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected RestDocumentationResultHandler toDocument(String title) { // (1) build/generated-snippets/{title} 폴더 하위에 문서 작성
        return document(title, getDocumentRequest(), getDocumentResponse());
    }

    protected String toJson(Object object) {    // (2) object를 JSON 형태로 변환
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("직렬화 오류");
        }
    }

    private OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(   // (3) 문서상 uri를 기본값인 http://localhost:8080에서 https://{domain} 으로 변경
                modifyUris()
                        .scheme("http")
                        .host("domain")
                        .removePort(),
                prettyPrint()); // (4) 문서의 request를 예쁘게 출력
    }

    private OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());   // (5) 문서의 response를 예쁘게 출력
    }
}
