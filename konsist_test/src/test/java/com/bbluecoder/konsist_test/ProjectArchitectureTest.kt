package com.bbluecoder.konsist_test

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import org.junit.Test

class ProjectArchitectureTest {

    @Test
    fun `clean architecture layers have correct dependencies`() {
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                val presentation = Layer("Ui", "com.bluecoder.cloudcastle.ui..")
                val data = Layer("Data", "com.bluecoder.cloudcastle.data..")


                data.dependsOnNothing()
                presentation.dependsOn(data)
            }
    }
}