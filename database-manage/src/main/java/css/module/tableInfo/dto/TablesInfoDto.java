package css.module.tableInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/24 10:00
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TablesInfoDto {

    private int tableNum;

    private List<String> tables;
}
