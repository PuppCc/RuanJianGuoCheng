package com.easyse.easyse_simple.pojo.DO.DTO;

import com.easyse.easyse_simple.pojo.DO.example.SceneDesign;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author: zky
 * @date: 2022/12/09
 * @description: 接收技术分享和多文件
 */

@Data
public class MultiFileSD {
    /**
     * 接收的文件
     */
    MultipartFile[] files;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long groupId;
    private String year;
    private String sceneId;
    private String techShare;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
