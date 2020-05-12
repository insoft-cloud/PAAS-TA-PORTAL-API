package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.uaa.clients.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.ClientServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by CissC2 on 2017-03-20.
 */
@RestController
public class ClientControllerV2 extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientControllerV2.class);

    @Autowired
    private ClientServiceV2 clientServiceV2;

    /**
     * 클라이언트 목록 조회
     *
     * @param
     * @return Map client list
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V2_URL + "/clients"})
    public ListClientsResponse getClientList() throws Exception {
        return clientServiceV2.getClientList();
    }

    /**
     * 클라이언트 상세 정보 조회
     *
     * @param clientId clientId
     * @return Map client
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V2_URL + "/clients/{clientId}"})
    public GetClientResponse getClient(@PathVariable String clientId) throws Exception {
        return clientServiceV2.getClient(clientId);
    }

    /**
     * 클라이언트 등록
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @PostMapping(value = {Constants.V2_URL + "/clients"})
    public CreateClientResponse registerClient(@RequestBody Map<String, Object> param) throws Exception {
        return clientServiceV2.registerClient(param);
    }

    /**
     * 클라이언트 수정
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @PutMapping(value = {Constants.V2_URL + "/clients"})
    public UpdateClientResponse updateClient(@RequestBody Map<String, Object> param) throws Exception {
        return clientServiceV2.updateClient(param);
    }

    /**
     * 클라이언트 삭제
     *
     * @param clientId
     * @return Map map
     * @throws Exception the exception
     */
    @DeleteMapping(value = {Constants.V2_URL + "/clients/{clientId}"})
    public DeleteClientResponse deleteClient(@PathVariable String clientId) throws Exception {
        return clientServiceV2.deleteClient(clientId);
    }

}