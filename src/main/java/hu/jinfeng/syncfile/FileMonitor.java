package hu.jinfeng.syncfile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * * 文件变化监听器
 * * 在Apache的Commons-IO中有关于文件的监控功能的代码. 文件监控的原理如下：
 * * 由文件监控类FileAlterationMonitor中的线程不停的扫描文件观察器FileAlterationObserver，
 * * 如果有文件的变化，则根据相关的文件比较器，判断文件时新增，还是删除，还是更改。（默认为1000毫秒执行一次扫描）
 *
 * @Author Jinfeng.hu  @Date 2021/11/16
 **/
@Slf4j
@Service
public class FileMonitor {
    @Autowired
    private FileListener fileListener;
    /**
     * 源目录
     */
    @Value("${file.source.path:}")
    private String source;
    /**
     * 目标目录
     */
    @Value("${file.target.path:}")
    private String target;
    /**
     * 文件类型
     */
    @Value("${file.suffix:}")
    private String[] suffix;
    /**
     * 检查时间 - ms
     */
    @Value("${file.interval:1000}")
    private Long interval;

    @PostConstruct
    public void init() throws Exception {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            throw new RuntimeException("源目录不存在：" + source);
        } else {
            if (!sourceFile.isDirectory()) {
                throw new RuntimeException("源目标不是目录：" + source);
            }
        }
        File targetFile = new File(target);
        if (!targetFile.exists()) {
            log.warn("目标目录不存在" + target);
            if (targetFile.mkdirs()) {
                log.warn("创建目录：" + target);
            } else {
                throw new RuntimeException("创建目标目录失败：" + target);
            }
        } else {
            if (!targetFile.isDirectory()) {
                throw new RuntimeException("目标不是目录：" + target);
            }
        }

        // 创建过滤器 - 只监测可见文件
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter[] suffixFilters = new IOFileFilter[suffix.length];
        for (int i = 0; i < suffix.length; i++) {
            suffixFilters[i] = FileFilterUtils.suffixFileFilter(suffix[i]);
        }
        IOFileFilter suffixFilter = FileFilterUtils.or(suffixFilters);
        IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), suffixFilter);
        IOFileFilter filter = FileFilterUtils.or(directories, files);
        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(sourceFile, filter);
        //不使用过滤器
        //FileAlterationObserver observer = new FileAlterationObserver(new File(sourcePath));
        observer.addListener(fileListener);
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        monitor.start();
    }
}
