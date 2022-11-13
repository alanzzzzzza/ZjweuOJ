package com.nextrt.core.mapper.contest;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nextrt.core.entity.contest.ContestProblemResult;
import com.nextrt.core.vo.contest.ContestRankVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 轩辕子墨
 * @Project: nextoj
 * @Date: 2020/3/1
 * @Description:
 */
@Repository
public interface ContestProblemResultMapper extends BaseMapper<ContestProblemResult> {
}
