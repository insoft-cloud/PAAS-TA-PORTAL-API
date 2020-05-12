package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.MonitoringService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by hrjin on 2017-10-16.
 */
@RestController
public class MonitoringController {

    private final Logger LOGGER = getLogger(MonitoringController.class);
    private final MonitoringService monitoringService;

    @Autowired
    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    /**
     * Cpu 사용량을 가져온다.
     *
     * @param guid             the String
     * @param idx              the long
     * @param defaultTimeRange the String
     * @param groupBy          the String
     * @param type             the String
     * @return the map
     */
    @GetMapping(value = {Constants.EXTERNAL_URL + "/{guid}/{idx}/cpuUsage"})
    public Map getCpuUsage(@PathVariable String guid, @PathVariable long idx, @RequestParam(value = "defaultTimeRange") String defaultTimeRange, @RequestParam(value = "groupBy") String groupBy, @RequestParam(value = "type") String type) {
        LOGGER.info("### get guid ::: {}, idx :::{}, defaultTimeRange :::{}, groupBy :::{}", guid, idx, defaultTimeRange, groupBy);
        return monitoringService.getCpuUsage(guid, idx, defaultTimeRange, groupBy, type);
    }

    /**
     * 메모리 사용량을 가져온다.
     *
     * @param guid             the String
     * @param idx              the long
     * @param defaultTimeRange the String
     * @param groupBy          the String
     * @param type             the String
     * @return the map
     */
    @GetMapping(value = {Constants.EXTERNAL_URL + "/{guid}/{idx}/memoryUsage"})
    public Map getMemoryUsage(@PathVariable String guid, @PathVariable long idx, @RequestParam(value = "defaultTimeRange") String defaultTimeRange, @RequestParam(value = "groupBy") String groupBy, @RequestParam(value = "type") String type) {
        return monitoringService.getMemoryUsage(guid, idx, defaultTimeRange, groupBy, type);
    }

    /**
     * 네트워크 사용량을 가져온다.
     *
     * @param guid             the String
     * @param idx              the long
     * @param defaultTimeRange the String
     * @param groupBy          the String
     * @param type             the String
     * @return the map
     */
    @GetMapping(value = {Constants.EXTERNAL_URL + "/{guid}/{idx}/getNetworkByte"})
    public Map getNetworkByte(@PathVariable String guid, @PathVariable long idx, @RequestParam(value = "defaultTimeRange") String defaultTimeRange, @RequestParam(value = "groupBy") String groupBy, @RequestParam(value = "type") String type) {
        return monitoringService.getNetworkByte(guid, idx, defaultTimeRange, groupBy, type);
    }
}
