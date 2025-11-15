package artycorp.notez

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotezApplication

fun main(args: Array<String>) {
	runApplication<NotezApplication>(*args)
}
