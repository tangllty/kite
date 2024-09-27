@file:OptIn(ExperimentalEncodingApi::class)

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
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.toString

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

//        val token = Base64.Default.encode("$username:$password".toByteArray())
//        val name = "${project.group}:${project.name}:${project.version}"
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create("https://central.sonatype.com/api/v1/publisher/upload/?publishingType=AUTOMATIC&?name=$name"))
//            .header("accept", "multipart/form-data")
//            .header("Authorization", "Bearer $token")
//            .POST(HttpRequest.BodyPublishers.ofByteArray(rootPath.resolve(fileName).readBytes()))
//            .build()
//        val response = client.send(request, BodyHandlers.ofString())
//        var body = response.body()
//        println("response: $body")
    }

}
