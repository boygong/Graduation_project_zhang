package com.tokenauthentication.testLogin;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.captcha20230305.AsyncClient;
import com.aliyun.sdk.service.captcha20230305.models.VerifyCaptchaRequest;
import com.aliyun.sdk.service.captcha20230305.models.VerifyCaptchaResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.requset.IAcsTokenRequest;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;


@Slf4j
public class TestAfsCheck {

//    public int testLogin(IAcsTokenRequest iAcsTokenRequest) throws Exception{
//        //YOUR ACCESS_KEY、YOUR ACCESS_SECRET请替换成您的阿里云accesskey id和secret
//        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", "LTAI5tPAp9gPXzL1sgUhQh4Z", "nddTjq77eL1pwxKJ5dIT5j2ZT0PYJP");
//        IAcsClient client = new DefaultAcsClient(profile);
//        DefaultProfile.addEndpoint("cn-beijing","blog-zhangzx","oss-cn-beijing.aliyuncs.com");
//
//        AuthenticateSigRequest request = new AuthenticateSigRequest();
//        request.setSessionId(iAcsTokenRequest.getSessionId());// 必填参数，从前端获取，不可更改，android和ios只传这个参数即可
//        request.setSig(iAcsTokenRequest.getSig());// 必填参数，从前端获取，不可更改
//        request.setToken(iAcsTokenRequest.getAliToken());// 必填参数，从前端获取，不可更改
//        request.setScene(iAcsTokenRequest.getScene());// 必填参数，从前端获取，不可更改
//        request.setAppKey("个人隐私");// 必填参数，后端填写
//        request.setRemoteIp("1");// 必填参数，后端填写
//
//        try {
//            AuthenticateSigResponse response = client.getAcsResponse(request);
//            if(response.getCode() == 100) {
//                return 1;
//            } else {
//               return 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public int testLogin(String captchaVerifyParam) throws Exception {
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                    .accessKeyId("LTAI5tPAp9gPXzL1sgUhQh4Z")
                    .accessKeySecret("nddTjq77eL1pwxKJ5dIT5j2ZT0PYJP")
                    .build());

            AsyncClient client = AsyncClient.builder()
                    .credentialsProvider(provider)
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("captcha.cn-shanghai.aliyuncs.com")
                    )
                    .build();
            VerifyCaptchaRequest verifyCaptchaRequest = VerifyCaptchaRequest.builder()
                    .captchaVerifyParam(captchaVerifyParam)
                    .build();
            CompletableFuture<VerifyCaptchaResponse> response = client.verifyCaptcha(verifyCaptchaRequest);
            VerifyCaptchaResponse resp = response.get();
            log.info(new Gson().toJson(resp));
            client.close();
            if (resp.getBody().getSuccess()){
                return 1;
            }else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return 0;
    }
}