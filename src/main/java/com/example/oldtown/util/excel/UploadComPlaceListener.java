package com.example.oldtown.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.com.service.ComPlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ding.yp
 * @name ComPlace读取类
 * @info 此Listener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 * @date 2020/12/02
 */


public class UploadComPlaceListener extends AnalysisEventListener<ComPlace> {
    private final Logger LOGGER = LoggerFactory.getLogger(UploadComPlaceListener.class);
    private static final int BATCH_COUNT = 10;
    List<ComPlace> list = new ArrayList<>();
    private ComPlaceService comPlaceService;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */
    public UploadComPlaceListener(ComPlaceService comPlaceService) {
        this.comPlaceService = comPlaceService;
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(ComPlace comPlace, AnalysisContext analysisContext) {
        list.add(comPlace);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            batchSave();
        }
    }

    @Transactional
    public void batchSave() {

        comPlaceService.saveBatch(list);
        // 存储完成清理 list
        list.clear();
    }

    /**
     * 所有数据解析完成了 会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        // comPlaceService.saveBatch(list);
        // 存储完成清理 list
        list.clear();
    }

}
