package com.easyse.easyse_simple.service.tasksservice;

import com.easyse.easyse_simple.pojo.DO.task.Group;
import com.easyse.easyse_simple.pojo.vo.ResultVO;

import java.util.HashMap;


public interface GroupService {
    ResultVO joinGroup(String teamCode, Long userID);

    ResultVO quitGroup(Long userID);

    ResultVO updateGroupInfo(Group group);

    ResultVO getGroupInfo(Long groupID);

    ResultVO getGroupList(Long classID);

    ResultVO getProgress(Long groupID);

    Integer addGroup(Group group);

    String getCode(Long id);

    ResultVO deleteGroup(Long id);

    HashMap<String, Object> getGroupMembers(Long id);

    ResultVO getWorkBench(Long groupID);

    ResultVO getGroupAllList();

    ResultVO getGroupByYear(Integer year);
}
