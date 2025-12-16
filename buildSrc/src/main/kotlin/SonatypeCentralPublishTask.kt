import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.plugins.signing.SigningExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.encoding.Base64

/**
 * @author Tang
 */
abstract class SonatypeCentralPublishTask : DefaultTask() {

    init {
        group = "publishing"
        description = "Publishes the project to Sonatype Central Repository"
        dependsOn(
            "clean",
            "publishMavenJavaPublicationToProjectRepository"
        )
    }

    @get:Input
    var username: String = ""

    @get:Input
    var password: String = ""

    private val client = HttpClient.newHttpClient()

    private val signingExtension: SigningExtension = project.extensions.getByType(SigningExtension::class.java)

    @TaskAction
    fun publishToSonatypeCentral() {
        val rootPath = project.layout.buildDirectory.get().asFile.resolve("repositories")
        val fileName = project.name + "-" + project.version + "-bundle.zip"
        val zipFile = ZipOutputStream(rootPath.resolve(fileName).outputStream())
        zipFile.use { output ->
            val ioFolder = rootPath.resolve("io")
            val entry = ZipEntry(rootPath.toPath().relativize(ioFolder.toPath()).toString() + "/")
            output.putNextEntry(entry)
            output.closeEntry()
            project.fileTree(ioFolder).forEach { file ->
                if (file.isFile) {
                    val fileEntry = ZipEntry(rootPath.toPath().relativize(file.toPath()).toString())
                    output.putNextEntry(fileEntry)
                    file.inputStream().use { it.copyTo(output) }
                    output.closeEntry()
                }
            }
        }

        val token = Base64.encode("$username:$password".toByteArray())
        val boundary = "----WebKitFormBoundary" + System.currentTimeMillis()
        val fileBytes = rootPath.resolve(fileName).readBytes()
        val newLine = "\r\n"
        val formData = buildString {
            append("--$boundary$newLine")
            append("Content-Disposition: form-data; name=\"bundle\"; filename=\"$fileName\"\r\n")
            append("Content-Type: application/zip\r\n\r\n")
        }.toByteArray() + fileBytes + "\r\n--$boundary--\r\n".toByteArray()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(UPLOAD))
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "multipart/form-data; boundary=$boundary")
            .POST(HttpRequest.BodyPublishers.ofByteArray(formData))
            .build()
        val response = client.send(request, BodyHandlers.ofString())
        val body = response.body()
        println("response: $response")
        println("body: $body")
    }

    companion object {

        private const val PUBLISHER_BASEURL = "https://central.sonatype.com/api/v1/publisher"

        const val UPLOAD = "$PUBLISHER_BASEURL/upload"

        const val STATUS = "$PUBLISHER_BASEURL/status"

        const val DEPLOYMENT = "$PUBLISHER_BASEURL/deployment"

    }

}
