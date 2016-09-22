import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by watson zhang on 16/9/18.
 */

@EnableAutoConfiguration()
@SpringBootApplication
@ComponentScan
@ImportResource({"classpath:applicatoin.xml"})
public class Entry {

    public static void main(String[] args){
        new SpringApplication(Entry.class).run(args);
    }

}
