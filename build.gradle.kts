plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.99-stable")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("org.bstats:bstats-bukkit:3.2.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    runServer {
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        dependencies {
            // Only merge bStats into the final jar, no other dependencies
            exclude { it.moduleGroup != "org.bstats" }
        }

        // Relocate bStats into the plugin's package to avoid conflicts with other
        // plugins using bStats
        relocate("org.bstats", project.group.toString())
    }
}
