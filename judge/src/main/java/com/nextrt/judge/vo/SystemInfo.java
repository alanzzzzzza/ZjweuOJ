package com.nextrt.judge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by XYZM
 * Project: examination
 * User: xyzm
 * Date: 2020/2/24
 * info:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemInfo {
    private String sn;
    private String sysInfo;
    private Long memory;
    private Long memoryFree;
    private int cpuCoreNum;
    private double cpu;
    private double mem;
    private String judgeVersion;
    private Integer dealNum;
}
