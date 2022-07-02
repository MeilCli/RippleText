package net.meilcli.rippletext.gradle.extensions

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.api(dependency: String) {
    add("api", dependency)
}

fun DependencyHandlerScope.testImplementation(dependency: String) {
    add("testImplementation", dependency)
}

fun DependencyHandlerScope.testRuntimeOnly(dependency: String) {
    add("testRuntimeOnly", dependency)
}

fun DependencyHandlerScope.androidTestImplementation(dependency: String) {
    add("androidTestImplementation", dependency)
}