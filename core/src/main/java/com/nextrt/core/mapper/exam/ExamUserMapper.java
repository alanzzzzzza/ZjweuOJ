package com.nextrt.core.mapper.exam;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nextrt.core.entity.exam.ExamUser;
import com.nextrt.core.vo.contest.ExamRankVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamUserMapper extends BaseMapper<ExamUser> {

    @Select("select e.contest_user_id, e.username , e.school_no ,e.name , e.school ,e.team , e.class_name ,e.sub , e.score ,e.ac ,e.score ,e.ac from exam_user as e where e.contest_id = #{contestId} order by e.score desc,e.ac desc,e.sub")
    List<ExamRankVO> getExamRankByOiMode(int contestId);

    @Select("select e.contest_user_id, e.username , e.school_no ,e.name , e.school ,e.team , e.class_name ,e.sub , e.score ,e.ac ,e.score ,e.ac  from exam_user as e where e.contest_id = #{contestId} order by e.ac desc,e.sub ")
    List<ExamRankVO> getExamRankByAcmMode(int contestId);

}
