package com.nextrt.core.mapper.exercise;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nextrt.core.entity.exercise.Submission;
import com.nextrt.core.vo.contest.SubmissionVO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by XYZM
 * Project: nextoj
 * User: xyzm
 * Date: 2020/2/24
 * info:
 */
@Repository
public interface SubmissionMapper extends BaseMapper<Submission> {
    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.problem_id, s.language, s.contest_id,p.title,u.username,u.nick FROM submission as s,problem as p,user as u WHERE s.problem_id=p.id and s.user_id=u.user_id",
            "<when test='problemId > 0'> AND s.problem_id = #{problemId} </when>",
            "<when test='contestId &lt; 1'> and s.type=0 </when>",
            "<when test='contestId > 0'> AND s.contest_id = #{contestId} </when> order by s.id desc",
            "</script>"})
    IPage<SubmissionVO> GetContestSubmission(Page<?> page, int problemId, int contestId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.code, s.problem_id, s.language, s.contest_id,p.title,u.username,u.nick FROM submission as s,problem as p,user as u WHERE s.problem_id=p.id and s.user_id=u.user_id and s.user_id = #{userId}",
            "<when test='problemId > 0'> AND s.problem_id = #{problemId} </when>",
            "<when test='contestId &lt; 1'> and s.type=0 </when>",
            "<when test='contestId > 0'> AND s.contest_id = #{contestId} </when> order by s.id desc",
            "</script>"})
    IPage<SubmissionVO> UserGetContestSubmission(Page<?> page, int contestId, int problemId, int userId);


    @Select({"<script>",
            "select s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.code, s.problem_id, s.language, s.contest_id, p.title, u.username, u.nick, u.email, u.school from submission as s, problem as p, user as u where s.problem_id = p.id and s.user_id = u.user_id  and s.contest_id = #{contestId} and s.user_id = #{userId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    IPage<SubmissionVO> contestGetUserContestSubmission(Page<?> page, int contestId, int problemId, int userId);

    @Select({"<script>",
            "select s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.status,s.problem_id, s.language, s.contest_id, p.title, u.username, u.nick, u.email, u.school from submission as s, problem as p, user as u where s.problem_id = p.id and s.user_id = u.user_id  and s.contest_id = #{contestId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    IPage<SubmissionVO> contestGetAllContestSubmission(Page<?> page, int contestId, int problemId);

    @Select({"<script>",
            "select s.id, s.user_id, s.sub_time,s.public_ip,s.local_ip, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.code, s.problem_id, s.language, s.contest_id, p.title, u.username, u.nick, u.email, u.school from submission as s, problem as p, user as u where s.problem_id = p.id and s.user_id = u.user_id  and s.contest_id = #{contestId} and s.user_id = #{userId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    List<SubmissionVO> contestAdminGetUserContestSubmission(int contestId, int problemId, int userId);

    @Select({"<script>",
            "select s.id, s.user_id, s.sub_time,s.public_ip,s.local_ip, s.memory_use, s.score, s.got_score, s.time_use, s.status,s.code,s.problem_id, s.language, s.contest_id, p.title, u.username, u.nick, u.email, u.school from submission as s, problem as p, user as u where s.problem_id = p.id and s.user_id = u.user_id  and s.contest_id = #{contestId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    List<SubmissionVO> contestAdminGetAllContestSubmission(int contestId, int problemId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.code, s.problem_id, s.language, s.contest_id,p.title,u.username, u.name as nick, u.school_no, u.team FROM submission as s,problem as p,exam_user as u WHERE s.problem_id=p.id and s.user_id=u.contest_user_id and s.contest_id = #{contestId} and s.user_id = #{userId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    IPage<SubmissionVO> examGetUserContestSubmission(Page<?> page, int contestId, int problemId, int userId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.problem_id, s.language, s.contest_id,p.title,u.username, u.name as nick, u.school_no, u.team FROM submission as s,problem as p,exam_user as u WHERE s.problem_id=p.id and s.user_id=u.contest_user_id and s.contest_id = #{contestId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    IPage<SubmissionVO> examGetAllContestSubmission(Page<?> page, int contestId, int problemId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time,s.public_ip,s.local_ip, s.memory_use, s.score, s.got_score, s.time_use, s.status,s.code, s.code, s.problem_id, s.language, s.contest_id,p.title,u.username, u.name as nick, u.school_no, u.team FROM submission as s,problem as p,exam_user as u WHERE s.problem_id=p.id and s.user_id=u.contest_user_id and s.contest_id = #{contestId} and s.user_id = #{userId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    List<SubmissionVO> examAdminGetUserContestSubmission(int contestId, int problemId, int userId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time,s.public_ip,s.local_ip, s.memory_use, s.score, s.got_score, s.time_use, s.status, s.problem_id, s.language, s.contest_id,p.title,u.username, u.name as nick, u.school_no, u.team FROM submission as s,problem as p,exam_user as u WHERE s.problem_id=p.id and s.user_id=u.contest_user_id and s.contest_id = #{contestId}",
            "<when test='problemId > 0'> and s.problem_id = #{problemId} </when> order by s.id desc " +
                    "</script>"})
    List<SubmissionVO> examAdminGetAllContestSubmission(int contestId, int problemId);


    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.public_ip, s.status, s.problem_id, s.language, s.contest_id,p.title,u.username,u.nick FROM submission as s,problem as p,user as u WHERE s.problem_id=p.id and s.user_id=u.user_id and s.contest_id=0",
            "<when test='problemId > 0'> AND s.problem_id = #{problemId} </when>",
            "<when test='userId > 0'> AND s.user_id = #{userId} </when> order by s.id desc",
            "</script>"})
    IPage<SubmissionVO> TouristsGetSubmission(Page<?> page, int problemId, int userId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.score, s.got_score, s.time_use, s.code, s.public_ip, s.status, s.problem_id, s.language, s.contest_id,p.title,u.username,u.nick FROM submission as s,problem as p,user as u WHERE s.problem_id=p.id and s.user_id=u.user_id and s.contest_id=0",
            "<when test='problemId > 0'> AND s.problem_id = #{problemId} </when>",
            "<when test='userId > 0'> AND s.user_id = #{userId} </when> order by s.id desc",
            "</script>"})
    IPage<SubmissionVO> AdminGetSubmission(Page<?> page, int problemId, int userId);

    @Select({"<script>",
            "SELECT s.id, s.user_id, s.sub_time, s.memory_use, s.other_info, s.score, s.got_score, s.time_use, s.code, s.status, s.problem_id, s.language, s.contest_id,p.title,u.username,u.nick FROM submission as s,problem as p,user as u WHERE s.problem_id=p.id and s.user_id=u.user_id and s.contest_id=0",
            "<when test='problemId > 0'> AND s.problem_id = #{problemId} </when>",
            "<when test='userId > 0'> AND s.user_id = #{userId} </when> order by s.id desc",
            "</script>"})
    IPage<SubmissionVO> getOrdinaryUserSubmission(Page<?> page, int problemId, int userId);


    @Update({"<script> UPDATE submission SET status=-1 WHERE contest_id=#{contestId}",
            "<when test='problemId > 0'> AND problem_id = #{problemId} </when>",
            "</script>"})
    int updateStatus(int contestId,int problemId);

}
