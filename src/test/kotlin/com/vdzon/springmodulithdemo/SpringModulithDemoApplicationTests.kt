package com.vdzon.springmodulithdemo

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter

class SpringModulithDemoApplicationTests {

    private val modules = ApplicationModules.of(SpringModulithDemoApplication::class.java)

    @Test
    fun verifyModules() {
        modules.verify()
    }

    @Test
    fun createDocumentation() {
        Documenter(modules)
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml()
    }
}
