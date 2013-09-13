package org.intellij.xquery.formatter;

/**
 * User: ligasgr
 * Date: 12/09/13
 * Time: 16:52
 */
public class XQueryFormatterSpacesTest extends XQueryFormattingModelBuilderTest {
    @Override
    protected String getTestDataPath() {
        return "src/test/testData/org/intellij/xquery/formatter/spaces";
    }

    public void testSpaceAroundAssignmentOperators() {
        getSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = false;
        executeTest();
    }

    public void testSpaceAroundEqualityOperators() {
        getSettings().SPACE_AROUND_EQUALITY_OPERATORS = false;
        executeTest();
    }

    public void testSpaceAroundRelationalOperators() {
        getSettings().SPACE_AROUND_RELATIONAL_OPERATORS = false;
        executeTest();
    }

    public void testSpaceAroundAdditiveOperators() {
        getSettings().SPACE_AROUND_ADDITIVE_OPERATORS = false;
        executeTest();
    }

    public void testSpaceAroundMultiplicativeOperators() {
        getSettings().SPACE_AROUND_MULTIPLICATIVE_OPERATORS= true;
        executeTest();
    }

    public void testSpaceBeforeAfterComma() {
        getSettings().SPACE_BEFORE_COMMA = true;
        getSettings().SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
        executeTest();
    }

    public void testSpaceBeforeAndAfterArgumentAndParamList() {
        executeTest();
    }

    public void testSpaceAroundKeyword() {
        executeTest();
    }

    public void testSpaceAroundAssignmentInModuleDeclAndFirstDecl() {
        getXQuerySettings().SPACE_AROUND_ASSIGNMENT_IN_PROLOG = false;
        executeTest();
    }

    public void testSpaceAroundAssignmentInXmlAttribute() {
        getXQuerySettings().SPACE_AROUND_ASSIGNMENT_IN_XML_ATTRIBUTE = false;
        executeTest();
    }

    public void testSpaceBeforeIfParentheses() {
        getSettings().SPACE_BEFORE_IF_PARENTHESES = false;
        executeTest();
    }

    public void testSpaceBeforeSwitchParentheses() {
        getSettings().SPACE_BEFORE_SWITCH_PARENTHESES = false;
        executeTest();
    }

    public void testSpaceBeforeTypeswitchParentheses() {
        getXQuerySettings().SPACE_BEFORE_TYPESWITCH_PARENTHESES = false;
        executeTest();
    }

    public void testSpaceBeforeTypeTestParentheses() {
        getXQuerySettings().SPACE_BEFORE_TYPE_TEST_PARENTHESES = true;
        executeTest();
    }

    public void testSpaceBeforeFunctionDeclarationParentheses() {
        getXQuerySettings().SPACE_BEFORE_FUNCTION_DECLARATION_PARENTHESES = true;
        executeTest();
    }

    public void testSpaceBeforeFunctionCallParentheses() {
        getXQuerySettings().SPACE_BEFORE_FUNCTION_CALL_PARENTHESES = true;
        executeTest();
    }
}
