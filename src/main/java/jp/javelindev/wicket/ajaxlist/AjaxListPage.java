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
package jp.javelindev.wicket.ajaxlist;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.filtering.HeaderResponseFilteredResponseContainer;
import org.apache.wicket.response.StringResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class AjaxListPage extends WebPage {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AjaxListPage.class);

    public AjaxListPage(PageParameters parameters) {
        super(parameters);

        final RepeatingView view = new RepeatingView("item");
        add(view);

        Label defaultLabel = new Label(view.newChildId(), "test");
        defaultLabel.setOutputMarkupId(true);
        view.add(defaultLabel);

        add(new AjaxLink<Void>("addNew") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                Label newLabel = new Label(view.newChildId(), new SimpleDateFormat("HH:mm:ss").format(new Date()));
                newLabel.setOutputMarkupId(true);
                view.add(newLabel);

                Response bodyResonse = new StringResponse();
                Response originalResponse = getResponse();

                getRequestCycle().setResponse(bodyResonse);
                newLabel.render();
                String componentString = bodyResonse.toString();
                LOGGER.info("label: {}", componentString);
                getRequestCycle().setResponse(originalResponse);

                target.appendJavaScript("$('#viewContainer').append('" + componentString + "')");
            }
        });

        add(new HeaderResponseFilteredResponseContainer("footerJS", "footerJS"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(new JavaScriptResourceReference(AjaxListPage.class, "jquery-1.6.2.js"));
    }
}
