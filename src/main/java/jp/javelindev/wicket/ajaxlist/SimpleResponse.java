/*
 * Copyright 2011 Tsutomu YANO.
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
package jp.javelindev.wicket.ajaxlist;

import org.apache.wicket.request.Response;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

/**
 *
 * @author Tsutomu YANO
 */
public class SimpleResponse extends Response {

    private final AppendingStringBuffer buffer = new AppendingStringBuffer(256);
    private boolean escaped = false;
    private final Response originalResponse;

    public SimpleResponse(Response originalResponse) {
        this.originalResponse = originalResponse;
    }

    @Override
    public void write(CharSequence cs) {
        String string = cs.toString();
        if (needsEncoding(string)) {
            string = encode(string);
            escaped = true;
            buffer.append(string);
        } else {
            buffer.append(cs);
        }
    }

    @Override
    public void write(byte[] array) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeURL(CharSequence url) {
        return originalResponse.encodeURL(url);
    }

    @Override
    public Object getContainerResponse() {
        return originalResponse.getContainerResponse();
    }

    /**
     * @return contents of the response
     */
    public CharSequence getContents() {
        return buffer;
    }

    /**
     * @return true if any escaping has been performed, false otherwise
     */
    public boolean isContentsEncoded() {
        return escaped;
    }

    /**
     * 
     * @param str
     * @return true if string needs to be encoded, false otherwise
     */
    protected boolean needsEncoding(CharSequence str) {
        /*
         * TODO Post 1.2: Ajax: we can improve this by keeping a buffer of at least 3 characters and
         * checking that buffer so that we can narrow down escaping occurring only for ']]>'
         * sequence, or at least for ]] if ] is the last char in this buffer.
         * 
         * but this improvement will only work if we write first and encode later instead of working
         * on fragments sent to write
         */
        return Strings.indexOf(str, ']') >= 0;
    }

    /**
     * Encodes a string so it is safe to use inside CDATA blocks
     * 
     * @param str
     * @return encoded string
     */
    protected String encode(CharSequence str) {
        if (str == null) {
            return null;
        }

        return Strings.replaceAll(str, "]", "]^").toString();
    }
}
