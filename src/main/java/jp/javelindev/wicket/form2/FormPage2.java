/*
 * Copyright 2011 t_yano.
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
package jp.javelindev.wicket.form2;

import java.util.Date;
import jp.javelindev.wicket.FormKey;
import jp.javelindev.wicket.WicketSession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author t_yano
 */
public class FormPage2 extends WebPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormPage2.class);
    private int counter = 0;
    private String input;

    public FormPage2(PageParameters parameters) {
        super(parameters);

        Form<Void> form = new Form<Void>("form") {

            private FormKey key;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                this.key = new FormKey(getPageId(), getId(), new Date());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                WicketSession session = (WicketSession) getSession();
                session.addFormKey(key);
            }
        };
        add(form);

        final TextField<String> text = new TextField<String>("input", new PropertyModel<String>(this, "input"));
        form.add(text);

        final Label label = new Label("label", new PropertyModel<String>(this, "input"));
        label.setOutputMarkupId(true);
        add(label);

        form.add(new Button("notAjaxButton") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onSubmit() {
                super.onSubmit();
            }
        });

        form.add(new AjaxButton("ajaxButton", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(label);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });


    }
}
