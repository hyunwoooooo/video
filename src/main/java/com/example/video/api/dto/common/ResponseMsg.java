package com.example.video.api.dto.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ResponseMsg {
    public static final String SUCCESS = "2000";
    public static final String ERROR = "9999";

    private boolean retStatus;
    private int retCode;
    private HttpStatus retHttpStatus;
    private int retHttpCode;
    private Object retData;
}
