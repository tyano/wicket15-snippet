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
package jp.javelindev.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 *
 * @author Tsutomu YANO
 */
public class BehaviorPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public BehaviorPage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new AbstractAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onRequest() {
                RequestCycle.get().getResponse().write("test");
            }

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.renderJavaScript("var callback = '" + getCallbackUrl() + "';", "behavior");
            }
        });

        add(new AjaxLink("link") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
            }


        });

        add(new Link<Void>("link2") {
            private static final long serialVersionUID = 1L;

            @Override
            protected CharSequence getOnClickScript(CharSequence url) {
                return "wicketAjaxGet(callback, function(value) { Wicket.$('test').innerHTML = value; }, function() { alert('error'); }, null, null); return false;";
            }

            @Override
            public void onClick() {
            }
        });
    }
}
