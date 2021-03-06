/*******************************************************************************
 * Copyright 2017 ZTE, Inc. and others.
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
 ******************************************************************************/
package org.onap.msb.sdk.example.server;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import org.jvnet.hk2.annotations.Service;
import org.onap.msb.sdk.discovery.entity.MicroServiceInfo;
import org.onap.msb.sdk.discovery.entity.Node;
import org.onap.msb.sdk.httpclient.msb.MSBServiceClient;

public class MsbHelper {

    private MSBServiceClient msbClient;

    public MsbHelper(MSBServiceClient msbClient) {
        super();
        this.msbClient = msbClient;
    }



    public void registerMsb() throws Exception {
        MicroServiceInfo msinfo = new MicroServiceInfo();
        msinfo.setServiceName("animals");
        msinfo.setVersion("v1");
        msinfo.setUrl("/api/rpc/v1");
        msinfo.setProtocol("REST");
        msinfo.setVisualRange("0|1");

        Set<Node> nodes = new HashSet<>();
        Node node1 = new Node();
        node1.setIp(InetAddress.getLocalHost().getHostAddress());
        node1.setPort("9090");
        node1.setCheckType("HTTP");
        node1.setCheckUrl("http://10.74.57.151:9090/api/rpc/v1/animals/panda");
        nodes.add(node1);
        msinfo.setNodes(nodes);
        msbClient.registerMicroServiceInfo(msinfo, false);
    }
}
