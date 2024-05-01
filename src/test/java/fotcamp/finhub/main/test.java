package fotcamp.finhub.main;


import fotcamp.finhub.admin.repository.CategoryRepository;
import fotcamp.finhub.admin.repository.TopicRepository;
import fotcamp.finhub.common.domain.Category;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Transactional
public class test {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void deleteTest(){
        Category category = categoryRepository.findById(1L).orElseThrow(() -> {
            throw new EntityNotFoundException("에러");
        });
        try {
            categoryRepository.delete(category);
            System.out.println("delete되는거지?");
        }catch (RuntimeException e){
            System.out.println("연관된 topic있다.");
        }
    }
}
