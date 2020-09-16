package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.spaces.GetSpaceSummaryResponse;
import org.cloudfoundry.client.v2.spaces.ListSpaceServicesResponse;
import org.cloudfoundry.client.v2.spaces.ListSpaceUserRolesResponse;
import org.cloudfoundry.client.v3.spaces.AssignSpaceIsolationSegmentResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.openpaas.paasta.portal.api.model.UserRole;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.openpaas.paasta.portal.api.service.OrgServiceV3;
import org.openpaas.paasta.portal.api.service.SpaceServiceV3;
import org.openpaas.paasta.portal.api.service.UserServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cheolhan on 2019-04-24.
 */
@RestController
public class SpaceControllerV3 extends Common {
    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////


    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceControllerV3.class);

    /**
     * The Space service.
     */
    @Autowired
    SpaceServiceV3 spaceServiceV3;
    /**
     * The Org service.
     */
    @Autowired
    OrgServiceV3 orgServiceV3;

    @Autowired
    UserServiceV3 userServiceV3;

    @Autowired
    AppServiceV3 appServiceV3;

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceid}/summary")
    public GetSpaceSummaryResponse getSpaceSummary(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        GetSpaceSummaryResponse respSapceSummary = spaceServiceV3.getSpaceSummary(spaceid, token);
        return respSapceSummary;
    }

    /**
     * 공간 요약 정보를 조회한다. (관리자)
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceid}/summary-admin")
    public GetSpaceSummaryResponse getSpaceSummaryAdmin(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        GetSpaceSummaryResponse respSapceSummary = spaceServiceV3.getSpaceSummary(spaceid, token);
        return respSapceSummary;
    }


    /**
     * 공간 요약 정보를 조회한다. (관리자)
     *
     * @param spaceid the spaceId
     * @param token   the token
     * @return Space respSpace
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceid}/summarylist")
    public Map getSpaceSummary2(@PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("Get SpaceSummary Start : " + spaceid);
        return spaceServiceV3.getSpaceSummary2(spaceid,token);
    }

    /**
     * 공간명을 변경한다.
     *
     * @param space the space
     * @param token the token
     * @return UpdateSpaceResponse
     * @throws Exception the exception
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @PutMapping(Constants.V3_URL + "/spaces")
    public Map renameSpace(@RequestBody Space space, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("renameSpace Start ");
        Map resultMap = spaceServiceV3.renameSpace(space, token);

        LOGGER.info("renameSpace End ");
        return resultMap;
    }


    /**
     * 공간을 삭제한다.
     *
     * @param guid  the space
     * @param token the request
     * @return ModelAndView model
     * @version 2.0
     * @author hgcho
     * @since 2018.5.8
     */
    @DeleteMapping(Constants.V3_URL +  "/spaces/{guid}")
    public Map deleteSpace(@PathVariable String guid, @RequestParam boolean recursive, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("deleteSpace Start ");

        Map resultMap = spaceServiceV3.deleteSpace(guid, recursive, token);

        LOGGER.info("deleteSpace End ");
        return resultMap;
    }

    @RequestMapping(value = {Constants.V3_URL + "/spaces/{guid}/services"}, method = RequestMethod.GET)
    public ListSpaceServicesResponse getSpaceServices(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceServices Start : " + guid);

        ListSpaceServicesResponse respSpaceServices = spaceServiceV3.getSpaceServices(guid);

        LOGGER.info("getSpaceServices End ");

        return respSpaceServices;
    }

    /**
     * 공간에 속한 유저들의 역할(Role)을 전부 조회한다. 단, 조직에 속해있지만 공간에 속하지 않은 유저는 빈 배열로 채운다.
     *
     * @param spaceId
     * @param token
     * @return Users with roles that belong in the organization
     * @author hgcho
     * @version 2.0
     * @since 2018.5.16
     */
    @GetMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles")
    public ListSpaceUserRolesResponse getSpaceUserRoles(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return spaceServiceV3.getSpaceUserRoles(spaceId, token);

    }

    @GetMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles2")
    public Map getSpaceUserRoles2(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        Map resultMap = new HashMap();
        Map spaceMap = spaceServiceV3.getSpaceUserRoles2(spaceId, token);
        resultMap.put("result", spaceMap);
        return resultMap;
    }

    @PutMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles")
    public boolean associateSpaceUserRoles(@PathVariable String spaceId, @RequestBody List<UserRole> Roles, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return spaceServiceV3.associateSpaceUserRoles(spaceId, Roles, token);
    }

    @DeleteMapping(Constants.V3_URL + "/spaces/{spaceId}/user-roles")
    public void removeSpaceUserRoles(@PathVariable String spaceId, @RequestParam String userId, @RequestParam String role, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        spaceServiceV3.removeSpaceUserRole(spaceId, userId, role);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////   Document : https://v3-apidocs.cloudfoundry.org/version/3.69.0/index.html#spaces                    //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Space 정보를 가져온다.
     *
     * @param spaceId    the space guid
     * @param  token    user token
     * @return GetSpaceResponse
     * 권한 : 사용자권한
     */
    @GetMapping(Constants.V3_URL+"/spaces/{spaceId:.+}")
    public org.cloudfoundry.client.v3.spaces.GetSpaceResponse getSpace(@PathVariable String spaceId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.getSpaceV3(spaceId, token);
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param  organizationId    the organization guid
     * @param  token    user token
     * @return ListSpacesResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/spaces/{organizationId:.+}")
    public org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpace(@PathVariable String organizationId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return spaceServiceV3.listSpace(token,organizationId);
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param  organizationId    the organization guid
     * @return ListSpacesResponse
     * 권한 : 관리자 권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/spaces-admin/{organizationId:.+}")
    public org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpaceAdmin(@PathVariable String organizationId){
        return spaceServiceV3.listSpaceAdmin(organizationId);
    }

    /**
     * Space명 중복검사를 실행한다.
     * @param  organizationId    the organization guid
     * @param  spaceName                this space name
     * @return boolean
     * 권한 : 사용자
     */
    @GetMapping(Constants.V3_URL + "/spaces/{organizationId:.+}/{spaceName:.+}/exist")
    public boolean isExistSpaceName(@PathVariable String organizationId, @PathVariable String spaceName) {
        return spaceServiceV3.isExistSpaceName(organizationId,spaceName);
    }

    /**
     * 운영자 포털에서 Space를 생성한다.
     *
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL+"/spaces")
    public Map<?,?> createSpace(@RequestBody Map space){
        return spaceServiceV3.createSpace(space);
    }

    /**
     * Space Isolation 에 Isolation Segments 를 설정한다.
     *
     * @param spaceId            the space id
     * @param isolationSegmentId the isolation segement id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/spaces/{spaceId:.+}/isolationSegments/{isolationSegmentId:.+}")
    public AssignSpaceIsolationSegmentResponse setSpaceDefaultIsolationSegments(@PathVariable String spaceId, @PathVariable String isolationSegmentId) throws Exception {
        return spaceServiceV3.setSpaceDefaultIsolationSegments(spaceId, isolationSegmentId);
    }

    /**
     * Space Isolation 에 Isolation Segments 를 해제한다.
     *
     * @param spaceId the space id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/spaces/{spaceId:.+}/isolationSegments/reset")
    public AssignSpaceIsolationSegmentResponse resetSpaceDefaultIsolationSegments(@PathVariable String spaceId) throws Exception {
        return spaceServiceV3.resetSpaceDefaultIsolationSegments(spaceId);
    }

    /**
     * 운영자포탈에서 스페이스 목록전체를 가져온다
     *
     * @return
     */
    @GetMapping(Constants.V3_URL  + "/spaces-admin")
    public org.cloudfoundry.client.v2.spaces.ListSpacesResponse getSpacesForAdmin(){
        return spaceServiceV3.getSpacesForAdmin();
    }

    /**
     * 운영자 포털에서 공간명과 할당량을 변경한다. (Space : Update)
     *
     * @param space
     * @return Map
     */
    @PutMapping(Constants.V3_URL + "/space-admin")
    public Map renameSpaceQuotaForAdmin(@RequestBody Space space) {
        Map resultMap = spaceServiceV3.renameSpaceQuotaForAdmin(space);

        return resultMap;
    }

    /**
     * 운영자 포털에서 할당량을 변경한다. (Space : Update)
     *
     * @param space
     * @return Map
     */
    @PutMapping(Constants.V3_URL + "/space-quota-admin")
    public Map qutaoSpaceForAdmin(@RequestBody Space space) {
        Map resultMap = spaceServiceV3.qutaoSpaceForAdmin(space);

        return resultMap;
    }

    /**
     * 운영자 포털에서 공간명을 변경한다. (Space : Update)
     *
     * @param space
     * @return Map
     */
    @PutMapping(Constants.V3_URL + "/space-name-admin")
    public Map renameSpaceForAdmin(@RequestBody Space space) {
        Map resultMap = spaceServiceV3.renameSpaceForAdmin(space);
        return resultMap;
    }

}
