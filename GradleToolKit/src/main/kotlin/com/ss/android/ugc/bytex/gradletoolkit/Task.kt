package com.ss.android.ugc.bytex.gradletoolkit


import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Task

val Task.variant: BaseVariant?
    get() = this.let {
//        if(!AGP.isAndroidModule(project)) return@let null
//        val android = AGP.withAndroidExtension(project)
//        val allVariant = android.variants
        val android = project.extensions.findByName("android") as? BaseExtension ?: return@let null
        val allVariant = when (android) {
            is AppExtension -> {
                android.applicationVariants.map { v -> v as BaseVariant }
            }

            is LibraryExtension -> {
                android.libraryVariants.map { v -> v as BaseVariant }
            }

            else -> {
                return@let null
            }
        }
        var matchedVariant: BaseVariant? = null
        for (variant in allVariant) {
            if (it.name.endsWith(variant.name.capitalize())) {
                if (matchedVariant == null || matchedVariant.name.length < variant.name.length) {
//                    matchedVariant = variant.raw()
                    matchedVariant = variant
                }
            }
        }
        matchedVariant
    }