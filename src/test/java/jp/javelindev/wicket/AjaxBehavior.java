/*
 * Copyright 2012 Tsutomu YANO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.javelindev.wicket;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 *
 * @author Tsutomu YANO
 */
public class AjaxBehavior extends AbstractAjaxBehavior{

    @Override
    public void onRequest() {
        Response response = RequestCycle.get().getResponse();
        response.write("test");
    }

}
