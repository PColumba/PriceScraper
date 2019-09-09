import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {

    private final String elementSelector;
    private final String elementTextPattern;
    private final boolean elementChildText;
    private final String attributeName;
    private final String attributeTextPattern;
    private final boolean extractLastElement;

    Extractor(String elementSelector, String elementTextPattern, boolean elementChildText, String attributeName,
                     String attributeTextPattern, boolean extractLastElement){
        this.elementSelector = elementSelector;
        this.elementTextPattern = elementTextPattern;
        this.elementChildText = elementChildText;
        this.attributeName = attributeName;
        this.attributeTextPattern = attributeTextPattern;
        this.extractLastElement = extractLastElement;
    }

    public Extractor(String elementSelector){
        this.elementSelector = elementSelector;
        this.elementTextPattern = null;
        this.elementChildText = false;
        this.attributeName = null;
        this.attributeTextPattern = null;
        this.extractLastElement = false;
    }

    String extract(Element doc){
        return extractLastElement ? extractFromElement(doc.select(elementSelector).last()) : extractFromElement(doc.selectFirst(elementSelector));
    }

    Elements getAll(Element doc){
        return doc.select(elementSelector);
    }

    private String getFirstMatch(String str, String pattern) {
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(str);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String extractFromElement(Element el){
        if(el == null)
            return null;
        if(attributeName != null) {
            String unmodifiedText = el.attributes().get(attributeName);
            return attributeTextPattern != null ? getFirstMatch(unmodifiedText, attributeTextPattern) : unmodifiedText;
        } else {
            String unmodifiedText = elementChildText ? el.text() : el.ownText();
            return elementTextPattern != null ? getFirstMatch(unmodifiedText, elementTextPattern) : unmodifiedText;
        }
    }

    String getElementSelector() {
        return elementSelector;
    }

    String getElementTextPattern() {
        return elementTextPattern;
    }

    boolean isElementChildText() {
        return elementChildText;
    }

    String getAttributeName() {
        return attributeName;
    }

    String getAttributeTextPattern() {
        return attributeTextPattern;
    }

    @Override
    public String toString(){
        return "element selector: " + elementSelector +
                ", element text pattern: " + elementTextPattern +
                ", element include child text: " + elementChildText +
                ", attribute name: " + attributeName +
                ", attribute text pattern: " + attributeTextPattern;
    }

}
