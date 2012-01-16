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

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Tsutomu YANO
 */
public class FormKey implements Serializable {
    private int pageId;
    private String formId;
    private Date date;

    public FormKey(int pageId, String formId, Date date) {
        this.pageId = pageId;
        this.formId = formId;
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FormKey other = (FormKey) obj;
        if (this.pageId != other.pageId) {
            return false;
        }
        if ((this.formId == null) ? (other.formId != null) : !this.formId.equals(other.formId)) {
            return false;
        }
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.pageId;
        hash = 37 * hash + (this.formId != null ? this.formId.hashCode() : 0);
        hash = 37 * hash + (this.date != null ? this.date.hashCode() : 0);
        return hash;
    }
}
