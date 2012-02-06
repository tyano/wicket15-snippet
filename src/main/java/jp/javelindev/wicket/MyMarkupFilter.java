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

import java.text.ParseException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class MyMarkupFilter extends AbstractMarkupFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMarkupFilter.class);

    private int count = 0;
    private final MarkupResourceStream markup;

    public MyMarkupFilter(MarkupResourceStream resource) {
        this.markup = resource;
    }

    @Override
    protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {
        String namespace = markup.getWicketNamespace();
        final String wicketIdValue = tag.getAttributes().getString(namespace + ":id");

        //wicketのタグでなく、wicket:idもついてない場合は、強制的にwicket:idを付けてみる
        if (!namespace.equalsIgnoreCase(tag.getNamespace()) && Strings.isEmpty(wicketIdValue)) {
            tag.put("wicket:id", "dummy" + count++);
        }
        LOGGER.info(tag.toString());

        return tag;
    }
}
