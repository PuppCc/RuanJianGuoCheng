package com.easyse.easyse_simple.controller.oss;

import com.alibaba.fastjson.JSONObject;
import com.easyse.easyse_simple.service.FileUploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: zky2002
 * @Description: 文件上传controller
 */
@RequestMapping("api/file")
@RestController
@Api(tags = "技术问答板块图片上传")
public class OssFileController {
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传api
     * @param: file
     * @return: com.alibaba.fastjson.JSONObject
     * @author: zky2002
     */
    @PostMapping("upload")
    public JSONObject upload(@RequestParam("file") MultipartFile file) {
        // 作为返回JSON字符串
        JSONObject jsonObject = new JSONObject();
        if (file != null) {
            String returnFileUrl = fileUploadService.upload(file, false);
            if (returnFileUrl.equals("error")) {
                jsonObject.put("error", "文件上传失败！");
                return jsonObject;
            }
            jsonObject.put("success", "文件上传成功！");
            jsonObject.put("returnFileUrl", returnFileUrl);
            return jsonObject;
        } else {
            jsonObject.put("error", "文件上传失败！");
            return jsonObject;
        }
    }

    /**
     * 文件下载api
     * @param: fileName
     * @param: response
     * @return: com.alibaba.fastjson.JSONObject
     * @author: zky2002
     */
    @GetMapping(value = "download/{fileName}")
    public JSONObject download(@PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception {
        JSONObject jsonObject = new JSONObject();

        String status = fileUploadService.download(fileName, response);
        if (status.equals("error")) {
            jsonObject.put("error", "文件下载失败！");
            return jsonObject;
        } else {
            jsonObject.put("success", "文件下载成功！");
            return jsonObject;
        }
    }

    /**
     * 文件删除api
     * @param: fileName
     * @return: com.alibaba.fastjson.JSONObject
     * @author: zky2002
     */
    @GetMapping("/delete/{fileName}")
    public JSONObject deleteFile(@PathVariable("fileName") String fileName) {
        JSONObject jsonObject = new JSONObject();

        String status = fileUploadService.delete(fileName);
        if (status.equals("error")) {
            jsonObject.put("error", "文件删除失败！");
            return jsonObject;
        } else {
            jsonObject.put("success", "文件删除成功！");
            return jsonObject;
        }
    }
}
