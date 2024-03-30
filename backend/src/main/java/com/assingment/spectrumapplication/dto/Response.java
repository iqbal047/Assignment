package com.assingment.spectrumapplication.dto;

import com.assingment.spectrumapplication.constants.enums.OperationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<D> {
    private OperationStatus status;
    private String message;
    private D data;
}
