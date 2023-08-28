package com.ss.android.ugc.bytex.gradletoolkit

import com.android.build.gradle.api.BaseVariant

val BaseVariant.scope: Any?
    //    get() =  compatVariant.variantScope.originScope()
    // FIXME
    get() = null
