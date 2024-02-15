package fotcamp.finhub.admin.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "promise")
@Getter
@Setter
public class PromiseUtils {
    private String category;
    private String topic;
    private String usertype;
}
