/**
 * Copyright 2017-2018 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onap.msb.sdk.discovery;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.msb.sdk.discovery.common.RouteException;
import org.onap.msb.sdk.discovery.entity.MicroServiceFullInfo;
import org.onap.msb.sdk.discovery.entity.MicroServiceInfo;
import org.onap.msb.sdk.discovery.entity.NodeInfo;
import org.onap.msb.sdk.discovery.util.HttpClientUtil;
import org.onap.msb.sdk.discovery.util.JacksonJsonUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientUtil.class})
@PowerMockIgnore("jdk.internal.reflect.*")

public class MSBServiceTest {
    private static final String MOCK_MSB_URL_REG_UPDATE_TRUE =
                    "http://127.0.0.1:10081/api/microservices/v1/services?createOrUpdate=true";
    private static final String MOCK_MSB_URL_REG_UPDATE_FALSE =
                    "http://127.0.0.1:10081/api/microservices/v1/services?createOrUpdate=false";
    private static final String MOCK_MSB_URL_DIS =
                    "http://127.0.0.1:10081/api/microservices/v1/services/aai/version/v8?ifPassStatus=true";
    private static final String MOCK_MSB_URL_UNREG =
                    "http://127.0.0.1:10081/api/microservices/v1/services/aai/version/v8";

    private static final String MOCK_REG_SERVICE_JSON =
                    "{\"serviceName\":\"aai\",\"version\":\"v8\",\"url\":\"/aai/v8\",\"protocol\":\"REST\",\"visualRange\":\"1\",\"lb_policy\":\"\",\"path\":\"/aai/v8\",\"nodes\":[{\"ip\":\"10.74.44.1\",\"port\":\"8443\",\"ttl\":\"20\",\"checkType\":\"HTTP\",\"checkUrl\":\"\",\"checkInterval\":\"10s\",\"checkTimeOut\":\"10s\"}],\"metadata\":[{\"key\":\"key1\",\"value\":\"value1\"}],\"enable_ssl\":false}";

    @Test
    public void test_registration_update_true() throws RouteException {
        String msbAddress = "127.0.0.1:10081";
        MicroServiceInfo microServiceInfo =
                        (MicroServiceInfo) JacksonJsonUtil.jsonToBean(MOCK_REG_SERVICE_JSON, MicroServiceInfo.class);
        MicroServiceFullInfo microServiceFullInfo = mockMicroServiceFullInfo(microServiceInfo);
        System.out.println(microServiceFullInfo.toString());
        System.out.println(microServiceInfo.toString());
        mockHttpPost(MOCK_MSB_URL_REG_UPDATE_TRUE, JacksonJsonUtil.beanToJson(microServiceFullInfo));
        MSBService msbService = new MSBService();
        microServiceFullInfo = msbService.registerMicroServiceInfo(msbAddress, microServiceInfo);
        Assert.assertTrue(microServiceFullInfo.getPath().equals("/aai/v8"));
        Assert.assertTrue(microServiceFullInfo.getProtocol().equals("REST"));
        Assert.assertTrue(microServiceFullInfo.getServiceName().equals("aai"));
        Assert.assertTrue(microServiceFullInfo.getUrl().equals("/aai/v8"));
        Assert.assertTrue(microServiceFullInfo.getVersion().equals("v8"));
        Assert.assertTrue(microServiceFullInfo.getVisualRange().equals("1"));
    }

    @Test
    public void test_registration_update_false() throws RouteException {
        String msbAddress = "127.0.0.1:10081";
        MicroServiceInfo microServiceInfo =
                        (MicroServiceInfo) JacksonJsonUtil.jsonToBean(MOCK_REG_SERVICE_JSON, MicroServiceInfo.class);
        MicroServiceFullInfo microServiceFullInfo = mockMicroServiceFullInfo(microServiceInfo);
        mockHttpPost(MOCK_MSB_URL_REG_UPDATE_FALSE, JacksonJsonUtil.beanToJson(microServiceFullInfo));
        MSBService msbService = new MSBService();
        microServiceFullInfo = msbService.registerMicroServiceInfo(msbAddress, microServiceInfo, false);
        Assert.assertTrue(microServiceFullInfo.getPath().equals("/aai/v8"));
        Assert.assertTrue(microServiceFullInfo.getProtocol().equals("REST"));
        Assert.assertTrue(microServiceFullInfo.getServiceName().equals("aai"));
        Assert.assertTrue(microServiceFullInfo.getUrl().equals("/aai/v8"));
        Assert.assertTrue(microServiceFullInfo.getVersion().equals("v8"));
        Assert.assertTrue(microServiceFullInfo.getVisualRange().equals("1"));
    }

    @Test
    public void test_discovery() throws RouteException {
        String msbAddress = "127.0.0.1:10081";
        MicroServiceInfo microServiceInfo =
                        (MicroServiceInfo) JacksonJsonUtil.jsonToBean(MOCK_REG_SERVICE_JSON, MicroServiceInfo.class);
        MicroServiceFullInfo microServiceFullInfo = mockMicroServiceFullInfo(microServiceInfo);
        mockHttpGet(MOCK_MSB_URL_DIS, JacksonJsonUtil.beanToJson(microServiceFullInfo));

        MSBService msbService = new MSBService();
        microServiceFullInfo = msbService.queryMicroServiceInfo(msbAddress, "aai", "v8");
        Assert.assertTrue(microServiceFullInfo.getPath().equals("/aai/v8"));
        Assert.assertTrue(microServiceFullInfo.getProtocol().equals("REST"));
        Assert.assertTrue(microServiceFullInfo.getServiceName().equals("aai"));
        Assert.assertTrue(microServiceFullInfo.getUrl().equals("/aai/v8"));
        Assert.assertTrue(microServiceFullInfo.getVersion().equals("v8"));
        Assert.assertTrue(microServiceFullInfo.getVisualRange().equals("1"));
    }

    @Test
    public void test_unregistration() throws Exception {
        String msbAddress = "127.0.0.1:10081";
        mockHttpDel();
        MSBService msbService = new MSBService();
        msbService.cancelMicroServiceInfo(msbAddress, "aai", "v8");
    }

    @Test
    public void test_unregistration_a_instance() throws Exception {
        String msbAddress = "127.0.0.1:10081";
        mockHttpDel();
        MSBService msbService = new MSBService();
        msbService.cancelMicroServiceInfo(msbAddress, "aai", "v8", "10.74.44.1", "8443");
    }

    @Test
    public void test_healthCheckbyTTL() {
        try {
            PowerMockito.mockStatic(HttpClientUtil.class);
            MSBService msbService = new MSBService();
            msbService.healthCheckbyTTL("127.0.0.1:10081", "aai", "v8", "10.74.44.1", "8443");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RouteException);
        }
    }

    private MicroServiceFullInfo mockMicroServiceFullInfo(MicroServiceInfo info) {
        MicroServiceFullInfo serviceInfo = new MicroServiceFullInfo();
        serviceInfo.setServiceName(info.getServiceName());
        serviceInfo.setVersion(info.getVersion());
        serviceInfo.setUrl(info.getUrl());
        serviceInfo.setProtocol(info.getProtocol());
        serviceInfo.setVisualRange(info.getVisualRange());
        serviceInfo.setLb_policy(info.getLb_policy());
        serviceInfo.setPath(info.getPath());

        Set<NodeInfo> nodes = new HashSet<NodeInfo>();
        NodeInfo node = new NodeInfo();
        node.setCreated_at(new Date());
        node.setExpiration(new Date());
        node.setIp("10.74.44.1");
        node.setNodeId("10.74.44.1_8443");
        node.setPort("8443");
        node.setStatus("1");
        node.setTtl("20");
        node.setUpdated_at(new Date());

        nodes.add(node);
        serviceInfo.setNodes(nodes);
        return serviceInfo;
    }


    private void mockHttpPost(String mockMSBUrl, String mockServiceInfoJson) throws RouteException {
        PowerMockito.mockStatic(HttpClientUtil.class);
        PowerMockito.when(HttpClientUtil.httpPostWithJSON(mockMSBUrl, MOCK_REG_SERVICE_JSON))
                        .thenReturn(mockServiceInfoJson);
    }

    private void mockHttpGet(String mockMSBUrl, String mockServiceInfoJson) throws RouteException {
        PowerMockito.mockStatic(HttpClientUtil.class);
        PowerMockito.when(HttpClientUtil.httpGet(mockMSBUrl)).thenReturn(mockServiceInfoJson);
    }

    private void mockHttpDel() throws Exception {
        PowerMockito.mockStatic(HttpClientUtil.class);

    }
}
