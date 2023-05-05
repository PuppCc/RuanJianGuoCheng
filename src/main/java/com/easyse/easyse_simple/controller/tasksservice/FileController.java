package com.easyse.easyse_simple.controller.tasksservice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author ：rc
 * @date ：Created in 2022/10/24 12:54
 * @description：文件上传
 */
@RestController
@RequestMapping(value = "/tasks/files")
public class FileController {
    //TODO 文件上传模块
    @PostMapping(value={"/uploadFile"})
    public String uploadFile(MultipartFile file, String type, HttpServletResponse response) throws Exception{

        return null;
        }
}

