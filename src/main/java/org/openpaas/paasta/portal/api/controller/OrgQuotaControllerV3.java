package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v2.organizationquotadefinitions.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Quota;
import org.openpaas.paasta.portal.api.service.OrgQuotaServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@RestController
public class OrgQuotaControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgQuotaControllerV3.class);

    @Autowired
    OrgQuotaServiceV3 orgQuotaServiceV3; // The Org Quota service.

    /**
     * 조직 할당량 리스트를 조회한다.
     *
     * @param token the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/orgs/quota-definitions")
    public ListOrganizationQuotaDefinitionsResponse getOrgQuotaDefinitions(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getOrgQuotaDefinitions Start : ");
        return orgQuotaServiceV3.getOrgQuotaDefinitionsList(token);
    }

    /**
     * 특정 조직의 할당량 정의를 조회한다.
     *
     * @param quotaId The guid of the Organization Quota Definition
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/orgs/quota-definitions/{quotaId}")
    public GetOrganizationQuotaDefinitionResponse getOrgQuotaDefinition(@PathVariable String quotaId,  @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getOrgQuotaDefinition Start : ");
        return orgQuotaServiceV3.getOrgQuotaDefinitions(quotaId, token);
    }

    /**
     * 조직 할당량 리스트를 등록한다.
     *
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL + "/orgs/quota-definitions")
    public CreateOrganizationQuotaDefinitionResponse createOrgQuotaDefinitions(@RequestBody Quota quota, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("createOrgQuotaDefinitions Start : ");
        return orgQuotaServiceV3.createOrgQuotaDefinitions(quota, token);
    }

    /**
     * 해당 조직 할당량 정의를 수정한다.
     *
     * @param quotaId The guid of the Organization Quota Definition
     * @param quota   Quota Info
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/orgs/quota-definitions/{quotaId}")
    public UpdateOrganizationQuotaDefinitionResponse updateOrgQuotaDefinitions(@PathVariable String quotaId, @RequestBody Quota quota, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("updateOrgQuotaDefinitions Start : ");
        quota.setGuid(UUID.fromString(quotaId));
        return orgQuotaServiceV3.updateOrgQuotaDefinitions(quota, token);
    }

    /**
     * 해당 조직 할당량 정의를 삭제한다.
     *
     * @param quotaId The guid of the Organization Quota Definition
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/orgs/quota-definitions/{quotaId}")
    public DeleteOrganizationQuotaDefinitionResponse deleteOrgQuotaDefinitions(@PathVariable String quotaId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("deleteOrgQuotaDefinitions Start : ");
        return orgQuotaServiceV3.deleteOrgQuotaDefinitions(quotaId, token);
    }

    /**
     * 해당 조직 할당량 정의를 지정한다.
     *
     * @param quota Quota Info
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @PutMapping(Constants.V3_URL + "/orgs/quota-definitions/associations")
    public boolean setOrgQuotaDefinitions(@RequestBody Quota quota, HttpServletRequest request) throws Exception {
        // Name : 사용자가 입력하는 값이기 떄문에 URL 값으로 받지 않음
        LOGGER.info("setOrgQuotaDefinitions Start : ");
        orgQuotaServiceV3.setOrgQuotaDefinitions(quota);
        return true;
    }
}
