package com.example.video.api.controller;


import com.example.video.api.dto.common.ResponseMsg;
import com.example.video.api.utill.Common;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @ApiOperation(value = "main test", notes = "테스트")
    @GetMapping("/test/{testNum}")
    public ResponseEntity<ResponseMsg> test(@PathVariable(value = "testNum") String testNum){
        ResponseMsg responseMsg = ResponseMsg.builder()
                .retStatus(true)
                .retCode(Common.StatusCode.RETURN_SUCCESS)
                .retHttpCode(HttpStatus.ACCEPTED.value())
                .retHttpStatus(HttpStatus.ACCEPTED)
                .retData("[tast data is " + testNum + "]")
                .build();

        return ResponseEntity.ok().body(responseMsg);
    }
}
