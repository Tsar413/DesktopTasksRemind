package com.study.workToDo.controller;

import com.study.workToDo.entity.Work;
import com.study.workToDo.service.IWorkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/work")
public class WorkController {

    @Resource
    private IWorkService iWorkService;

    /**
     * 获取全部的工作
     *
     * @return 包含全部工作的list
     */
    @GetMapping("/works")
    public List<Work> getAllWorks(){
        return iWorkService.getAllWorks();
    }

    /**
     * 获取全部未做短时任务与长时任务
     *
     * @return 包含全部未做短时任务list
     */
    @GetMapping("/works/todo")
    public List<Work> getAllToDoWorks(){
        return iWorkService.getAllToDoWorks();
    }

    /**
     * 获取全部已经完成任务的信息
     *
     * @return 包含全部已完成任务list
     */
    @GetMapping("/works/done")
    public List<Work> getAllDoneWorks(){
        return iWorkService.getAllDoneWorks();
    }

    /**
     * 获取全部长时间任务信息
     *
     * @return 包含全部长时间任务信息的list
     */
    @GetMapping("/works/longLife")
    public List<Work> getAllLongLifeWorks(){
        return iWorkService.getAllLongLifeWorks();
    }

    /**
     * 添加新任务
     *
     * @param work 不包含任务id的实体
     * @return 200 “保存成功” 400 "保存失败"
     */
    @PostMapping("/works")
    public String addNewWork(@RequestBody Work work){
        boolean flag = iWorkService.addNewWork(work);
        if(!flag){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "保存出现问题");
        }
        return "保存成功";
    }

    /**
     * 修改任务
     *
     * @param work 修改任务
     * @return 200 “修改成功” 400 "修改失败"
     */
    @PutMapping("/works")
    public String changeWork(@RequestBody Work work){
        boolean flag = iWorkService.changeWork(work);
        if(!flag){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修改出现问题");
        }
        return "修改成功";
    }

    /**
     * 修改任务完成状态
     *
     * @param workId 修改任务完成状态
     * @return 200 “修改成功” 400 "修改失败"
     */
    @PutMapping("/works/{workId}")
    public String changeWorkFinishStatus(@PathVariable String workId){
        boolean flag = iWorkService.changeWorkFinishStatus(workId);
        if(!flag){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "修改出现问题");
        }
        return "修改成功";
    }

    /**
     * 删除任务
     *
     * @param workId 删除任务
     * @return 200 “删除成功” 400 "删除失败"
     */
    @DeleteMapping("/works/{workId}")
    public String deleteWork(@PathVariable String workId){
        boolean flag = iWorkService.deleteWork(workId);
        if(!flag){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "删除出现问题");
        }
        return "删除成功";
    }
}
