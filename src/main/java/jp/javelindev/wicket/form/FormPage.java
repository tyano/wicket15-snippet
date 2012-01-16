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
package jp.javelindev.wicket.form;

import java.util.Date;
import jp.javelindev.wicket.FormKey;
import jp.javelindev.wicket.WicketSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
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
public class FormPage extends WebPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormPage.class);
    private int counter = 0;
    private String input;

    public FormPage(PageParameters parameters) {
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

            @Override
            protected void onSubmit() {
                WicketSession session = (WicketSession) getSession();
                if(!session.removeFormKey(key)) {
                    LOGGER.info("DOUBLE SUBMIT.");
                    throw new IllegalStateException("Double submit occurs.");
                } else {
                    LOGGER.info("submitted {} time(s)", ++counter);
                    try {
                        //テストしやすいように待つ
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO 自動生成された catch ブロック
                        e.printStackTrace();
                    }
                    setResponsePage(new NextPage(input, counter));
                }
            }
        };
        add(form);

        form.add(new TextField<String>("input", new PropertyModel<String>(this, "input")));

        add(new Label("label", new PropertyModel<String>(this, "input")));
    }
}
