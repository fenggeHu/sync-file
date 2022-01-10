package hu.jinfeng.syncfile;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @Description:
 * @Author Jinfeng.hu  @Date 2021-11-2021/11/16
 **/
@Data
@Configuration
public class Config {

    /**
     * 源目录
     */
    @Value("${file.source.path:}")
    private String source;
    private File sourcePath;

    public String getSourceAbsolutePath() {
        return sourcePath.getAbsolutePath();
    }

    /**
     * 目标目录
     */
    @Value("${file.target.path:}")
    private String target;
    private File targetPath;

    public String getTargetAbsolutePath() {
        return targetPath.getAbsolutePath();
    }

    /**
     * 文件类型
     */
    @Value("${file.suffix:}")
    private String[] suffix;
    /**
     * 忽略的路径或文件
     */
    @Value("${file.ignore:}")
    private String[] ignore;
    /**
     * 检查时间 - ms
     */
    @Value("${file.interval:1000}")
    private Long interval;

    @PostConstruct
    public void init() {
        this.sourcePath = new File(this.source);
        this.targetPath = new File(this.target);
    }
}
