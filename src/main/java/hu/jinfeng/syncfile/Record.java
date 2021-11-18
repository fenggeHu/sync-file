package hu.jinfeng.syncfile;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: 文件变化的记录信息
 * @Author Jinfeng.hu  @Date 2021/11/16
 **/
@Data
@Builder
public class Record {
    /**
     * 相对路径
     */
    private String path;
    /**
     * 绝对路径
     */
    private String absolutePath;
    /**
     * 是否文件夹/目录
     */
    private boolean directory;
    /**
     * 记录状态
     */
    private Status status;

    /**
     * 文件变动状态
     */
    enum Status {
        C("增"), D("删"), U("改");
        String value;

        Status(String s) {
            this.value = s;
        }
    }
}
