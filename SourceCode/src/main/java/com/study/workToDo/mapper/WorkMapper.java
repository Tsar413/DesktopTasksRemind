package com.study.workToDo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.workToDo.entity.Work;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkMapper extends BaseMapper<Work> {
    int changeWork(Work work);
}
