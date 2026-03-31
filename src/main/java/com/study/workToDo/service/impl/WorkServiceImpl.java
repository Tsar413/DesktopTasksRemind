package com.study.workToDo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.workToDo.entity.Work;
import com.study.workToDo.mapper.WorkMapper;
import com.study.workToDo.service.IWorkService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {
    @Override
    public List<Work> getAllWorks() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Work> getAllToDoWorks() {
        QueryWrapper<Work> wrapper = new QueryWrapper<>(); // 查找长时间任务
        wrapper.eq("is_finished", 0)
                .eq("is_long_life_task", 0)
                .orderByAsc("dead_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Work> getAllDoneWorks() {
        QueryWrapper<Work> wrapper = new QueryWrapper<>(); // 查找已经完成的任务
        wrapper.eq("is_finished", 1);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Work> getAllLongLifeWorks() {
        QueryWrapper<Work> wrapper = new QueryWrapper<>(); // 查找长时间任务
        wrapper.eq("is_finished", 0)
                .eq("is_long_life_task", 1);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean addNewWork(Work work) {
        String id = ""; // id生成与校验
        while (true){
            id = UUID.randomUUID().toString();
            Work work1 = baseMapper.selectOne(new QueryWrapper<Work>().eq("work_id", id));
            if(work1 == null){
                break;
            }
        }
        work.setWorkId(id);
        work.setIsFinished(0);
        work.setSubmitTime(LocalDateTime.now());
        try {
            baseMapper.insert(work);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean changeWork(Work work) {
        try {
            return baseMapper.changeWork(work) > 0;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean changeWorkFinishStatus(String workId) {
        QueryWrapper<Work> wrapper = new QueryWrapper<>(); // 查找长时间任务
        wrapper.eq("work_id", workId);
        List<Work> list = baseMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return false;
        }
        Work work = list.get(0);
        try {
            work.setIsFinished(work.getIsFinished() == 1 ? 0 : 1);
            return baseMapper.changeWork(work) > 0;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteWork(String workId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("work_id", workId);
        try {
            return baseMapper.deleteByMap(map) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
