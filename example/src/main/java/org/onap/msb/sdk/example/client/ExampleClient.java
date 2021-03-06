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
package org.onap.msb.sdk.example.client;

import java.io.IOException;

import org.onap.msb.sdk.example.common.Animal;
import org.onap.msb.sdk.httpclient.RestServiceCreater;
import org.onap.msb.sdk.httpclient.msb.MSBServiceClient;


public class ExampleClient {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // For real use case, MSB discovery IP and Port should come from configuration file instead
        // of hard code here
        String msb_discovery_ip = "10.96.33.44";
        int msb_discovery_port = 30080;

        MSBServiceClient msbClient = new MSBServiceClient(msb_discovery_ip, msb_discovery_port);

        RestServiceCreater restServiceCreater = new RestServiceCreater(msbClient);

        AnimalServiceClient implProxy = restServiceCreater.createService(AnimalServiceClient.class);

        Animal animal = implProxy.queryAnimal("panda").execute().body();
        System.out.println("animal:" + animal);
    }

}
