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
package jp.javelindev.wicket.decorator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

/**
 *
 * @author Tsutomu YANO
 */
public class DecoratorPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public DecoratorPage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextField<String> userinput = new TextField<String>("userinput", Model.of(""));
        userinput.add(new AjaxFormSubmitBehavior("onchange") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                super.getAjaxCallDecorator();
                return new MyAjaxCallDecorator();
            }
        });

        Form<Void> form = new Form<Void>("form");
        add(form);

        form.add(userinput);

        AjaxRequestTarget.get();
        getPageParameters();
        
    }
}
