package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.openpaas.paasta.portal.api.service.BuildPackServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by swmoon on 2017-12-19.
 */
@RestController
public class BuildPackControllerV2 extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPackControllerV2.class);


    @Autowired
    private BuildPackServiceV2 buildPackServiceV2;


    /**
     * 빌드팩 리스트 가져오기
     *
     * @return boolean boolean
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V2_URL + "/buildpacks"})
    public Map<String, Object> getBuildPacks() throws Exception {

        LOGGER.info("getBuildPacks Start");
        Map<String, Object> buildPacks = buildPackServiceV2.getBuildPacks();
        LOGGER.info("getBuildPacks End ");

        return buildPacks;

    }


    /**
     * 빌드팩 정보 수정
     *
     * @param buildPack the buildPack
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PutMapping(value = {Constants.V2_URL + "/buildpacks/{guid}"})
    public Map<String, Object> updateBuildPack(@RequestBody BuildPack buildPack, @PathVariable String guid) throws Exception {

        LOGGER.info("updateBuildPack Start : " + guid);
        Map<String, Object> resultMap = new HashMap<>();
        buildPack.setGuid(UUID.fromString(guid));
        buildPackServiceV2.updateBuildPack(buildPack);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        LOGGER.info("updateBuildPack End ");

        return resultMap;
    }
}
