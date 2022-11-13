package com.nextrt.acm.controller.admin;

import com.nextrt.acm.service.oj.admin.AdminProblemService;
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
import java.util.List;


@RestController
@RequestMapping(value = "/admin/problem", produces = "application/json;charset=UTF-8")
public class AdminProblemController {
    private final AdminProblemService problemService;

    public AdminProblemController(AdminProblemService problemService) {
        this.problemService = problemService;
    }

    @ProblemOPLog(Desc = "添加问题")
    @PostMapping("/add")
    public Result addProblem(@RequestBody @Valid Problem problem, HttpServletRequest request) {
        problem.setIsPublic(1);
        problem.setUserId(request.getIntHeader("userId"));
        problem.setMemoryLimit(problem.getMemoryLimit() * 1024);
        int pid =  problemService.addProblem(problem);
        ProblemTestData problemTestData = new ProblemTestData();
        problemTestData.setProblemId(pid);
        problemTestData.setInput(problem.getInputExample());
        problemTestData.setOutput(problem.getOutputExample());
        problemTestData.setScore(0);
        return addProblemsTestData(problemTestData);
    }

    @ProblemOPLog(Desc = "更新问题")
    @PostMapping("/update")
    public Result updateProblem(@RequestBody @Valid Problem problem, HttpServletRequest request) {
        problem.setIsPublic(1);
        problem.setUserId(request.getIntHeader("userId"));
        problem.setMemoryLimit(problem.getMemoryLimit() * 1024);
        problemService.updateProblem(problem);
        int pid = problem.getId();
        List<ProblemTestData> problemTestDataList = (List<ProblemTestData>)getProblemsTestData(pid).getData();
        ProblemTestData problemTestData = null;
        if(problemTestDataList == null) {
            problemTestData = new ProblemTestData();
            problemTestData.setProblemId(pid);
            problemTestData.setScore(0);
            problemTestData.setInput(problem.getInputExample());
            problemTestData.setOutput(problem.getOutputExample());
            return addProblemsTestData(problemTestData);
        } else {
            problemTestData = problemTestDataList.get(0);
            problemTestData.setInput(problem.getInputExample());
            problemTestData.setOutput(problem.getOutputExample());
            return updateProblemsTestData(problemTestData);
        }
    }

    @GetMapping("/get/{problemId:[0-9]+}")
    public Result getProblemByAdminId(@PathVariable int problemId) {
        return problemService.getProblemById(problemId);
    }

    @GetMapping("/list")
    public Result getProblemsByAdmin(@RequestParam(required = false) int difficulty, @RequestParam(required = false) String name, int page, int size) {
        return problemService.getProblems(difficulty, name, page, size);
    }

    @GetMapping(value = "/delete/{problemId:[0-9]+}")
    public Result deleteProblem(@PathVariable int problemId) {
        return problemService.deleteProblemById(problemId);
    }

    @GetMapping("/test/list/{problemId:[0-9]+}")
    public Result getProblemsTestData(@PathVariable int problemId) {
        return problemService.getProblemTestData(problemId);
    }

    @GetMapping("/test/delete/{problemTestId:[0-9]+}")
    public Result deleteProblemsTestData(@PathVariable int problemTestId) {
        return problemService.deleteProblemTestDataByTestId(problemTestId);
    }

    @GetMapping("/test/clean/{problemId:[0-9]+}")
    public Result cleanProblemsTestData(@PathVariable int problemId) {
        return problemService.cleanProblemTestData(problemId);
    }

    @PostMapping("/test/add")
    public Result addProblemsTestData(@RequestBody ProblemTestData problemTestData) {
        return problemService.addProblemTestData(problemTestData);
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
    public Result updateProblemsTestData(@RequestBody ProblemTestData problemTestData) {
        return problemService.updateProblemTestData(problemTestData);
    }
}
