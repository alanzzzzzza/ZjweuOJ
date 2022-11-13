package com.nextrt.acm.controller.teacher;

import com.nextrt.acm.service.oj.teacher.TeacherProblemService;
import com.nextrt.acm.util.NetUtil;
import com.nextrt.acm.util.file.LocalFileUtil;
import com.nextrt.core.entity.annotation.ProblemOPLog;
import com.nextrt.core.entity.exercise.Problem;
import com.nextrt.core.entity.exercise.ProblemTestData;
import com.nextrt.core.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/teacher/problem", produces = "application/json;charset=UTF-8")
public class TeacherProblemController {
    private final TeacherProblemService problemService;

    public TeacherProblemController(TeacherProblemService problemService) {
        this.problemService = problemService;
    }

    @ProblemOPLog(Desc = "添加问题")
    @PostMapping("/add")
    public Result addProblem(@RequestBody @Valid Problem problem, HttpServletRequest request) {
        problem.setUserId(request.getIntHeader("userId"));
        problem.setMemoryLimit(problem.getMemoryLimit() * 1024);
        return problemService.addProblem(problem);
    }

    @ProblemOPLog(Desc = "更新问题")
    @PostMapping("/update")
    public Result updateProblem(@RequestBody @Valid Problem problem, HttpServletRequest request) {
        problem.setUserId(request.getIntHeader("userId"));
        problem.setMemoryLimit(problem.getMemoryLimit() * 1024);
        return problemService.updateProblem(problem);
    }

    @GetMapping("/get/{problemId:[0-9]+}")
    public Result getProblemByAdminId(@PathVariable int problemId, HttpServletRequest request) {
        return problemService.getProblemById(problemId,request.getIntHeader("userId"));
    }

    @GetMapping("/list")
    public Result getProblemsByAdmin(@RequestParam(required = false) int difficulty, @RequestParam(required = false) String name, int page, int size, HttpServletRequest request) {
        return problemService.getProblems(request.getIntHeader("userId"),difficulty, name, page, size);
    }

    @GetMapping(value = "/delete/{problemId:[0-9]+}")
    public Result deleteProblem(@PathVariable int problemId, HttpServletRequest request) {
        return problemService.deleteProblemById(problemId,request.getIntHeader("userId"));
    }

    @GetMapping("/test/list/{problemId:[0-9]+}")
    public Result getProblemsTestData(@PathVariable int problemId, HttpServletRequest request) {
        return problemService.getProblemTestData(problemId,request.getIntHeader("userId"));
    }

    @GetMapping("/test/delete/{problemTestId:[0-9]+}")
    public Result deleteProblemsTestData(@PathVariable int problemTestId, HttpServletRequest request) {
        return problemService.deleteProblemTestDataByTestId(problemTestId,request.getIntHeader("userId"));
    }

    @GetMapping("/test/clean/{problemId:[0-9]+}")
    public Result cleanProblemsTestData(@PathVariable int problemId, HttpServletRequest request) {
        return problemService.cleanProblemTestData(problemId,request.getIntHeader("userId"));
    }

    @PostMapping("/test/add")
    public Result addProblemsTestData(@RequestBody ProblemTestData problemTestData, HttpServletRequest request) {
        return problemService.addProblemTestData(problemTestData,request.getIntHeader("userId"));
    }

    @GetMapping("test/export/{problemId:[0-9]+}")
    public Result exportTestData(@PathVariable int problemId, HttpServletRequest request, HttpServletResponse response) {
        Result result = problemService.exportProblemTestDataToZip(problemId, request.getIntHeader("userId"));
        if (result.getStatus() == 1) {
            LocalFileUtil.downloadFile(response, result.getData().toString());
            return null;
        }
        return result;
    }

    @PostMapping(value = "/test/add/{problemId:[0-9]+}")
    public Result upload(@RequestParam("file") MultipartFile file, @PathVariable int problemId, HttpServletRequest request) {
        if (file.isEmpty()) return new Result(-4, "文件不能为空，请检查文件！", "");
        int userId = request.getIntHeader("userId");
        return problemService.addProblemTestDataByFile(file, problemId, userId, NetUtil.getPublicIP(request));
    }

    @PostMapping("/test/update")
    public Result updateProblemsTestData(@RequestBody ProblemTestData problemTestData, HttpServletRequest request) {
        return problemService.updateProblemTestData(problemTestData,request.getIntHeader("userId"));
    }
}
