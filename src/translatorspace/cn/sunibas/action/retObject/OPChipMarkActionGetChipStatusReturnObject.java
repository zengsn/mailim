package cn.sunibas.action.retObject;

import cn.sunibas.action.retObject.abstractClass.CommonProto;
import cn.sunibas.redis.RedisFroTSmark;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by sunbing on 17-3-6.
 */
public class OPChipMarkActionGetChipStatusReturnObject extends CommonProto {
    Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void check(RedisFroTSmark redisFroTSmark,String kidid) {
        Iterator<String> iterator = map.keySet().iterator();
        setRetStr("");
        while (iterator.hasNext()) {
            String tssuuid = iterator.next();
            String tkidid = redisFroTSmark.getKididFromTssuuid(tssuuid);
            if (tkidid != null) {
                if (tkidid.equals(kidid)) {
                    setRetStr(tssuuid);
                    break;
                }
            }
        }
    }
}
