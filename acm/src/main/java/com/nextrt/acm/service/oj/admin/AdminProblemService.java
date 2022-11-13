package com.nextrt.acm.service.oj.admin;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nextrt.acm.biz.exercise.ProblemBiz;
import com.nextrt.acm.service.AttachmentService;
import com.nextrt.acm.util.file.LocalFileUtil;
import com.nextrt.core.entity.common.Attachment;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.entity.exercise.ProblemTestData;
import com.nextrt.core.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AdminProblemService {
    private final ProblemBiz problemBiz;
    private final AttachmentService attachmentService;

    public AdminProblemService(ProblemBiz problemBiz, AttachmentService attachmentService) {
        this.problemBiz = problemBiz;
        this.attachmentService = attachmentService;
    }

    public int addProblem(Problem problem) {
        problem.setAddTime(new Date());
        problem.setUpdateTime(new Date());
        return problemBiz.addProblem(problem);
    }

    public Result deleteProblemById(int problemId) {
        if (problemBiz.deleteProblemById(problemId) != 0) {
            problemBiz.deleteProblemTestDataByProblemId(problemId);
            return new Result(1, "问题删除成功");
        }
        return new Result(0, "问题删除失败，请联系管理员");
    }

    public Result updateProblem(Problem problem) {
        problem.setUpdateTime(new Date());
        if (problemBiz.updateProblem(problem) > 0)
            return new Result(1, "问题更新成功,请添加测试数据!");
        return new Result(0, "问题更新失败，请联系管理员");
    }

    public Result getProblemById(int problemId) {
        Problem problem = problemBiz.getProblemById(problemId);
        if (problem == null)
            return new Result(-1, "该问题不存在!");
        problem.setMemoryLimit(problem.getMemoryLimit() / 1024);
        return new Result(1, "问题信息获取成功", problem);
    }


    public Result getProblems(int difficulty, String name, int page, int size) {
        IPage<Problem> problemIPage = problemBiz.getProblems(difficulty, name, page, size);
        return new Result(1, "题目信息获取成功！", problemIPage);
    }

    public Result getProblemTestData(int problemId) {
        List<ProblemTestData> list = problemBiz.getProblemTestDataByProblemId(problemId);
        return new Result(1, "题目测试数据列表获取成功！", list);
    }

    public Result addProblemTestData(ProblemTestData problemTestData) {
        if (problemBiz.addProblemTestData(problemTestData) > 0)
            return new Result(1, "题目测试数据添加成功！");
        return new Result(0, "添加测试数据失败！");
    }

    public Result deleteProblemTestDataByTestId(int problemTestId) {
        if (problemBiz.deleteProblemTestDataById(problemTestId) > 0)
            return new Result(1, "题目测试数据删除成功！");
        return new Result(0, "测试数据删除失败！");
    }

    public Result cleanProblemTestData(int problemId) {
        if (problemBiz.deleteProblemTestDataByProblemId(problemId) > 0)
            return new Result(1, "题目测试数据已经清空");
        return new Result(0, "题目测试数据清空失败");
    }

    public Result updateProblemTestData(ProblemTestData problemTestData) {
        if (problemBiz.updateProblemTestData(problemTestData) > 0)
            return new Result(1, "题目测试数据更新成功！");
        return new Result(0, "更新测试数据失败！");
    }

    public Result exportProblemTestDataToZip(int problemId, int userId) {
        List<ProblemTestData> list = problemBiz.getProblemTestDataByProblemId(problemId);
        if(list.isEmpty())
            return new Result(-1,"没有可以导出的数据");
        String path = "problem_test_data_export" + File.separator + "Problem_" + problemId + "_tmp" + File.separator;
        list.forEach(x -> {
            int i = list.indexOf(x)+1;
            attachmentService.tempWriteString(userId, path + "tmp" + File.separator + i + ".in", x.getInput());
            attachmentService.tempWriteString(userId, path + "tmp" + File.separator + i + ".out", x.getOutput());
            if (x.getScore() != null && x.getScore() > 0)
                attachmentService.tempWriteString(userId, path + "tmp" + File.separator + i + ".score", String.valueOf(x.getScore()));
        });
        return new Result(1, "文件压缩成功", attachmentService.zip(path + "tmp",path + File.separator + "Problem_" + problemId + ".zip",userId));
    }

    public Result addProblemTestDataByFile(MultipartFile file, int problemId, int userId, String ip) {
        String fileName = file.getOriginalFilename();
        if (fileName == null)
            return new Result(-2, "文件名不能为空");
        try {
            String type = FileTypeUtil.getType(file.getInputStream());
            if (!StrUtil.isNotBlank(type) || !type.equalsIgnoreCase("zip"))
                return new Result(-3, "文件必须是ZIP压缩文件");
            fileName = "Problem_" + problemId + "_TestData.zip";
            String savePath = "problem_test_data" + File.separator + fileName;
            Result result = attachmentService.addFile(file, fileName, "zip", savePath, userId, ip);
            if (result.getStatus() != 1)
                return result;
            Attachment attachment = (Attachment) result.getData();
            String tmpPath = attachment.getFileSavePath().replace(attachment.getFileName(), "Problem_" + problemId + "_tmp");
            ZipUtil.unzip(attachment.getFileSavePath(), tmpPath);
            for (int i = 1; ; i++) {
                try {
                    String in = new FileReader(tmpPath + File.separator + i + ".in").readString().trim();
                    String out = new FileReader(tmpPath + File.separator + i + ".out").readString().trim();
                    int score;
                    try {
                        score = Integer.parseInt(new FileReader(tmpPath + File.separator + i + ".score").readString().trim());
                    }catch (Exception e) {
                        score = 0;
                        e.printStackTrace();
                    }
                    if (StrUtil.hasBlank(in) || StrUtil.hasBlank(out)) break;
                    ProblemTestData problemTestData = ProblemTestData.builder().problemId(problemId).input(in).output(out).score(score).build();
                    problemBiz.addProblemTestData(problemTestData);
                } catch (IORuntimeException e) {
                    break;
                }
            }
            LocalFileUtil.deleteFile(tmpPath);
            attachmentService.deleteFile(attachment.getFileId());
            return new Result(1, "测试数据导入成功");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(-1, "系统出现错误，文件上传失败");
    }
}
