package com.palantir.fintech.controller;

import com.palantir.fintech.dto.ResponseDTO;
import com.palantir.fintech.dto.ResultObject;

public abstract class AbstractController {

    protected <T> ResponseDTO<T> ok() {
        return ok(null, ResultObject.getSuccess());
    }

    protected <T> ResponseDTO<T> ok(T data) {
        return ok(data, ResultObject.getSuccess());
    }

    protected <T> ResponseDTO<T> ok(T data, ResultObject result) {
        ResponseDTO<T> obj = new ResponseDTO<>();
        obj.setData(data);
        obj.setResult(result);

        return obj;
    }
}
