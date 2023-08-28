package com.ss.android.ugc.bytex.gradletoolkit

import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.publishing.AndroidArtifacts
import com.didiglobal.booster.gradle.allClasses
import com.didiglobal.booster.gradle.apk
import com.didiglobal.booster.gradle.getArtifactCollection
import com.didiglobal.booster.gradle.mergedAssets
import com.didiglobal.booster.gradle.mergedManifests
import com.didiglobal.booster.gradle.mergedRes
import com.didiglobal.booster.gradle.processedRes
import com.didiglobal.booster.gradle.symbolList
import com.didiglobal.booster.gradle.symbolListWithPackageName
import com.google.auto.service.AutoService
import java.io.File
import java.util.*
import kotlin.streams.asStream
import kotlin.streams.toList

/**
 * Created by tanlehua on 2019-04-29.
 */
@AutoService(TransformEnv::class)
class TransformEnvImpl() : TransformEnv {
    private var invocation: TransformInvocation? = null

    override fun setTransformInvocation(invocation: TransformInvocation) {
        this.invocation = invocation
    }

    override fun getArtifact(artifact: Artifact): Collection<File> {
        if (invocation == null) {
            return Collections.emptyList()
        }
        return when (artifact) {
//            Artifact.AAR -> invocation!!.variant.aarArtifactCollection()
//            Artifact.JAR -> invocation!!.variant.jarArtifactCollection()
//            Artifact.PROCESSED_JAR -> {
//                if (AGP.agpVersionCode() >= 400) {
//                    invocation!!.variant.processedJarArtifactCollection()
//                } else {
//                    invocation!!.variant.jarArtifactCollection()
//                }
//            }
//            Artifact.CLASSES -> invocation!!.variant.classesArtifactCollection()
//            Artifact.ALL_CLASSES -> invocation!!.variant.allClasses
//            Artifact.APK -> invocation!!.variant.apk
//            Artifact.JAVAC -> invocation!!.variant.javacClasses
//            Artifact.MERGED_ASSETS -> invocation!!.variant.mergedAssets
//            Artifact.MERGED_RES -> invocation!!.variant.mergedRes
//            Artifact.MERGED_MANIFESTS -> invocation!!.variant.mergedManifestFiles
//            Artifact.MERGED_MANIFESTS_WITH_FEATURES -> invocation!!.variant.mergedManifestFilesWithFeatures
//            Artifact.PROCESSED_RES -> invocation!!.variant.processedRes
//            Artifact.SYMBOL_LIST -> invocation!!.variant.symbolList
//            Artifact.SYMBOL_LIST_WITH_PACKAGE_NAME -> invocation!!.variant.symbolListWithPackageName
//            Artifact.RAW_RESOURCE_SETS -> invocation!!.variant.mergeResources.computeResourceSetList0()
//                ?: emptyList()
//            Artifact.RAW_ASSET_SETS -> invocation!!.variant.mergeSourceSet.assetSetList()
            Artifact.AAR -> {
                invocation!!.variant.getArtifactCollection(
                    AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
                    AndroidArtifacts.ArtifactScope.ALL,
                    AndroidArtifacts.ArtifactType.AAR
                ).artifactFiles.files
            }

            Artifact.JAR -> {
                invocation!!.variant.getArtifactCollection(
                    AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
                    AndroidArtifacts.ArtifactScope.ALL,
                    AndroidArtifacts.ArtifactType.JAR
                ).artifactFiles.files
            }

            Artifact.PROCESSED_JAR -> {
                invocation!!.variant.getArtifactCollection(
                    AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
                    AndroidArtifacts.ArtifactScope.ALL,
                    if (ANDROID_GRADLE_PLUGIN_VERSION.major > 3 || ANDROID_GRADLE_PLUGIN_VERSION.minor >= 2) {
                        AndroidArtifacts.ArtifactType.PROCESSED_JAR
                    } else {
                        AndroidArtifacts.ArtifactType.JAR
                    }
                ).artifactFiles.files
            }

            Artifact.CLASSES -> {
                invocation!!.variant.getArtifactCollection(
                    AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
                    AndroidArtifacts.ArtifactScope.ALL,
                    AndroidArtifacts.ArtifactType.CLASSES
                ).artifactFiles.files
            }

            Artifact.ALL_CLASSES -> invocation!!.variant.allClasses.files
            Artifact.APK -> invocation!!.variant.apk.files
            Artifact.JAVAC -> emptyList()
            Artifact.MERGED_ASSETS -> invocation!!.variant.mergedAssets.files
            Artifact.MERGED_RES -> invocation!!.variant.mergedRes.files
            Artifact.MERGED_MANIFESTS -> invocation!!.variant.mergedAssets.flatMap {
                when {
                    it.isDirectory -> it.walk().asStream().filter { it.isFile }.toList()
                    it.isFile -> listOf(it)
                    else -> emptyList()
                }.filter { file ->
                    file.isFile && file.name.endsWith(".xml")
                }
            }

            Artifact.MERGED_MANIFESTS_WITH_FEATURES ->
                invocation!!.project.rootProject.subprojects
                    .mapNotNull { it.extensions.findByName("android") as? AppExtension? }
                    .map {
                        var result: ApplicationVariant? = null
                        it.applicationVariants.forEach { v ->
                            if (invocation!!.context.variantName.contains(v.name) && (result == null || v.name.contains(result!!.name))) {
                                result = v as ApplicationVariant
                            }
                        }
                        result!!
                    }.flatMap { v ->
                        v.mergedManifests
                    }
                    .flatMap {
                        when {
                            it.isDirectory -> it.walk().asStream().filter { it.isFile }.toList()
                            it.isFile -> listOf(it)
                            else -> emptyList()
                        }
                    }.filter { it ->
                        it.isFile && it.name.endsWith(".xml")
                    }.toList()

            Artifact.PROCESSED_RES -> invocation!!.variant.processedRes.files
            Artifact.SYMBOL_LIST -> invocation!!.variant.symbolList.files
            Artifact.SYMBOL_LIST_WITH_PACKAGE_NAME -> invocation!!.variant.symbolListWithPackageName.files
            // FIXME
//            Artifact.RAW_RESOURCE_SETS -> invocation!!.variant.mergeResources.resourceSetList()
//            Artifact.RAW_ASSET_SETS -> invocation!!.variant.mergeAssets.assetSetList()
            else -> emptyList()

        }
    }
}