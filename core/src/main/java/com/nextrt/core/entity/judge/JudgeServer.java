package com.nextrt.core.entity.judge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/23
 * info:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("judge_server")
public class JudgeServer {
    @TableId(type = IdType.AUTO)
    private int id;
    private int status = 1;
    private Date lastSend;
    private String sn;
    private String sysInfo;
    private Long memory;
    private Long memoryFree;
    private int cpuCoreNum;
    private double cpu;
    private double mem;
    private String judgeVersion;
    private Integer dealNum;
    @Version
    private Integer version = 0;
}
