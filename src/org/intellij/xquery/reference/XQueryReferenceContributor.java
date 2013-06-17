/*
 * Copyright 2013 Grzegorz Ligas
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

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.intellij.xquery.psi.XQueryVarRefName;
import org.jetbrains.annotations.NotNull;

/**
 * User: ligasgr
 * Date: 08/06/13
 * Time: 22:24
 */
public class XQueryReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XQueryVarRefName.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        XQueryVarRefName varRef = (XQueryVarRefName) element;
                        String text = varRef.getText();
                        if (text != null) {
                            return new PsiReference[]{new XQueryVarNameReference(element, new TextRange(0, text.length()))};
                        }
                        return new PsiReference[0];
                    }
                });
    }
}
