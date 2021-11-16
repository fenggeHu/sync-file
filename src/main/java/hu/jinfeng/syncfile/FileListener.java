package hu.jinfeng.syncfile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 文件变化监听器
 */
@Slf4j
@Service
public class FileListener extends FileAlterationListenerAdaptor {
    @Autowired
    private SyncFileService syncFileService;
    @Autowired
    private Config config;

    private int pathIndex;

    @PostConstruct
    public void init() {
        pathIndex = config.getSourceAbsolutePath().length();
        if (!config.getSourceAbsolutePath().endsWith(File.separator) && !config.getSourceAbsolutePath().endsWith("/")) {
            pathIndex += 1;
        }
    }

    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
        System.out.println("[新建]:" + file.getAbsolutePath());
        log.info("[新建]:" + file.getAbsolutePath());
        if (file.getAbsolutePath().startsWith(config.getSourceAbsolutePath())) {
            Record record = Record.builder().absolutePath(file.getAbsolutePath())
                    .path(file.getAbsolutePath().substring(pathIndex))
                    .status(Record.Status.C)
                    .build();
            syncFileService.sync(record);
        }
    }

    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {
        System.out.println("[修改]:" + file.getAbsolutePath());
        log.info("[修改]:" + file.getAbsolutePath());
        if (file.getAbsolutePath().startsWith(config.getSourceAbsolutePath())) {
            Record record = Record.builder().absolutePath(file.getAbsolutePath())
                    .path(file.getAbsolutePath().substring(pathIndex))
                    .status(Record.Status.U)
                    .build();
            syncFileService.sync(record);
        }
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        System.out.println("[删除]:" + file.getAbsolutePath());
        log.info("[删除]:" + file.getAbsolutePath());
        if (file.getAbsolutePath().startsWith(config.getSourceAbsolutePath())) {
            Record record = Record.builder().absolutePath(file.getAbsolutePath())
                    .path(file.getAbsolutePath().substring(pathIndex))
                    .status(Record.Status.D)
                    .build();
            syncFileService.sync(record);
        }
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        System.out.println("[新建]:" + directory.getAbsolutePath());
        log.info("[新建]:" + directory.getAbsolutePath());
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        System.out.println("[修改]:" + directory.getAbsolutePath());
        log.info("[修改]:" + directory.getAbsolutePath());
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        System.out.println("[删除]:" + directory.getAbsolutePath());
        log.info("[删除]:" + directory.getAbsolutePath());
    }

    public void onStart(FileAlterationObserver observer) {
// TODO Auto-generated method stub
        super.onStart(observer);
    }

    public void onStop(FileAlterationObserver observer) {
// TODO Auto-generated method stub
        super.onStop(observer);
    }
}