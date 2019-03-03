import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Description.
 *
 * @author : CeaserWang
 * @version : 1.0
 * @since : 2019/2/23 14:51
 */
public class TestLinkedHashMap {

    public static void main(String[] args) {
        LinkedHashMap<String, String> linkedHashMap =
                new LinkedHashMap<String, String>(16, 0.75f, false);
        linkedHashMap.put("111", "111");
        linkedHashMap.put("222", "222");
        linkedHashMap.put("333", "333");
        linkedHashMap.put("444", "444");
        loopLinkedHashMap(linkedHashMap);
        linkedHashMap.get("111");
        loopLinkedHashMap(linkedHashMap);
        linkedHashMap.put("222", "2222");
        loopLinkedHashMap(linkedHashMap);
    }

    public static void loopLinkedHashMap(LinkedHashMap<String, String> linkedHashMap)
    {
        Set<Map.Entry<String, String>> set = linkedHashMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();

        while (iterator.hasNext())
        {
            System.out.print(iterator.next() + "\t");
        }
        System.out.println();
    }
}
