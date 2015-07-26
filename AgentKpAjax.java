/**
 * DingDing.com Inc. Copyright (c) 2000-2015 All Rights Reserved.
 */
package com.dingding.agentkp.ajax;

import com.dingding.agentkp.bo.dto.CommonResponse;
import com.dingding.agentkp.common.AgentKpConstant;
import com.dingding.agentkp.service.AgentPerformanceDetailsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dingding.agentkp.bo.dto.AgentKpRequest;
import com.dingding.agentkp.common.ApiErrCode;
import com.dingding.agentkp.service.AgentPerformanceService;

import java.util.Map;

/**
 * 经纪人绩效入口
 * 
 * @author surlymo
 * @date Jun 20, 2015
 */
@Controller
public class AgentKpAjax {

    private static final Logger logger = Logger.getLogger(AgentKpAjax.class);

    @Autowired
    private AgentPerformanceService agentPerformanceService;
    @Autowired
    private AgentPerformanceDetailsService agentPerformanceDetailsService;

    /**
     * 获取经纪人指定月份绩效整体情况
     * 
     * @param request {@link AgentKpRequest}
     * @return {@link CommonResponse}
     */
    @RequestMapping(value = "/getMyPerform.do")
    @ResponseBody
    public CommonResponse<Map<String, Integer>> getMyPerform(AgentKpRequest request) {
        CommonResponse response = null;
        long beginTime = System.currentTimeMillis();
        try {
            if (StringUtils.isEmpty(request.getDate())
                    || request.getAgentId() == null || request.getAgentId().longValue() <= 0) {
                logger.error("Param error. agentId or date is null or empty! request: " + request);
                response = errorReturn(ApiErrCode.ERRCODE_PARA_INVALID, AgentKpConstant.MSG_FAILED);
            } else {
                response = agentPerformanceService.getAgentReport(request.getAgentId(), request.getDate());
            }
        } catch (Exception e) {
            logger.error("System inner error! request: " + request, e);
            response = errorReturn(ApiErrCode.ERRCODE_UNKNOW, AgentKpConstant.MSG_FAILED);
        } finally {
            logger.info("request: " + request + ", response: " + response + ", cost: "
                    + (System.currentTimeMillis() - beginTime));
        }
        return response;
    }

    /**
     * 获取经纪人指定月份指定类型绩效明细情况
     * 
     * @param request {@link AgentKpRequest}
     * @return {@link CommonResponse}
     */
    @RequestMapping(value = "/getPerformDetails.do")
    @ResponseBody
    public CommonResponse getMyPerformDetails(AgentKpRequest request) {
        CommonResponse response = null;
        long beginTime = System.currentTimeMillis();
        try {
            if (StringUtils.isEmpty(request.getDate()) || request.getAgentId() == null
                    || request.getType() == null || request.getType() < 0 || request.getAgentId().longValue() <= 0) {
                logger.error("Param error. agentId or date is null or empty! request: " + request);
                response = errorReturn(ApiErrCode.ERRCODE_PARA_INVALID, AgentKpConstant.MSG_FAILED);
            } else {
                response = agentPerformanceDetailsService.getAgentReport(
                        request.getAgentId(), request.getDate(), request.getType());
            }
        } catch (Exception e) {
            logger.error("System inner error! request: " + request, e);
            response = errorReturn(ApiErrCode.ERRCODE_UNKNOW, AgentKpConstant.MSG_FAILED);
        } finally {
            logger.info("request: " + request + ", response: " + response + ", cost: "
                    + (System.currentTimeMillis() - beginTime));
        }
        return response;
    }

    private CommonResponse errorReturn(int code, String message) {
        CommonResponse response = new CommonResponse();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
