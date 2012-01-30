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

import java.util.Arrays;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Tsutomu YANO
 */
public class MovableListPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public MovableListPage(PageParameters parameters) {
        super(parameters);
        construct();
    }

    private void construct() {
        WebMarkupContainer container = new WebMarkupContainer("listContainer");
        add(container);

        container.add(new MovableList<String>("item", Arrays.asList("テスト1", "テスト2")) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("content", item.getModel()).setRenderBodyOnly(true));
            }
        });
    }
}
