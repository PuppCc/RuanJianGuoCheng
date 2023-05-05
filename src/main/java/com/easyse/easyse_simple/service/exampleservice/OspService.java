package com.easyse.easyse_simple.service.exampleservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyse.easyse_simple.pojo.DO.example.ExamplesOsp;
import com.easyse.easyse_simple.pojo.vo.ResultVO;


public interface OspService {
     ResultVO getOspsPage(Integer page);

     ResultVO putOsp(ExamplesOsp osp);

     ResultVO deleteOsp(Long id);

    ResultVO getOspsKinds();

    ResultVO getOspsByKind(String kind);

    ResultVO search(String string);
}
