package com.easyse.easyse_simple.service.tasksservice;

import com.easyse.easyse_simple.pojo.DO.task.Task;
import com.easyse.easyse_simple.pojo.DO.task.Ttask;
import com.easyse.easyse_simple.pojo.vo.ResultVO;

import java.util.HashMap;


public interface MissionService {
    ResultVO getMissionList(Long groupID);

    ResultVO addMission(Task task);

    ResultVO getStudentMission(Integer userId);

    ResultVO endMission(String way, Integer userID, Integer taskID, String stroy);

    ResultVO updateMission(Task task);

    ResultVO getLaterMission();

    HashMap<String, Object> getTaskInfo(String id);

    HashMap<String, Object> getTtaskInfo(String id);


    boolean checkMission(String id, String t);

    ResultVO addttask(Ttask ttask);

    ResultVO deleteTtask(Long id);

    ResultVO deleteTask(Long id);

    ResultVO searchTask(String str);

    ResultVO setTaskQA(Long id, String q, String a);

    ResultVO setTtaskQA(Long id, String q, String a);
}
