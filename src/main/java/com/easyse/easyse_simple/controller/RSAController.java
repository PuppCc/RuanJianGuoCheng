package com.easyse.easyse_simple.controller;

import com.easyse.easyse_simple.utils.encryption.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.easyse.easyse_simple.utils.encryption.RSAUtil.*;

/**
 * @author: zky
 * @date: 2022/12/19
 * @description:
 */
@RestController
@RequestMapping("/rsa")
@Api(tags = "公钥")
public class RSAController {

    /**
     * 公钥
     */
    public String publicKey = "";

    /**
     * 私钥
     */
    public String prvateKey = "";

    @GetMapping("public")
    @ApiOperation(value = "获得公钥")
    public String getPublic(){
        Map<String, String> keys = createKeys(512);
        publicKey = keys.get(PRIVATE_KEY);
        prvateKey = keys.get(PUBLIC_KEY);
        return publicKey;
    }
}
