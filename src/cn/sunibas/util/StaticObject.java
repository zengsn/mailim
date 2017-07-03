package cn.sunibas.util;

import cn.sunibas.entity.TSLanguage;
import cn.sunibas.service.ITSLanguageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/15.
 */
public class StaticObject {
    private static ITSLanguageService itsLanguageService;

    public void setItsLanguageService(ITSLanguageService itsLanguageService) {
        this.itsLanguageService = itsLanguageService;
    }

    private Map<Integer,String> language = null;

    public Map<Integer,String> getLanguage() {
        if (language == null) {
            language = new HashMap<Integer, String>();
            Object o = itsLanguageService.getAll();
            List<TSLanguage> list = itsLanguageService.getAll();
            for (TSLanguage item : list) {
                language.put(item.getId(),item.getName());
            }
        }
        return language;
    }
}
