import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
//    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    // Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.15.0"
    // Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "2.2.0"
}

dependencies {
    testCompileOnly("com.alibaba:fastjson:1.2.83")
    testCompileOnly("com.fasterxml.jackson.core:jackson-annotations:2.11.0")
    // https://projectlombok.org/setup/gradle
    testCompileOnly("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
}

group = "ink.organics"
version = "1.2.6"


repositories {
    mavenCentral()
}

intellij {
    version.set("2022.3")
    updateSinceUntilBuild.set(false)
    // https://github.com/JetBrains/gradle-intellij-plugin/issues/38
    plugins.set(listOf("java", "Kotlin"))
}

changelog {
    version.set("${project.version}")
    path.set("${project.projectDir}/CHANGELOG.md")
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    wrapper {
        gradleVersion = "8.3"
    }

    buildSearchableOptions {
        enabled = false
    }

    instrumentCode {
        enabled = false
    }

    instrumentTestCode {
        enabled = false
    }

    // TODO https://youtrack.jetbrains.com/issue/IDEA-278926#focus=Comments-27-5561012.0-0
    val test by getting(Test::class) {
        setScanForTestClasses(false)
        // Only run tests from classes that end with "Test"
        include("**/JavaTestCase.class")
        include("**/KotlinTestCase.class")
    }

    test {
        // 这个路径下要存在mockJDK，其目录结构为 java/mockJDK-$JAVA_VERSION$
        // https://plugins.jetbrains.com/docs/intellij/testing-faq.html#how-to-test-a-jvm-language
        systemProperties(Pair("idea.home.path", project.projectDir))
    }

    patchPluginXml {
        sinceBuild.set("223")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            File(projectDir, "README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider { changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML) })
    }

    signPlugin {
        certificateChain.set(File(projectDir, ".keys/chain.crt").readText(Charsets.UTF_8))
        privateKey.set(File(projectDir, ".keys/private.pem").readText(Charsets.UTF_8))
        password.set(properties("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(properties("PUBLISH_TOKEN"))
    }
}



