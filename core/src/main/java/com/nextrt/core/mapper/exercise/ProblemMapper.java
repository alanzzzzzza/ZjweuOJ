package com.nextrt.core.mapper.exercise;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.vo.contest.ProblemVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemMapper extends BaseMapper<Problem> {
    @Select({"<script>",
            "select id,title,source,difficulty,accepted_number,submission_number,min(sub_status) as sub_status from( " +
                    "select p.id,p.title,p.source,p.accepted_number,p.submission_number,p.difficulty,coalesce(s.status,-2) as sub_status from problem as p " +
                    "left join ( select status,user_id,problem_id from submission where contest_id=0 order by status asc ) s on (s.user_id = #{userId} and s.problem_id = p.id and s.status > -1) " +
                    "where p.id > 0 and p.status =1 and p.is_public =1" +
                    "<when test='difficulty > 0'> and p.difficulty = #{difficulty} </when>",
                    "<when test='name != null'> and p.title like CONCAT('%',#{name},'%') </when>",
                    "order by p.id,s.status ) x group by x.id",
            "</script>"})
    IPage<ProblemVO> userGetProblemsList(Page<?> page, int userId, int difficulty, String name);

    @Select("select max(id) from problem")
    int userGetLatestProblemId();
}
