package css.common.consts;

import java.util.HashSet;
import java.util.Set;

/**
 * 关键字，有需要再继续加
 * @author Ng_Chun-fai
 * @date 2021年12月10日 15:46
 */
public class SqlKeyword {

    public static final Set<String> SQL_SERVER_KEYWORDS = new HashSet<String>(2) {{
        add("key");
        add("order");
    }};

    public static final Set<String> MYSQL_KEYWORDS = new HashSet<String>(1) {{
        add("order");
        add("describe");
    }};

    public static final Set<String> SQLITE_KEYWORDS = new HashSet<String>(1) {{
        add("order");
    }};
}
