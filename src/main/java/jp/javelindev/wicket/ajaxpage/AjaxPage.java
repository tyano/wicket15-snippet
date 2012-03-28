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
package jp.javelindev.wicket.ajaxpage;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.resource.filtering.HeaderResponseFilteredResponseContainer;

/**
 *
 * @author Tsutomu YANO
 */
public class AjaxPage extends WebPage {
    private static final long serialVersionUID = 1L;


    private Date displayDate = new Date();

    public AjaxPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Label label = new Label("label", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(displayDate);
            }
        });
        label.setOutputMarkupId(true);
        add(label);

        AjaxLink<Void> link = new AjaxLink<Void>("button") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                setDisplayDate(new Date());
                target.add(label);
            }
        };
        add(link);

        add(new HeaderResponseFilteredResponseContainer("footer", "footerJS"));

    }

    public Date getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(Date displayDate) {
        this.displayDate = displayDate;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(new JavaScriptResourceReference(AjaxPage.class, "test1.js"), "test1");
        response.renderJavaScriptReference(new JavaScriptResourceReference(AjaxPage.class, "test2.js"), "test2");
        response.renderCSSReference(new CssResourceReference(AjaxPage.class, "test1.css"));
        response.renderCSSReference(new CssResourceReference(AjaxPage.class, "test2.css"));
    }

}
