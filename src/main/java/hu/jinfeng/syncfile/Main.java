package hu.jinfeng.syncfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@EnableScheduling
@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan(basePackages = {"hu.jinfeng.syncfile"})
public class Main {

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            log.info("SpringBoot 默认Bean：");
            String[] beanNames = context.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            Arrays.stream(beanNames).forEach(System.out::println);

//            RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
//            Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
//            map.entrySet().stream().forEach(m ->{
//                HandlerMethod method = m.getValue();
//                log.info("className: {}, method: {}", method.getMethod().getDeclaringClass().getName(), method.getMethod().getName());
//                RequestMappingInfo k = m.getKey();
//                PatternsRequestCondition p = k.getPatternsCondition();
//                for(String url : p.getPatterns()){
//                    log.info("url: {}", url);
//                }
//            });
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

//        // 创建过滤器
//        IOFileFilter directories = FileFilterUtils.and(
//                FileFilterUtils.directoryFileFilter(),
//                HiddenFileFilter.VISIBLE);
//        IOFileFilter files = FileFilterUtils.and(
//                FileFilterUtils.fileFileFilter(),
//                FileFilterUtils.suffixFileFilter(".java"));
//        IOFileFilter filter = FileFilterUtils.or(directories, files);
//        // 使用过滤器
//        FileAlterationObserver observer = new FileAlterationObserver(new File(sourcePath), filter);
//        //不使用过滤器
//        //FileAlterationObserver observer = new FileAlterationObserver(new File(sourcePath));
//        observer.addListener(new FileListener());
//        //创建文件变化监听器
//        FileAlterationMonitor monitor = new FileAlterationMonitor(1000, observer);
//        // 开始监控
//        monitor.start();

//        //创建一个文件系统的监听服务
//        WatchService watchService = FileSystems.getDefault().newWatchService();
//
//        Path path = Paths.get(sourcePath);
//        //为该文件夹注册监听,监听新建、修改、删除事件。只能为文件夹（目录）注册监听，不能为单个文件注册监听
//        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
//        //编写事件处理
//        while (true) {  //一直监听
//            //拉取一个WatchKey。当触发监听的事件时，就会产生一个WatchKey，此WatchKey封装了事件信息。
//            WatchKey watchKey = watchService.take();
//
//            //使用循环是因为这一个WatchKey中可能有多个文件变化了，比如Ctrl+A全选，然后删除，只触发了一个WatchKey，但有多个文件变化了
//            for (WatchEvent event : watchKey.pollEvents()) {
//                System.out.println(event.context() + "发生了" + event.kind() + "事件！");
//                                 /*
//21                 watchKey.pollEvents()  获取此次WatchKey中所有变化了的文件的信息，以List（列表）形式返回，一个WatchEvent就是一个元素，封装了一个变化了的文件的信息
//22                 event.context()  获取文件名
//23                 event.kind()  获取发生的事件类型
//24
//25                 因为只能为文件夹注册监听，如果要监听某些指定的文件，可以在增强的for循环中，先根据event.context()判断是否是指定的文件，是才处理。
//26                  */
//            }
//
//            //虽然是while()循环，但WatchKey和ByteBuffer一样，使用完要重置状态，才能继续用。
//            watchKey.reset();    //如果不重置，WatchKey使用一次过后就不能再使用，即只能监听到一次文件变化。
//        }
    }
}
