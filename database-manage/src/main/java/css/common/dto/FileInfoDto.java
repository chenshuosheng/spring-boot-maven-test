package css.common.dto;


import lombok.Data;

@Data
public class FileInfoDto {

    /** 文件后缀 */
    private String fileExt;
    /** 文件绝对路径 */
    private String fileAbsolutePath;
    /** 文件相对路径 */
    private String fileRelativePath;

}
