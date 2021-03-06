/*
 * Copyright 2011 Tsutomu YANO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package jp.javelindev.wicket.bookmarkable;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Tsutomu YANO
 */
@SuppressWarnings("serial")
public class SamplePage extends WebPage {
    public SamplePage(PageParameters parameters) {
        super(parameters);
        
        add(new Label("time", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return new SimpleDateFormat("HH:mm:ss").format(new Date());
            }
        }));
        
        add(new BookmarkablePageLink<Void>("link", SamplePage.class));
    }
}
