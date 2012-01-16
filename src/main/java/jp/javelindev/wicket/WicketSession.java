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

import java.util.HashSet;
import java.util.Set;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 *
 * @author Tsutomu YANO
 */
public class WicketSession extends WebSession {

    private Set<FormKey> formKeySet = new HashSet<FormKey>();

    public WicketSession(Request request) {
        super(request);
    }

    public boolean addFormKey(FormKey key) {
        return formKeySet.add(key);
    }

    public boolean removeFormKey(FormKey key) {
        return formKeySet.remove(key);
    }
}
