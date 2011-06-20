/*
 * Copyright 2011 Tsutomu YANO.
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
package jp.javelindev.wicket.dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.wicket.IEventDispatcher;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.event.IEventSink;

/**
 *
 * @author Tsutomu YANO
 */
public class AnnotationEventDispatcher implements IEventDispatcher {

    @Override
    public void dispatchEvent(IEventSink es, IEvent<?> event) {
        Class<? extends IEventSink> sinkClass = es.getClass().asSubclass(IEventSink.class);
        Object payload = event.getPayload();
        Class<?> payloadClass = payload.getClass();
        
        if (payload != null) {
            for (Method method : sinkClass.getMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if(paramTypes.length == 1 && paramTypes[0].isAssignableFrom(payloadClass)) {
                        try {
                            method.invoke(es, payload);
                        } catch (IllegalAccessException ex) {
                            throw new IllegalStateException("Could not access to the method. EventHandler must be a public method.", ex);
                        } catch (InvocationTargetException ex) {
                            throw new RuntimeException("underlying method thrown a exception. see the stack trace.", ex);
                        }
                    }
                }
            }
        }
    }
}
