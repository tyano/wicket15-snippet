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
package jp.javelindev.wicket.movablelist;

import java.util.List;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

/**
 *
 * @author Tsutomu YANO
 */
public abstract class MovableList<T> extends ListView<T> {
    private static final long serialVersionUID = 1L;

    private MarkupContainer container;

    public MovableList(String id, List<? extends T> list) {
        super(id, list);
    }

    public MovableList(String id, IModel<? extends List<? extends T>> model) {
        super(id, model);
    }

    public MovableList(String id) {
        super(id);
    }

    public MovableList(String id, List<? extends T> list, MarkupContainer container) {
        super(id, list);
        this.container = container;
    }

    public MovableList(String id, IModel<? extends List<? extends T>> model, MarkupContainer container) {
        super(id, model);
        this.container = container;
    }

    public MovableList(String id, MarkupContainer container) {
        super(id);
        this.container = container;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js", "jquery-reference");
        response.renderJavaScriptReference("https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js", "jquery-ui-reference");
        response.renderJavaScript("jQuery.noConflict();", "jquery-noconflict");

        response.renderOnDomReadyJavaScript(
                "jQuery('#" + this.container.getMarkupId() + "').sortable();"
                );
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if(this.container == null) {
            this.container = getParent();
        }

        this.container.setOutputMarkupId(true);
    }
}
