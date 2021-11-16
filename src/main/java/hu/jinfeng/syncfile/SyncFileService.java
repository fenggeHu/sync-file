package hu.jinfeng.syncfile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: 文件同步
 * @Author Jinfeng.hu  @Date 2021-11-2021/11/16
 **/
@Slf4j
@Service
public class SyncFileService {
    @Autowired
    private Config config;


    private Queue<Record> queue = new LinkedBlockingQueue<>();

    @Scheduled(fixedDelayString = "1000")
    public void run() throws IOException {
        Record record = queue.poll();
        if (null != record) {    // 没有记录休眠
            File targetFile = new File(config.getTarget(), record.getPath());
            log.info("from {} to {}", record.getAbsolutePath(), targetFile.getAbsolutePath());
            // 复制文件
            if (record.getStatus() == Record.Status.C || record.getStatus() == Record.Status.U) {
                File sourceFile = new File(record.getAbsolutePath());
                FileUtils.copyFile(sourceFile, targetFile);
            } else if (record.getStatus() == Record.Status.D) {
                FileUtils.delete(targetFile);
            } else {
                throw new RuntimeException("Not support!");
            }
        }
    }

    /**
     * 文件变化同步
     *
     * @param record
     */
    public void sync(Record record) {
        if (null == record) return;
        queue.add(record);
    }


}
