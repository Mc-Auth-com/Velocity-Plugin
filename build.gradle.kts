import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.taskTriggers

plugins {
    alias(libs.plugins.jetbrains.ideaExt)
}

idea {
    project {
        settings {
            taskTriggers {
                afterSync("plugin:generateTemplates")
            }
        }
    }
}
