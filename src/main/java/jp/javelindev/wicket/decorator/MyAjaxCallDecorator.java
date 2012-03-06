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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;

/**
 *
 * @author Tsutomu YANO
 */
public class MyAjaxCallDecorator extends AjaxCallDecorator {
    private static final long serialVersionUID = 1L;

    @Override
    public CharSequence decorateOnSuccessScript(Component c, CharSequence script) {
        return "alert(Wicket.$$(this));";
    }

    @Override
    public CharSequence decorateScript(Component c, CharSequence script) {
       StringBuilder builder = new StringBuilder();

       builder.append("var hoge = function() {");
       builder.append(script);
       builder.append("};");
       builder.append("setTimeout(hoge, 10)");
       return builder.toString();
    }
}
