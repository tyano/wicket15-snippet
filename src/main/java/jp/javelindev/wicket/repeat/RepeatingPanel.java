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
package jp.javelindev.wicket.repeat;

import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class RepeatingPanel extends Panel {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepeatingPanel.class);

    public RepeatingPanel(String id, IModel<? extends List<? extends String>> model) {
        super(id, model);
        construct();
    }

    public RepeatingPanel(String id) {
        super(id);
        construct();
    }

    @SuppressWarnings("unchecked")
    public IModel<? extends List<String>> getModel() {
        return (IModel<? extends List<String>>) getDefaultModel();
    }

    private void construct() {
        add(new ListView<String>("repeatingList", getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                setReuseItems(true);
            }

            @Override
            protected void populateItem(final ListItem<String> item) {
                item.setOutputMarkupId(true);
                item.add(new Label("content", item.getModelObject().toString()));
            }
        });

        final WebMarkupContainer next = new WebMarkupContainer("next");
        next.setOutputMarkupId(true);
        next.setOutputMarkupPlaceholderTag(true);
        next.setVisible(false);
        add(next);

        final WebMarkupContainer container = new WebMarkupContainer("container");
//        container.setOutputMarkupPlaceholderTag(true);
        container.setOutputMarkupId(true);
        add(container);

        AjaxLink<Void> button = new AjaxLink<Void>("repeatingButton") {
            private static final long serialVersionUID = 1L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                LOGGER.info("clicked");
                Panel panel = new RepeatingPanel(next.getId(), Model.ofList(Arrays.asList("1", "2", "3")));
                next.replaceWith(panel);
                container.setVisible(false);
                target.add(container, panel);
            }
        };
        button.setOutputMarkupId(true);
        container.add(button);
    }
}
