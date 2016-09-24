
import java.net.URL
import java.net.URLClassLoader

/**
 * Created by ice1000 on 2016/9/24.
 *
 * @author ice1000
 */
fun main(args: Array<String>) {
	URLClassLoader(Array(1, { i-> URL("file:compile") })).loadClass("ThisGame").newInstance()
}