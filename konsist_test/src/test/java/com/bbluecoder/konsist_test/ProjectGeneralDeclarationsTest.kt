package com.bbluecoder.konsist_test

import androidx.lifecycle.ViewModel
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentOf
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ProjectGeneralDeclarationsTest {

    @Test
    fun `every repository reside in data repos package`(){
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("Repo")
            .assertTrue { it.resideInPackage("..data.repos..") }
    }

    @Test
    fun `every class in repos package should end with repo suffix`(){
        Konsist
            .scopeFromPackage("..data.repo")
            .classes()
            .assertTrue { it.hasNameEndingWith("Repo") }
    }

    @Test
    fun `every repository has a test`(){
        Konsist
            .scopeFromPackage("..data.repo")
            .classes()
            .assertTrue { it.hasTestClass() }
    }

    @Test
    fun `classes extending 'ViewModel' should have 'ViewModel' suffix`(){
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(ViewModel::class)
            .assertTrue { it.hasNameEndingWith("ViewModel") }
    }

    @Test
    fun `every ViewModel reside in UI layer`(){
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("ViewModel")
            .assertTrue { it.resideInPackage("..ui.screens..") }
    }
}