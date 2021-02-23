package com.example.oldtown.util.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.oldtown.modules.com.service.ComPoiService;
import com.example.oldtown.modules.trf.model.TrfSweepPoint;
import com.example.oldtown.modules.trf.service.TrfSweepPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/28
 */


public class UploadTrfSweepPointListener extends AnalysisEventListener<TrfSweepPoint> {
    private final Logger LOGGER = LoggerFactory.getLogger(UploadComPoiListener.class);
    private static final int BATCH_COUNT = 50;
    List<TrfSweepPoint> list = new ArrayList<TrfSweepPoint>();
    private TrfSweepPointService trfSweepPointService;
    private Long routeId;
    private Date date;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */
    public UploadTrfSweepPointListener(TrfSweepPointService trfSweepPointService, Long routeId) {
        this.trfSweepPointService = trfSweepPointService;
        this.routeId = routeId;
        this.date = new Date();
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(TrfSweepPoint trfSweepPoint, AnalysisContext analysisContext) {
        trfSweepPoint.setRouteId(routeId);
        trfSweepPoint.setCreateTime(date);
        list.add(trfSweepPoint);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            trfSweepPointService.saveBatch(list);
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        trfSweepPointService.saveBatch(list);
        // 存储完成清理 list
        list.clear();
    }
}
