package spec

import com.sportphotos.Application
import com.sportphotos.integration.MongoDatabaseInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = [Application], webEnvironment = RANDOM_PORT)
@ActiveProfiles('test')
@ContextConfiguration(initializers = [MongoDatabaseInitializer])
@interface FunctionalTest {
}