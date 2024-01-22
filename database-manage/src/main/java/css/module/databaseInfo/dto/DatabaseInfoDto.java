package css.module.databaseInfo.dto;

import css.module.tableInfo.dto.TablesInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @Description: TODO
 * @Author: CSS
 * @Date: 2023/11/24 9:58
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseInfoDto {

    private int databaseNum;

    private HashMap<String, TablesInfoDto> databases;
}
