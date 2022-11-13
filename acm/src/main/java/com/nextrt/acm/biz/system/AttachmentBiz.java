package com.nextrt.acm.biz.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nextrt.core.entity.common.Attachment;
import com.nextrt.core.mapper.system.AttachmentMapper;
import org.springframework.stereotype.Component;


@Component
public class AttachmentBiz {
    private final AttachmentMapper attachmentMapper;

    public AttachmentBiz(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }

    public int addFile(Attachment attachment) {
        return attachmentMapper.insert(attachment);
    }

    public int deleteFile(int fileId) {
        return attachmentMapper.deleteById(fileId);
    }

    public Attachment getAttachmentById(int fileId) {
        return attachmentMapper.selectById(fileId);
    }

}
