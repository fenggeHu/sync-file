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
    @Autowired
    private Config config;


    @PostConstruct
    public void init() throws Exception {
        File sourceFile = config.getSourcePath();
        if (!sourceFile.exists()) {
            throw new RuntimeException("源目录不存在：" + config.getSource());
        } else {
            if (!sourceFile.isDirectory()) {
                throw new RuntimeException("源目标不是目录：" + config.getSource());
            }
        }
        File targetFile = new File(config.getTarget());
        if (!targetFile.exists()) {
            log.warn("目标目录不存在" + config.getTarget());
            if (targetFile.mkdirs()) {
                log.warn("创建目录：" + config.getTarget());
            } else {
                throw new RuntimeException("创建目标目录失败：" + config.getTarget());
            }
        } else {
            if (!targetFile.isDirectory()) {
                throw new RuntimeException("目标不是目录：" + config.getTarget());
            }
        }

        // 创建过滤器 - 只监测可见文件
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter[] suffixFilters = new IOFileFilter[config.getSuffix().length];
        for (int i = 0; i < config.getSuffix().length; i++) {
            suffixFilters[i] = FileFilterUtils.suffixFileFilter(config.getSuffix()[i]);
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
        FileAlterationMonitor monitor = new FileAlterationMonitor(config.getInterval(), observer);
        // 开始监控
        monitor.start();
    }
}
