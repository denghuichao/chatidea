package com.github.denghuichao.chatcode.util;

import org.commonmark.node.Node;
import org.commonmark.parser.IncludeSourceSpans;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class HtmlUtils {

    private static final Parser parser = Parser.builder().includeSourceSpans(IncludeSourceSpans.BLOCKS_AND_INLINES).build();

    public static String md2Html(String md) {
        Node document = parser.parse(md);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public static String addLineBreaks(String text) {
        return text.replaceAll("\n", "<br/>");
    }
}