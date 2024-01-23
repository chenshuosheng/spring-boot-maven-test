package css.testmybatis.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import css.testmybatis.entity.InputData;
import css.testmybatis.service.InputDataService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/20 14:17
 */
@Component
public class InputDataListener implements ReadListener<InputData> {

    private static final int BATCH_SIZE = 10;

    private List<InputData> list = ListUtils.newArrayListWithExpectedSize(BATCH_SIZE);

    private InputDataService inputDataService;

    public InputDataListener(InputDataService inputDataService) {
        this.inputDataService = inputDataService;
    }

    @Override
    public void invoke(InputData data, AnalysisContext context) {
        //对数据进行过滤，修改
        list.add(data);
        if (list.size() >= BATCH_SIZE) {
            inputDataService.saveList(list);
            list = ListUtils.newArrayListWithExpectedSize(BATCH_SIZE);
        }
    }

    //保证最后一批数据也能正常保存
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (list.size() > 0)
            inputDataService.saveList(list);
    }
}
