package com.study.workToDo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_work")
@TableName("tb_work")
public class Work {
    @Id
    @Column(name = "work_id", length = 100)
    @TableField("work_id")
    private String workId;

    @Column(name = "work_title")
    @TableField("work_title")
    private String workTitle;

    @Lob
    @Column(name = "work_content", columnDefinition = "LONGTEXT")
    @TableField("work_content")
    private String workContent;

    @Lob
    @Column(name = "work_comment", columnDefinition = "LONGTEXT")
    @TableField("work_comment")
    private String workComment;

    @Column(name = "is_finished")
    @TableField("is_finished")
    private Integer isFinished; // 是否完成

    @Column(name = "is_long_life_task")
    @TableField("is_long_life_task")
    private Integer isLongLifeTask; // 是否是长时任务；

    @JsonFormat(timezone = "GMT + 8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime submitTime; // 任务提交时间

    @JsonFormat(timezone = "GMT + 8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime deadTime; // 任务截止时间

    public Work() {
    }

    public Work(String workId, String workTitle, String workContent, String workComment, Integer isFinished, Integer isLongLifeTask, LocalDateTime submitTime, LocalDateTime deadTime) {
        this.workId = workId;
        this.workTitle = workTitle;
        this.workContent = workContent;
        this.workComment = workComment;
        this.isFinished = isFinished;
        this.isLongLifeTask = isLongLifeTask;
        this.submitTime = submitTime;
        this.deadTime = deadTime;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getWorkComment() {
        return workComment;
    }

    public void setWorkComment(String workComment) {
        this.workComment = workComment;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public Integer getIsLongLifeTask() {
        return isLongLifeTask;
    }

    public void setIsLongLifeTask(Integer isLongLifeTask) {
        this.isLongLifeTask = isLongLifeTask;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public LocalDateTime getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(LocalDateTime deadTime) {
        this.deadTime = deadTime;
    }

    @Override
    public String toString() {
        return "Work{" +
                "workId='" + workId + '\'' +
                ", workTitle='" + workTitle + '\'' +
                ", workContent='" + workContent + '\'' +
                ", workComment='" + workComment + '\'' +
                ", isFinished=" + isFinished +
                ", isLongLifeTask=" + isLongLifeTask +
                ", submitTime=" + submitTime +
                ", deadTime=" + deadTime +
                '}';
    }
}
