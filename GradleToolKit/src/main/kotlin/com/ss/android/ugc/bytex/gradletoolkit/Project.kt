package com.ss.android.ugc.bytex.gradletoolkit

import org.gradle.api.Project

fun Project.findVariantScope(variantName: String): Any? {
//    return AGP.withAndroidPlugin(this).variantManager.variants.firstOrNull { it.name==variantName }?.originScope()
    // FIXME
    return null
}