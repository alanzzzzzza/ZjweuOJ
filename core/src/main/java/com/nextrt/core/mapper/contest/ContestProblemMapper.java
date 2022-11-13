package com.nextrt.core.mapper.contest;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nextrt.core.entity.contest.ContestProblem;
import com.nextrt.core.vo.contest.ProblemExcelVO;
import com.nextrt.core.vo.contest.ProblemVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/27
 * info:
 */
@Repository
public interface ContestProblemMapper extends BaseMapper<ContestProblem> {

    //@Select("select * from problem as a,contest_problems as b where b.contest_id = #{contestId} and a.id = b.problem_id")
    @Select("select a.*, b.*, coalesce(r.status,-2) as subStatus from problem as a, contest_problems as b left join contest_problem_results r on (r.contest_id = b.contest_id and b.problem_id = r.problem_id and r.user_id = #{userId}) where b.contest_id = #{contestId} and a.id = b.problem_id")
    List<ProblemVO> userGetContestProblemList(int contestId,int userId);

   // @Select("select * from problem as a,contest_problems as b where b.contest_id = #{contestId} and a.id = b.problem_id")
    @Select("select a.*, b.*, coalesce(r.status,-2) as subStatus from problem as a, contest_problems as b left join contest_problem_results r on (r.contest_id = b.contest_id and b.problem_id = r.problem_id and r.user_id = #{userId}) where b.contest_id = #{contestId} and a.id = b.problem_id")
    List<ProblemVO> examGetContestProblemList(int contestId,int userId);

    @Select("select * from problem as a,contest_problems as b where b.contest_id = #{contestId} and a.id = b.problem_id")
    List<ProblemVO> adminGetContestProblemList(int contestId);

    @Select("select a.title,a.id as problem_id from problem as a,contest_problems as b where b.contest_id = #{contestId} and a.id = b.problem_id")
    List<ProblemExcelVO> getContestProblemListByExcel(int contestId);
}
