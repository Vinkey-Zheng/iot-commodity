package com.iot.manage.domain.vo;

import com.iot.manage.domain.Task;
import com.iot.manage.domain.TaskType;
import lombok.Data;

@Data
public class TaskVo extends Task {

    // 工单类型
    private TaskType taskType;
}
