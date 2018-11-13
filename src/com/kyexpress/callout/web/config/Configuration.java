package com.kyexpress.callout.web.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.kyexpress.callout.web.util.StringUtil;


public class Configuration {
    private static Logger logger = Logger.getLogger(Configuration.class);

    private static Map<String,String> items = new HashMap<String,String> ();

    private static String CONFIG_FILE_NAME = "configuration.xml";

    @SuppressWarnings("unchecked")
    private static void loadConfig() {
        try {
            InputStream inputStream = Configuration.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            if (document != null) {
                Element systemElement = document.getRootElement();
                List<Element> catList = systemElement.elements("category");
                Iterator<Element> catIter = catList.iterator();              
                while (catIter.hasNext()) {
                    Element catElement =  catIter.next();
                    String catName = catElement.attributeValue("name");
                    if (!StringUtil.isEmpty(catName)) {
                        List<Element> itemList     = catElement.elements("item");
                        Iterator<Element> itemIter = itemList.iterator();
                        while (itemIter.hasNext()) {
                            Element itemElement = itemIter.next();
                            String itemName = itemElement.attributeValue("name");
                            String value = itemElement.attributeValue("value");
                            if (!StringUtil.isEmpty(itemName)) {
                                items.put(catName + "." + itemName, value);
                            }
                        }

                    }
                }
            }

        } catch (Exception e) {
            logger.error("读入配置文件出错", e);
        } finally {
            
        }
    }

    public static String getString(String name) {
        String value = items.get(name);
        return value == null ? "" : value;
    }

    public static String getString(String name, String defaultValue) {
        String value = items.get(name);
        if ((value != null) && (value.length() > 0)) {
            return value;
        }
        return defaultValue;
    }

    public static Map<String, String> getItems() {
        return items;
    }

    static {
        loadConfig();
    }
}
