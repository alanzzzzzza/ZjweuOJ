package com.nextrt.acm.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ZipUtil;
import com.nextrt.acm.biz.system.AttachmentBiz;
import com.nextrt.acm.config.SystemConfig;
import com.nextrt.acm.util.file.LocalFileUtil;
import com.nextrt.core.entity.common.Attachment;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class AttachmentService {
    private static final String BasicPath = "upload";//文件上传基础目录
    private static final String Delimiter = File.separator;//目录分隔符

    private final SystemConfig config;
    private final AttachmentBiz attachmentBiz;

    public AttachmentService(SystemConfig config, AttachmentBiz attachmentBiz) {
        this.config = config;
        this.attachmentBiz = attachmentBiz;
    }

    public Result addFile(MultipartFile file, String fileName, String fileType, String filePath, int userId, String ip) {
        String savePath = config.getString("filePath") + Delimiter + BasicPath + Delimiter + userId + Delimiter + filePath;
        Attachment attachment = Attachment.builder().userId(userId).fileOldName(file.getOriginalFilename()).fileSavePath(savePath).fileName(fileName).fileSize(file.getSize()).fileUploadIp(ip).fileType(fileType).fileMd5Code(LocalFileUtil.getMd5Code(file)).build();
        if (LocalFileUtil.saveFile(file, savePath) == 1 && attachmentBiz.addFile(attachment) > 0)
            return new Result(1, "文件上传成功", attachment);
        return new Result(-1, "文件上传失败!");

    }

    public int tempWriteString(int userId, String filePath, String data) {
        String savePath = config.getString("filePath") + Delimiter + BasicPath + Delimiter + userId + Delimiter + filePath;
        try {
            FileUtil.touch(savePath);
            FileWriter writer = new FileWriter(savePath);
            writer.write(data);
            writer.flush();
            writer.close();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String zip(String src, String path, int userId) {
        String basePath = config.getString("filePath") + Delimiter + BasicPath + Delimiter + userId + Delimiter;
        ZipUtil.zip(basePath + src, basePath + path);
        return basePath + path;
    }

    public int deleteFile(int fileId) {
        Attachment attachment = attachmentBiz.getAttachmentById(fileId);
        if (attachment == null) return 0;
        LocalFileUtil.deleteFile(attachment.getFileSavePath());
        return attachmentBiz.deleteFile(fileId);
    }

    public Result readFile(int fileId) {
        Attachment attachment = attachmentBiz.getAttachmentById(fileId);
        if (attachment == null) return new Result(-1, "文件不存在");
        FileReader fileReader = new FileReader(attachment.getFileSavePath());
        byte[] result = fileReader.readBytes();
        if (result != null)
            return new Result(1, "文件读取成功", result);
        return new Result(-2, "文件读取失败");
    }

    public int deleteFile(int fileId, int userId) {
        Attachment attachment = attachmentBiz.getAttachmentById(fileId);
        if (attachment == null || userId != attachment.getUserId()) return 0;
        LocalFileUtil.deleteFile(attachment.getFileSavePath());
        return attachmentBiz.deleteFile(fileId);
    }

    public Attachment getAttachmentById(int id) {
        return attachmentBiz.getAttachmentById(id);
    }
}
