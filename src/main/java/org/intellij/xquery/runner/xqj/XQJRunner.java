/*
 * Copyright 2013 Grzegorz Ligas <ligasgr@gmail.com>
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
package org.intellij.xquery.runner.xqj;

import javax.xml.xquery.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * User: ligasgr
 * Date: 15/08/13
 * Time: 13:56
 */
public class XQJRunner {

    public static void main(String[] args) throws XQException, FileNotFoundException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        Class dsClass = Class.forName("net.xqj.marklogic.MarkLogicXQDataSource");
//        Class dsClass = Class.forName("net.sf.saxon.xqj.SaxonXQDataSource");
        XQDataSource xqs = (XQDataSource) dsClass.newInstance();
        xqs.setProperty("serverName", "localhost");
        xqs.setProperty("port", "8003");

        XQConnection conn = xqs.getConnection("admin", "admin");
//        XQConnection conn = xqs.getConnection();

        XQPreparedExpression xqpe = conn.prepareExpression(new FileInputStream(args[0]));

        XQResultSequence rs = xqpe.executeQuery();

        Properties props = new Properties();
        props.setProperty("method", "text");
        rs.writeSequence(System.out, props);


        conn.close();
    }
}
