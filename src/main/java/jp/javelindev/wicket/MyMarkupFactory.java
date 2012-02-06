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
package jp.javelindev.wicket;

import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.IMarkupFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class MyMarkupFactory extends MarkupFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMarkupFactory.class);

    public MyMarkupFactory() {
    }

    @Override
    public MarkupParser newMarkupParser(MarkupResourceStream resource) {
        MarkupParser parser = super.newMarkupParser(resource);

//下記コメント行をコメント解除すると、すべての非wicketタグに強制的にwicket:idを振り、強制的にコンポーネント化する。
//ただし、wicket:idに対応するコンポーネントをaddしてないので、エラーになります。
//        parser.getMarkupFilters().add(0, new MyMarkupFilter(resource));

        for (IMarkupFilter filter : parser.getMarkupFilters()) {
            LOGGER.info("filter: {}", filter.getClass().getName());
        }

        return parser;
    }
}
