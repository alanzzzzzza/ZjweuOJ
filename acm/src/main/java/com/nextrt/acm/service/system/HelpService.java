package com.nextrt.acm.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.biz.system.HelpsBiz;
import com.nextrt.core.entity.common.Help;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HelpService {
    private final HelpsBiz helpsBiz;

    public HelpService(HelpsBiz helpsBiz) {
        this.helpsBiz = helpsBiz;
    }

    public int addHelp(Help help) {
        help.setAddTime(new Date());
        return helpsBiz.addHelp(help);
    }

    public int deleteHelpById(int id) {
        return helpsBiz.deleteHelpById(id);
    }

    public int updateHelp(Help announcement) {
        return helpsBiz.updateHelp(announcement);
    }

    public Help getHelp(int id) {
        return helpsBiz.getHelp(id);
    }

    public IPage<Help> getHelps(int page, int size) {
        return helpsBiz.getHelps(page, size);
    }

}
