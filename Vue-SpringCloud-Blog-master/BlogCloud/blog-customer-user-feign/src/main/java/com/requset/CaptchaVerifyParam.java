package com.requset;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
public class CaptchaVerifyParam {
    private String captchaVerifyParam;
}
