package com.nextrt.core.entity.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 19-12-31
 * info: 文件类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("attachment")
@Builder
public class Attachment {
    @TableId(type = IdType.AUTO)
    private Integer fileId;//文件编号
    private Integer userId = 0;//操作用户
    private String fileName;//保存文件名
    private String fileOldName;//原文件名
    private Long fileSize;//文件大小
    private Date fileUploadTime = new Date();//文件上传时间
    private String fileUploadIp;//文件上传Ip
    private String fileType;//文件类型
    private Integer isDelete = 0;//文件是否删除
    private String fileSavePath;//文件保存目录
    private String fileMd5Code;//文件MD5

    public Attachment(Integer userId, String fileName, String fileOldName, Long fileSize, String fileUploadIp, String fileType,String fileMd5Code) {
        this.userId = userId;
        this.fileName = fileName;
        this.fileOldName = fileOldName;
        this.fileSize = fileSize;
        this.fileUploadIp = fileUploadIp;
        this.fileType = fileType;
        this.fileMd5Code = fileMd5Code;
    }
}
