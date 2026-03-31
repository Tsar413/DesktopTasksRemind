package com.study.workToDo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.workToDo.entity.Work;

import java.util.List;

public interface IWorkService extends IService<Work> {
    List<Work> getAllWorks();

    List<Work> getAllToDoWorks();

    List<Work> getAllDoneWorks();

    List<Work> getAllLongLifeWorks();

    boolean addNewWork(Work work);

    boolean changeWork(Work work);

    boolean changeWorkFinishStatus(String workId);

    boolean deleteWork(String workId);
}
