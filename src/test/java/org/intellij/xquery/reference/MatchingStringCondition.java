/*
 * Copyright 2013 Grzegorz Ligas <ligasgr@gmail.com> and other contributors (see the CONTRIBUTORS file).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.intellij.xquery.reference;

import com.intellij.openapi.util.Condition;

/**
* User: ligasgr
* Date: 15/08/13
* Time: 00:07
*/
public class MatchingStringCondition implements Condition<String> {
    private String matchingText;

    public MatchingStringCondition(String matchingText) {
        this.matchingText = matchingText;
    }

    @Override
    public boolean value(String text) {
        return matchingText.equals(text);
    }
}
